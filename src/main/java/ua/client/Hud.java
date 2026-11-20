package ua.client;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import ua.client.module.Extras;
import ua.client.module.Module;
import ua.client.module.ModuleManager;
import ua.client.module.Modules;

public class Hud {
    static final int PURPLE = 0xFF9B30FF;
    static final int ACCENT = 0xFFD9B3FF;

    private static final float[] anim = new float[7];
    private static float shownHp = -1;
    private static int shownId = -1;

    public static void render(MatrixStack ms) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || mc.options.hudHidden || mc.options.debugEnabled) return;

        Extras.Watermark wm = ModuleManager.get(Extras.Watermark.class);
        if (wm != null && wm.enabled) watermark(ms, mc);
        if (ModuleManager.on(Extras.FPSDisplay.class)) statsHud(ms, mc);
        if (ModuleManager.on(Extras.ArmorHUD.class)) armorHud(ms, mc);
        if (ModuleManager.on(Extras.Keystrokes.class)) keystrokes(ms, mc);
        if (ModuleManager.on(Extras.TargetHUD.class)) targetHud(ms, mc);
        Extras.Crosshair ch = ModuleManager.get(Extras.Crosshair.class);
        if (ch != null && ch.enabled) crosshair(ms, mc, ch);
        Extras.HitEffects he = ModuleManager.get(Extras.HitEffects.class);
        if (he != null && he.enabled && he.marker.on) hitmarker(ms, mc);
        if (ModuleManager.on(Modules.HUD.class)) arrayList(ms, mc);
        CoordinatesHudModule coordMod = ModuleManager.get(CoordinatesHudModule.class);
        if (coordMod != null && coordMod.enabled) coordMod.drawOverlay(ms, mc.getWindow().getScaledHeight());
        BiomeHudModule biomeMod = ModuleManager.get(BiomeHudModule.class);
        if (biomeMod != null && biomeMod.enabled) biomeMod.drawOverlay(ms, mc.getWindow().getScaledHeight());
        FpsGraphHudModule fpsGraph = ModuleManager.get(FpsGraphHudModule.class);
        if (fpsGraph != null && fpsGraph.enabled) fpsGraph.drawOverlay(ms);
        CompassHudModule compass = ModuleManager.get(CompassHudModule.class);
        if (compass != null && compass.enabled) compass.drawOverlay(ms);
        SessionTimerHudModule timer = ModuleManager.get(SessionTimerHudModule.class);
        if (timer != null && timer.enabled) timer.drawOverlay(ms, mc.getWindow().getScaledHeight());
        ua.client.notify.NotificationManager.render(ms);
    }

    private static void watermark(MatrixStack ms, MinecraftClient mc) {
        int ping = 0;
        PlayerListEntry e = mc.player.networkHandler.getPlayerListEntry(mc.player.getUuid());
        if (e != null) ping = e.getLatency();
        String title = "PASTA CLIENT";
        String sub = "1.16.5  |  " + ping + " ms";
        int bw = Math.max(mc.textRenderer.getWidth(title), mc.textRenderer.getWidth(sub)) + 14;
        DrawableHelper.fill(ms, 6, 6, 6 + bw, 31, 0xCC120A1E);
        DrawableHelper.fill(ms, 6, 6, 8, 31, PURPLE);
        DrawableHelper.fill(ms, 6, 6, 6 + bw, 7, PURPLE);
        mc.textRenderer.drawWithShadow(ms, title, 12, 9, 0xFFFFFFFF);
        mc.textRenderer.drawWithShadow(ms, sub, 12, 19, ACCENT);
    }

    private static void statsHud(MatrixStack ms, MinecraftClient mc) {
        int fps = mc.getCurrentFps();
        String fpsStr = "FPS: " + fps;
        String xyzStr = String.format(Locale.ROOT, "XYZ: %.0f, %.0f, %.0f",
                mc.player.getX(), mc.player.getY(), mc.player.getZ());
        int y = 36;
        DrawableHelper.fill(ms, 6, y, 6 + Math.max(mc.textRenderer.getWidth(fpsStr), mc.textRenderer.getWidth(xyzStr)) + 12, y + 22, 0xAA120A1E);
        DrawableHelper.fill(ms, 6, y, 8, y + 22, 0xFF7A2BE2);
        mc.textRenderer.drawWithShadow(ms, fpsStr, 12, y + 2, 0xFFC27BFF);
        mc.textRenderer.drawWithShadow(ms, xyzStr, 12, y + 12, ACCENT);
    }

    private static void armorHud(MatrixStack ms, MinecraftClient mc) {
        int x = mc.getWindow().getScaledWidth() / 2 + 100;
        int y = mc.getWindow().getScaledHeight() - 36;
        List<ItemStack> armor = new ArrayList<>();
        for (int i = 3; i >= 0; i--) {
            ItemStack st = mc.player.inventory.armor.get(i);
            if (!st.isEmpty()) armor.add(st);
        }
        if (armor.isEmpty()) return;
        for (ItemStack st : armor) {
            mc.getItemRenderer().renderGuiItemIcon(st, x, y);
            mc.getItemRenderer().renderGuiItemOverlay(mc.textRenderer, st, x, y);
            x += 18;
        }
    }

    private static void crosshair(MatrixStack ms, MinecraftClient mc, Extras.Crosshair ch) {
        int cx = mc.getWindow().getScaledWidth() / 2;
        int cy = mc.getWindow().getScaledHeight() / 2;
        int g = ch.gap.i();
        int l = ch.len.i();
        int col = 0xEEB85CFF;
        DrawableHelper.fill(ms, cx - g - l, cy, cx - g, cy + 1, col);
        DrawableHelper.fill(ms, cx + g + 1, cy, cx + g + 1 + l, cy + 1, col);
        DrawableHelper.fill(ms, cx, cy - g - l, cx + 1, cy - g, col);
        DrawableHelper.fill(ms, cx, cy + g + 1, cx + 1, cy + g + 1 + l, col);
        if (ch.dot.on) {
            DrawableHelper.fill(ms, cx, cy, cx + 1, cy + 1, 0xFFFFFFFF);
        }
    }

    private static void keystrokes(MatrixStack ms, MinecraftClient mc) {
        int x = 8, y = mc.getWindow().getScaledHeight() - 100;
        int k = 22, g = 2;
        key(ms, mc, 0, x + k + g, y, k, k, "W", mc.options.keyForward.isPressed());
        key(ms, mc, 1, x, y + k + g, k, k, "A", mc.options.keyLeft.isPressed());
        key(ms, mc, 2, x + k + g, y + k + g, k, k, "S", mc.options.keyBack.isPressed());
        key(ms, mc, 3, x + 2 * (k + g), y + k + g, k, k, "D", mc.options.keyRight.isPressed());
        int mw = (3 * k + 2 * g - g) / 2;
        key(ms, mc, 4, x, y + 2 * (k + g), mw, k, "LMB", mc.options.keyAttack.isPressed());
        key(ms, mc, 5, x + mw + g, y + 2 * (k + g), mw, k, "RMB", mc.options.keyUse.isPressed());
        key(ms, mc, 6, x, y + 3 * (k + g), 3 * k + 2 * g, 14, "SPACE", mc.options.keyJump.isPressed());
    }

    private static int lerp(int a, int b, float t) { return (int) (a + (b - a) * t); }

    private static void key(MatrixStack ms, MinecraftClient mc, int i, int x, int y, int w, int h, String label, boolean down) {
        anim[i] += ((down ? 1f : 0f) - anim[i]) * 0.35f;
        float t = anim[i];
        int col = (lerp(0x88, 0xE0, t) << 24) | (lerp(0x12, 0x9B, t) << 16) | (lerp(0x0C, 0x30, t) << 8) | lerp(0x1C, 0xFF, t);
        DrawableHelper.fill(ms, x, y, x + w, y + h, col);
        int bc = 0xAA6A2BD9;
        DrawableHelper.fill(ms, x, y, x + w, y + 1, bc);
        DrawableHelper.fill(ms, x, y + h - 1, x + w, y + h, bc);
        DrawableHelper.fill(ms, x, y, x + 1, y + h, bc);
        DrawableHelper.fill(ms, x + w - 1, y, x + w, y + h, bc);
        int tw = mc.textRenderer.getWidth(label);
        mc.textRenderer.drawWithShadow(ms, label, x + (w - tw) / 2f, y + (h - 8) / 2f, 0xFFFFFFFF);
    }

    private static void targetHud(MatrixStack ms, MinecraftClient mc) {
        LivingEntity t = Target.hud();
        if (t == null) { shownId = -1; return; }
        if (t.getEntityId() != shownId) { shownId = t.getEntityId(); shownHp = t.getHealth(); }
        shownHp += (t.getHealth() - shownHp) * 0.15f;
        float max = Math.max(1f, t.getMaxHealth());
        float ratio = MathHelper.clamp(shownHp / max, 0f, 1f);

        int x = mc.getWindow().getScaledWidth() / 2 + 30;
        int y = mc.getWindow().getScaledHeight() / 2 + 30;
        int pw = 150, ph = 46;
        DrawableHelper.fill(ms, x, y, x + pw, y + ph, 0xCC120A1E);
        DrawableHelper.fill(ms, x, y, x + pw, y + 1, PURPLE);
        DrawableHelper.fill(ms, x, y + ph - 1, x + pw, y + ph, PURPLE);
        DrawableHelper.fill(ms, x, y, x + 1, y + ph, PURPLE);
        DrawableHelper.fill(ms, x + pw - 1, y, x + pw, y + ph, PURPLE);

        if (t instanceof AbstractClientPlayerEntity) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.color4f(1f, 1f, 1f, 1f);
            mc.getTextureManager().bindTexture(((AbstractClientPlayerEntity) t).getSkinTexture());
            DrawableHelper.drawTexture(ms, x + 5, y + 6, 34, 34, 8f, 8f, 8, 8, 64, 64);
            DrawableHelper.drawTexture(ms, x + 5, y + 6, 34, 34, 40f, 8f, 8, 8, 64, 64);
        }

        int tx = x + 44;
        mc.textRenderer.drawWithShadow(ms, t.getName().getString(), tx, y + 7, 0xFFFFFFFF);
        int bw = pw - 50;
        DrawableHelper.fill(ms, tx, y + 21, tx + bw, y + 29, 0xFF2A2036);
        int r = (int) (255 * Math.min(1f, 2f * (1f - ratio)));
        int gr = (int) (255 * Math.min(1f, 2f * ratio));
        DrawableHelper.fill(ms, tx, y + 21, tx + (int) (bw * ratio), y + 29, 0xFF000000 | (r << 16) | (gr << 8) | 0x30);
        float dist = mc.player.distanceTo(t);
        String info = String.format(Locale.ROOT, "%.1f / %.0f HP  %.1fm", t.getHealth(), t.getMaxHealth(), dist);
        mc.textRenderer.drawWithShadow(ms, info, tx, y + 32, ACCENT);
    }

    private static void hitmarker(MatrixStack ms, MinecraftClient mc) {
        long dt = System.currentTimeMillis() - Target.hitTime;
        if (dt > 250) return;
        float a = 1f - dt / 250f;
        float cx = mc.getWindow().getScaledWidth() / 2f, cy = mc.getWindow().getScaledHeight() / 2f;
        Matrix4f m = ms.peek().getModel();
        RenderUtil.begin();
        for (int sx = -1; sx <= 1; sx += 2) {
            for (int sy = -1; sy <= 1; sy += 2) {
                RenderUtil.line(m, cx + sx * 3, cy + sy * 3, 0, cx + sx * 9, cy + sy * 9, 0, 0.85f, 0.45f, 1f, a);
            }
        }
        RenderUtil.end();
    }

    private static void arrayList(MatrixStack ms, MinecraftClient mc) {
        List<Module> on = new ArrayList<>();
        for (Module m : ModuleManager.modules) if (m.enabled && !(m instanceof Modules.HUD)) on.add(m);
        on.sort((a, b) -> mc.textRenderer.getWidth(b.name) - mc.textRenderer.getWidth(a.name));
        int w = mc.getWindow().getScaledWidth();
        int y = 4;
        for (Module m : on) {
            int tw = mc.textRenderer.getWidth(m.name);
            DrawableHelper.fill(ms, w - tw - 8, y - 1, w - 2, y + 9, 0x66120A1E);
            DrawableHelper.fill(ms, w - 2, y - 1, w, y + 9, PURPLE);
            mc.textRenderer.drawWithShadow(ms, m.name, w - tw - 5, y, 0xFFC27BFF);
            y += 10;
        }
    }
}
