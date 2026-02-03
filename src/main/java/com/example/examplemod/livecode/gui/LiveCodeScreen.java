package com.example.examplemod.livecode.gui;

import com.example.examplemod.livecode.LiveCodeRuntime;
import com.example.examplemod.livecode.LiveCodeStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class LiveCodeScreen extends Screen {
    private final Screen parent;
    private MultiLineEditBox editor;
    private Component status = Component.empty();

    public LiveCodeScreen(Screen parent) {
        super(Component.literal("Live Code"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int margin = 12;
        int top = 28;
        int bottomButtons = 28;
        int w = this.width - margin * 2;
        int h = this.height - top - bottomButtons - margin;

        this.editor = new MultiLineEditBox(this.font, margin, top, w, h, Component.literal("Code"), Component.literal(""));
        this.editor.setCharacterLimit(200000);
        this.editor.setValue(LiveCodeRuntime.getSource());
        this.addRenderableWidget(this.editor);

        int by = this.height - margin - 20;
        int bw = 80;
        int gap = 6;

        int x = margin;
        this.addRenderableWidget(Button.builder(Component.literal("Reload"), b -> reloadFromEditor(false)).bounds(x, by, bw, 20).build());
        x += bw + gap;
        this.addRenderableWidget(Button.builder(Component.literal("Save"), b -> saveToConfig()).bounds(x, by, bw, 20).build());
        x += bw + gap;
        this.addRenderableWidget(Button.builder(Component.literal("Load"), b -> loadFromConfig()).bounds(x, by, bw, 20).build());
        x += bw + gap;
        this.addRenderableWidget(Button.builder(Component.literal("Export"), b -> exportToProject()).bounds(x, by, bw, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Close"), b -> onClose()).bounds(this.width - margin - bw, by, bw, 20).build());

        updateStatusFromRuntime();
    }

    private void updateStatusFromRuntime() {
        List<String> errors = LiveCodeRuntime.getLastErrors();
        if (errors.isEmpty()) {
            status = Component.literal("OK");
        } else {
            status = Component.literal("Errors: " + errors.size());
        }
    }

    private void reloadFromEditor(boolean persist) {
        LiveCodeRuntime.setSource(this.editor.getValue());
        LiveCodeRuntime.reloadFromCurrentSource(persist);
        updateStatusFromRuntime();
    }

    private void saveToConfig() {
        LiveCodeRuntime.setSource(this.editor.getValue());
        boolean ok = LiveCodeStorage.writeScript(LiveCodeRuntime.getSource());
        status = Component.literal(ok ? "Saved" : "Save failed");
    }

    private void loadFromConfig() {
        String s = LiveCodeStorage.readScriptOrNull();
        if (s == null) {
            status = Component.literal("No file");
            return;
        }
        LiveCodeRuntime.setSource(s);
        this.editor.setValue(s);
        LiveCodeRuntime.reloadFromCurrentSource(false);
        updateStatusFromRuntime();
    }

    private void exportToProject() {
        LiveCodeRuntime.setSource(this.editor.getValue());
        boolean ok = LiveCodeRuntime.exportCurrentSource();
        status = Component.literal(ok ? "Exported" : "Export failed");
    }

    @Override
    public void onClose() {
        LiveCodeRuntime.setSource(this.editor.getValue());
        Minecraft.getInstance().setScreen(parent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawString(this.font, this.title, 12, 10, 0xFFFFFF, false);
        graphics.drawString(this.font, status, 12, this.height - 12 - 20 - 10, 0xA0A0A0, false);
    }
}
