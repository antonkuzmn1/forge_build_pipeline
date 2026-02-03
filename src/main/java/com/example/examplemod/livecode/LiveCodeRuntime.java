package com.example.examplemod.livecode;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.livecode.gui.LiveCodeScreen;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class LiveCodeRuntime {
    private static final String DEFAULT_SOURCE = "on_reload msg LiveCode loaded\n\n" +
            "every 20 msg tick 20\n" +
            "every 100 run say server command from script\n";

    private static String currentSource = "";
    private static LiveCodeScript currentScript = null;
    private static List<String> lastErrors = Collections.emptyList();
    private static long tick = 0;

    private static KeyMapping openEditorKey;

    private LiveCodeRuntime() {
    }

    public static void bootstrap() {
        if (!currentSource.isEmpty()) {
            return;
        }
        String s = LiveCodeStorage.readScriptOrNull();
        if (s == null) {
            s = DEFAULT_SOURCE;
            LiveCodeStorage.writeScript(s);
        }
        currentSource = s;
        reloadFromCurrentSource(false);
    }

    public static String getSource() {
        return currentSource;
    }

    public static void setSource(String source) {
        currentSource = source == null ? "" : source;
    }

    public static List<String> getLastErrors() {
        return lastErrors;
    }

    public static void reloadFromCurrentSource(boolean persist) {
        if (persist) {
            LiveCodeStorage.writeScript(currentSource);
        }

        LiveCodeParser.Result r = LiveCodeParser.parse(currentSource);
        lastErrors = r.errors();
        if (!lastErrors.isEmpty() || r.script() == null) {
            currentScript = null;
            notifyClient("LiveCode: parse errors: " + lastErrors.size());
            return;
        }

        currentScript = r.script();
        Context ctx = new Context();
        for (LiveCodeScript.LiveCodeAction a : currentScript.onReload()) {
            safeExecute(ctx, a);
        }
        for (LiveCodeScript.LiveCodeAction a : currentScript.once()) {
            safeExecute(ctx, a);
        }
        notifyClient("LiveCode: reloaded");
    }

    public static boolean exportCurrentSource() {
        String raw = LiveCodeConfig.EXPORT_PATH.get();
        if (raw == null || raw.trim().isEmpty()) {
            notifyClient("LiveCode: exportPath is empty");
            return false;
        }
        Path p = Path.of(raw);
        boolean ok = LiveCodeStorage.exportTo(p, currentSource);
        if (!ok) {
            notifyClient("LiveCode: export failed");
        }
        return ok;
    }

    public static void openEditor() {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> mc.setScreen(new LiveCodeScreen(mc.screen)));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        bootstrap();

        tick++;

        if (openEditorKey != null && openEditorKey.consumeClick()) {
            openEditor();
        }

        if (currentScript == null) {
            return;
        }

        Context ctx = new Context();
        for (LiveCodeScript.TickTask t : currentScript.tickTasks()) {
            if (tick % t.intervalTicks() == 0) {
                safeExecute(ctx, t.action());
            }
        }
    }

    private static void safeExecute(Context ctx, LiveCodeScript.LiveCodeAction action) {
        try {
            action.execute(ctx);
        } catch (Throwable e) {
            notifyClient("LiveCode: runtime error: " + e.getClass().getSimpleName());
        }
    }

    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("livecode")
                .then(Commands.literal("open").executes(c -> {
                    openEditor();
                    return 1;
                }))
                .then(Commands.literal("reload").executes(c -> {
                    reloadFromCurrentSource(false);
                    return 1;
                }))
                .then(Commands.literal("save").executes(c -> {
                    LiveCodeStorage.writeScript(currentSource);
                    notifyClient("LiveCode: saved");
                    return 1;
                }))
                .then(Commands.literal("load").executes(c -> {
                    String s = LiveCodeStorage.readScriptOrNull();
                    if (s == null) {
                        notifyClient("LiveCode: no file");
                        return 0;
                    }
                    currentSource = s;
                    reloadFromCurrentSource(false);
                    return 1;
                }))
                .then(Commands.literal("export").executes(c -> exportCurrentSource() ? 1 : 0));

        event.getDispatcher().register(root);
    }

    @Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ModBusClient {
        private ModBusClient() {
        }

        @SubscribeEvent
        public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
            if (openEditorKey != null) {
                return;
            }
            openEditorKey = new KeyMapping("key.examplemod.livecode_open", GLFW.GLFW_KEY_F8, "key.categories.examplemod");
            event.register(openEditorKey);
        }
    }

    public static final class Context {
        public void notify(String msg) {
            notifyClient(msg);
        }

        public void runCommand(String command) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }

            Object connection = mc.getConnection();
            if (connection != null) {
                if (tryInvoke(connection, "sendCommand", new Class<?>[]{String.class}, new Object[]{command})) {
                    return;
                }
            }

            Object playerConnection = mc.player.connection;
            if (playerConnection != null) {
                tryInvoke(playerConnection, "sendCommand", new Class<?>[]{String.class}, new Object[]{command});
            }
        }

        private boolean tryInvoke(Object target, String method, Class<?>[] types, Object[] args) {
            try {
                target.getClass().getMethod(method, types).invoke(target, args);
                return true;
            } catch (Throwable ignored) {
                return false;
            }
        }
    }

    private static void notifyClient(String msg) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.sendSystemMessage(Component.literal(msg));
        }
    }
}
