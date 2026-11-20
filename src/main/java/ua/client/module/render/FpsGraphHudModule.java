package ua.client.module.render;

import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import ua.client.config.ClientConfig;
import ua.client.module.Category;
import ua.client.module.Module;

public class FpsGraphHudModule extends Module {
    private final Deque<Integer> samples = new ArrayDeque<>();

    public FpsGraphHudModule() {
        super("FPSGraphHUD", Category.RENDER);
    }

    @Override
    public void onTick() {
        int fps = Math.max(1, mc().getCurrentFps());
        samples.addLast(fps);
        while (samples.size() > 60) samples.removeFirst();
    }

    public void drawOverlay(MatrixStack ms) {
        MinecraftClient mc = mc();
        if (samples.isEmpty()) return;
        int x = 8, y = 64, w = 64, h = 24;
        DrawableHelper.fill(ms, x, y, x + w, y + h, 0xAA120A1E);
        DrawableHelper.fill(ms, x, y, x + w, y + 1, ClientConfig.accentColor);
        int idx = 0;
        for (int fps : samples) {
            int barH = Math.max(2, Math.min(h - 4, (int) ((fps / 144.0) * (h - 4))));
            int bx = x + 2 + idx;
            DrawableHelper.fill(ms, bx, y + h - 2 - barH, bx + 1, y + h - 2, ClientConfig.secondaryColor);
            idx++;
        }
    }
}
