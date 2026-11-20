package ua.client.notify;

public class Notification {
    public final String title;
    public final String message;
    public final long createdAt;
    public final long durationMs;

    public Notification(String title, String message, long durationMs) {
        this.title = title;
        this.message = message;
        this.createdAt = System.currentTimeMillis();
        this.durationMs = durationMs;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - createdAt > durationMs;
    }

    public float progress() {
        float p = (float) (System.currentTimeMillis() - createdAt) / (float) Math.max(1L, durationMs);
        return Math.max(0f, Math.min(1f, p));
    }
}
