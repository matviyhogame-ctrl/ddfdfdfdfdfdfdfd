package ua.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class CustomTitleScreen extends Screen {
    public CustomTitleScreen() {
        super(new LiteralText("PastaClient Main Menu"));
    }

    @Override
    protected void init() {
        int bw = 180;
        int bh = 22;
        int bx = width / 2 - bw / 2;
        int startY = height / 2 - 22;

        addButton(new ButtonWidget(bx, startY, bw, bh, new LiteralText("Singleplayer"),
                btn -> client.openScreen(new SelectWorldScreen(this))));
        addButton(new ButtonWidget(bx, startY + 28, bw, bh, new LiteralText("Multiplayer"),
                btn -> client.openScreen(new MultiplayerScreen(this))));
        addButton(new ButtonWidget(bx, startY + 56, bw, bh, new LiteralText("Options"),
                btn -> client.openScreen(new OptionsScreen(this, client.options))));
        addButton(new ButtonWidget(bx, startY + 84, bw, bh, new LiteralText("Quit Game"),
                btn -> client.scheduleStop()));
    }

    @Override
    public void render(MatrixStack ms, int mx, int my, float delta) {
        fillGradient(ms, 0, 0, width, height, 0xFF140724, 0xFF2B0E4A);
        fill(ms, 0, 0, width, 3, 0xFF9B30FF);

        int cardW = 220;
        int cardH = 148;
        int cx = width / 2 - cardW / 2;
        int cy = height / 2 - 42;
        fill(ms, cx, cy, cx + cardW, cy + cardH, 0xAA1A0B2E);
        fill(ms, cx, cy, cx + cardW, cy + 1, 0xFF8A2BE2);
        fill(ms, cx, cy + cardH - 1, cx + cardW, cy + cardH, 0xFF8A2BE2);

        String title = "PASTA CLIENT";
        float scale = 1.6f;
        ms.push();
        ms.scale(scale, scale, 1f);
        int tx = (int) ((width / 2f - (textRenderer.getWidth(title) * scale) / 2f) / scale);
        int ty = (int) ((height / 2f - 78) / scale);
        textRenderer.drawWithShadow(ms, title, tx, ty, 0xFFD966FF);
        ms.pop();

        drawCenteredString(ms, textRenderer, "Fabric 1.16.5  •  Custom UI", width / 2, height / 2 - 55, 0xFFC299E6);

        super.render(ms, mx, my, delta);

        drawStringWithShadow(ms, textRenderer, "PastaClient Custom Menu", 8, height - 14, 0xFF9C72C4);
        String ver = "MC 1.16.5";
        drawStringWithShadow(ms, textRenderer, ver, width - textRenderer.getWidth(ver) - 8, height - 14, 0xFF9C72C4);
    }
}
