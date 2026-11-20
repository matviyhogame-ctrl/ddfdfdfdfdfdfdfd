package ua.client.util;

public class ColorUtil {
    public static int argb(int a, int r, int g, int b) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static int rainbow(long offsetMs, float speed, float saturation, float brightness) {
        float hue = ((System.currentTimeMillis() + offsetMs) * speed * 0.001f) % 1.0f;
        return java.awt.Color.HSBtoRGB(hue, saturation, brightness) | 0xFF000000;
    }

    public static int pulse(int baseColor, float speed) {
        double wave = 0.5 + 0.5 * Math.sin(System.currentTimeMillis() * 0.004 * speed);
        int a = (baseColor >> 24) & 0xFF;
        int r = (int) (((baseColor >> 16) & 0xFF) * (0.75 + 0.25 * wave));
        int g = (int) (((baseColor >> 8) & 0xFF) * (0.75 + 0.25 * wave));
        int b = (int) ((baseColor & 0xFF) * (0.75 + 0.25 * wave));
        return argb(a, Math.min(255, r), Math.min(255, g), Math.min(255, b));
    }
}
