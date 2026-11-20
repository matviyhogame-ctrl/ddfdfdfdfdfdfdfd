package ua.client.module.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import ua.client.config.ClientConfig;
import ua.client.module.Category;
import ua.client.module.Module;

public class SessionTimerHudModule extends Module {
    private final long startMs = System.currentTimeMillis();

    public SessionTimerHudModule() {
        super("SessionTimerHUD", Category.RENDER);
    }

    public void drawOverlay(MatrixStack ms, int screenHeight) {
        MinecraftClient mc = mc();
        long sec = Math.max(0L, (System.currentTimeMillis() - startMs) / 1000L);
        long m = sec / 60L;
        long s = sec % 60L;
        String label = String.format(java.util.Locale.ROOT, "Session: %02d:%02d", m, s);
        int w = mc.textRenderer.getWidth(label) + 12;
        int y = screenHeight - 154;
        DrawableHelper.fill(ms, 8, y, 8 + w, y + 14, 0xB8120A1E);
        DrawableHelper.fill(ms, 8, y, 10, y + 14, ClientConfig.accentColor);
        mc.textRenderer.drawWithShadow(ms, label, 13, y + 3, 0xFFD9B3FF);
    }
}
