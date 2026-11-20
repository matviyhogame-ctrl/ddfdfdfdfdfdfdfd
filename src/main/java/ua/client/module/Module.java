package ua.client.module;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public abstract class Module {
    public final String name;
    public final Category category;
    public boolean enabled;
    public boolean expanded;
    public boolean down;
    public boolean mouseDown;
    public int key = -1;
    public int mouseKey = -1;
    public final List<Setting> settings = new ArrayList<>();

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    protected MinecraftClient mc() { return MinecraftClient.getInstance(); }

    protected Setting add(Setting s) { settings.add(s); return s; }

    public void toggle() { setEnabled(!enabled); }

    public void setEnabled(boolean e) {
        if (enabled == e) return;
        enabled = e;
        if (e) onEnable(); else onDisable();
    }

    public String bindLabel() {
        if (mouseKey >= 0) {
            if (mouseKey == 0) return "M1";
            if (mouseKey == 1) return "M2";
            if (mouseKey == 2) return "M3";
            return "M" + (mouseKey + 1);
        }
        if (key >= 0) {
            String n = GLFW.glfwGetKeyName(key, 0);
            if (n != null && !n.isEmpty()) return n.toUpperCase(java.util.Locale.ROOT);
            if (key == GLFW.GLFW_KEY_SPACE) return "SPACE";
            if (key == GLFW.GLFW_KEY_LEFT_SHIFT) return "LSHIFT";
            if (key == GLFW.GLFW_KEY_RIGHT_SHIFT) return "RSHIFT";
            if (key == GLFW.GLFW_KEY_LEFT_CONTROL) return "LCTRL";
            if (key == GLFW.GLFW_KEY_LEFT_ALT) return "LALT";
            if (key == GLFW.GLFW_KEY_TAB) return "TAB";
            return "K" + key;
        }
        return "";
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
    public void onRender(MatrixStack ms, Camera camera, float tickDelta) {}
}
