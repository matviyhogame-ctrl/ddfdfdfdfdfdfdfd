package ua.client.module.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import ua.client.config.ClientConfig;
import ua.client.module.Category;
import ua.client.module.Module;

public class CompassHudModule extends Module {
    public CompassHudModule() {
        super("CompassHUD", Category.RENDER);
    }

    public void drawOverlay(MatrixStack ms) {
        MinecraftClient mc = mc();
        if (mc.player == null) return;
        float yaw = MathHelper.wrapDegrees(mc.player.yaw);
        String dir = yaw >= -45 && yaw < 45 ? "SOUTH (+Z)"
                : yaw >= 45 && yaw < 135 ? "WEST (-X)"
                : yaw < -45 && yaw >= -135 ? "EAST (+X)" : "NORTH (-Z)";
        String text = String.format(java.util.Locale.ROOT, "%s  %.0f", dir, yaw);
        int cx = mc.getWindow().getScaledWidth() / 2;
        int tw = mc.textRenderer.getWidth(text);
        DrawableHelper.fill(ms, cx - tw / 2 - 6, 8, cx + tw / 2 + 6, 22, 0xB8120A1E);
        DrawableHelper.fill(ms, cx - tw / 2 - 6, 21, cx + tw / 2 + 6, 22, ClientConfig.accentColor);
        mc.textRenderer.drawWithShadow(ms, text, cx - tw / 2f, 11, 0xFFFFFFFF);
    }
}
