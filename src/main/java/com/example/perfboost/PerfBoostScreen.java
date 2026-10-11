package com.example.perfboost;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class PerfBoostScreen extends Screen {
    private final Screen parent;

    public PerfBoostScreen(Screen parent) {
        super(Text.literal("PerfBoost"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int w = 220, h = 30;
        int x = this.width / 2 - w / 2;
        int y = this.height / 2 - 40;

        addDrawableChild(ButtonWidget.builder(getToggleText(), b -> {
            PerfBoost.setEnabled(!PerfBoost.enabled);
            b.setMessage(getToggleText());
        }).dimensions(x, y, w, h).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
                .dimensions(x, y + 50, w, h).build());
    }

    private Text getToggleText() {
        return Text.literal("Performance: " + (PerfBoost.enabled ? "ON" : "OFF"));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
