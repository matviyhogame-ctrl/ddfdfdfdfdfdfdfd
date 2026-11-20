package ua.client.util;

public class MathUtil {
    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public static double roundToStep(double value, double step) {
        if (step <= 0) return value;
        return Math.round(value / step) * step;
    }
}
