package ua.client.module.render;

import java.util.Locale;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import ua.client.config.ClientConfig;
import ua.client.module.Category;
import ua.client.module.Module;

public class CoordinatesHudModule extends Module {
    public CoordinatesHudModule() {
        super("CoordinatesHUD", Category.RENDER);
    }

    public void drawOverlay(MatrixStack ms, int screenHeight) {
        ClientPlayerEntity p = mc().player;
        if (p == null) return;
        String coords = String.format(Locale.ROOT, "X: %.0f  Y: %.0f  Z: %.0f", p.getX(), p.getY(), p.getZ());
        int w = mc().textRenderer.getWidth(coords) + 12;
        int y = screenHeight - 122;
        DrawableHelper.fill(ms, 8, y, 8 + w, y + 14, 0xB8120A1E);
        DrawableHelper.fill(ms, 8, y, 10, y + 14, ClientConfig.accentColor);
        mc().textRenderer.drawWithShadow(ms, coords, 13, y + 3, 0xFFE6CCFF);
    }
}
