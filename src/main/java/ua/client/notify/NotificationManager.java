package ua.client.notify;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import ua.client.config.ClientConfig;

public class NotificationManager {
    private static final List<Notification> list = new ArrayList<>();

    public static void push(String title, String message) {
        if (!ClientConfig.showNotifications) return;
        list.add(new Notification(title, message, 2400L));
        if (list.size() > 5) list.remove(0);
    }

    public static void render(MatrixStack ms) {
        if (!ClientConfig.showNotifications || list.isEmpty()) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        Iterator<Notification> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().isExpired()) it.remove();
        }

        int w = mc.getWindow().getScaledWidth();
        int h = mc.getWindow().getScaledHeight();
        int y = h - 34;

        for (int i = list.size() - 1; i >= 0; i--) {
            Notification n = list.get(i);
            String line = n.title + ": " + n.message;
            int boxW = Math.min(260, mc.textRenderer.getWidth(line) + 14);
            int x1 = w - boxW - 8;
            int x2 = w - 8;
            int y1 = y - 16;
            int y2 = y;

            DrawableHelper.fill(ms, x1, y1, x2, y2, 0xCC140924);
            DrawableHelper.fill(ms, x1, y1, x1 + 2, y2, ClientConfig.accentColor);
            float rem = 1f - n.progress();
            DrawableHelper.fill(ms, x1, y2 - 1, x1 + (int) (boxW * rem), y2, ClientConfig.secondaryColor);
            mc.textRenderer.drawWithShadow(ms, line, x1 + 6, y1 + 4, 0xFFFFFFFF);
            y -= 20;
        }
    }
}
