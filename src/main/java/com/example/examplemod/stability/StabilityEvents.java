package com.example.examplemod.stability;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.content.block.GraveBlock;
import com.example.examplemod.content.block.SupportBlock;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class StabilityEvents {
    private static final int CHECK_INTERVAL_TICKS = 10;
    private static final int CHUNK_RADIUS = 2;
    private static final Long2LongOpenHashMap lastCheckByChunk = new Long2LongOpenHashMap();

    private StabilityEvents() {
    }

    @SubscribeEvent
    public static void onPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        BlockState state = event.getPlacedBlock();
        BlockPos pos = event.getPos();

        if (state.getBlock() instanceof GraveBlock) {
            StabilityManager.registerGrave(level, pos);
            return;
        }

        if (state.getBlock() instanceof SupportBlock support) {
            if (state.getValue(SupportBlock.HALF) == DoubleBlockHalf.LOWER) {
                StabilityManager.registerSupport(level, pos, support.type().radius());
            }
        }
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        if (state.getBlock() instanceof GraveBlock) {
            StabilityManager.unregisterCenter(level, pos);
        } else if (state.getBlock() instanceof SupportBlock) {
            BlockPos lower = state.getValue(SupportBlock.HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
            StabilityManager.unregisterCenter(level, lower);
        }
        Crumble.trigger(level, pos);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        long gameTime = event.getServer().overworld().getGameTime();
        if (gameTime % CHECK_INTERVAL_TICKS != 0) return;
        for (ServerLevel level : event.getServer().getAllLevels()) {
            for (ServerPlayer player : level.players()) {
                BlockPos p = player.blockPosition();
                int pcx = p.getX() >> 4;
                int pcz = p.getZ() >> 4;
                for (int dcx = -CHUNK_RADIUS; dcx <= CHUNK_RADIUS; dcx++) {
                    for (int dcz = -CHUNK_RADIUS; dcz <= CHUNK_RADIUS; dcz++) {
                        int cx = pcx + dcx;
                        int cz = pcz + dcz;
                        if (!level.hasChunk(cx, cz)) continue;
                        long key = (((long) cx) << 32) ^ (cz & 0xffffffffL);
                        long last = lastCheckByChunk.getOrDefault(key, Long.MIN_VALUE);
                        if (gameTime - last < CHECK_INTERVAL_TICKS) continue;
                        lastCheckByChunk.put(key, gameTime);
                        Crumble.triggerChunk(level, cx, cz);
                    }
                }
            }
        }
    }
}
