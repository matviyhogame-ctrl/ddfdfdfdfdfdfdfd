package ua.client.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;
import ua.client.module.Category;
import ua.client.module.Module;
import ua.client.module.ModuleManager;
import ua.client.module.Setting;

public class ClickGui extends Screen {
    private static class Hit {
        int x1, y1, x2, y2;
        Category cat;
        Module mod;
        Setting set;
        boolean bindSlot;
    }

    private static Category selectedCategory = Category.COMBAT;
    private final List<Hit> hits = new ArrayList<>();
    private Module bindingMod;
    private int scroll;

    public ClickGui() { super(new LiteralText("PastaClient")); }

    @Override public boolean isPauseScreen() { return false; }

    private void addHit(int x1, int y1, int x2, int y2, Category c, Module m, Setting s, boolean bindSlot) {
        Hit h = new Hit();
        h.x1 = x1; h.y1 = y1; h.x2 = x2; h.y2 = y2;
        h.cat = c; h.mod = m; h.set = s; h.bindSlot = bindSlot;
        hits.add(h);
    }

    @Override
    public void render(MatrixStack ms, int mx, int my, float delta) {
        renderBackground(ms);
        hits.clear();

        int panelX = 24, panelY = 20;
        int panelW = Math.min(width - 48, 520);
        int panelH = Math.min(height - 40, 320);
        int sideW = 115;

        // Outer frame
        fill(ms, panelX, panelY, panelX + panelW, panelY + panelH, 0xE611091D);
        fill(ms, panelX, panelY, panelX + panelW, panelY + 2, 0xFF9B30FF);
        fill(ms, panelX, panelY + panelH - 1, panelX + panelW, panelY + panelH, 0xFF6A2BD9);
        fill(ms, panelX, panelY, panelX + 1, panelY + panelH, 0xFF6A2BD9);
        fill(ms, panelX + panelW - 1, panelY, panelX + panelW, panelY + panelH, 0xFF6A2BD9);

        // Left sidebar
        fill(ms, panelX + 1, panelY + 2, panelX + sideW, panelY + panelH - 1, 0xF0180D29);
        fill(ms, panelX + sideW, panelY + 2, panelX + sideW + 1, panelY + panelH - 1, 0xFF3E1866);
        drawStringWithShadow(ms, textRenderer, "PASTA CLIENT", panelX + 10, panelY + 10, 0xFFB85CFF);
        drawStringWithShadow(ms, textRenderer, "Cheat Menu v1.1", panelX + 10, panelY + 20, 0xFF9A7BB8);

        int cy = panelY + 38;
        for (Category c : Category.values()) {
            boolean active = c == selectedCategory;
            boolean hover = mx >= panelX + 6 && mx < panelX + sideW - 6 && my >= cy && my < cy + 20;
            int bg = active ? 0xFF6A2BD9 : (hover ? 0x99351859 : 0x44221138);
            fill(ms, panelX + 6, cy, panelX + sideW - 6, cy + 20, bg);
            if (active) fill(ms, panelX + 6, cy, panelX + 9, cy + 20, 0xFFE0A8FF);
            drawStringWithShadow(ms, textRenderer, c.label, panelX + 14, cy + 6, active ? 0xFFFFFFFF : 0xFFD0B8EB);
            addHit(panelX + 6, cy, panelX + sideW - 6, cy + 20, c, null, null, false);
            cy += 24;
        }

        // Right module area
        int listX = panelX + sideW + 10;
        int listW = panelW - sideW - 20;
        int listTop = panelY + 10;
        int listBot = panelY + panelH - 22;

        drawStringWithShadow(ms, textRenderer, selectedCategory.label.toUpperCase(java.util.Locale.ROOT),
                listX, listTop, 0xFFE0A8FF);

        int myY = listTop + 16 - scroll;
        for (Module m : ModuleManager.in(selectedCategory)) {
            int rowH = 20;
            if (myY + rowH >= listTop + 12 && myY <= listBot) {
                boolean hover = mx >= listX && mx < listX + listW && my >= myY && my < myY + rowH;
                int rowBg = m.enabled ? (hover ? 0xCC4B1D7A : 0xAA341457) : (hover ? 0xAA241636 : 0x88190E26);
                fill(ms, listX, myY, listX + listW, myY + rowH, rowBg);
                if (m.enabled) fill(ms, listX, myY, listX + 3, myY + rowH, 0xFFB85CFF);

                drawStringWithShadow(ms, textRenderer, m.name, listX + 8, myY + 6,
                        m.enabled ? 0xFFFFFFFF : 0xFFB8A6CC);

                // Bind badge on right
                String bindText = (bindingMod == m) ? "[...]" : (m.bindLabel().isEmpty() ? "[BIND]" : "[" + m.bindLabel() + "]");
                int bw = textRenderer.getWidth(bindText) + 8;
                int bx1 = listX + listW - bw - 4;
                int bx2 = listX + listW - 4;
                fill(ms, bx1, myY + 4, bx2, myY + 16, bindingMod == m ? 0xFF9B30FF : 0x992A1742);
                drawStringWithShadow(ms, textRenderer, bindText, bx1 + 4, myY + 6, 0xFFE8C8FF);
                addHit(bx1, myY + 3, bx2, myY + 17, null, m, null, true);

                // Main toggle/settings hit area
                addHit(listX, myY, bx1 - 2, myY + rowH, null, m, null, false);
            }
            myY += rowH + 3;

            if (m.expanded) {
                for (Setting s : m.settings) {
                    int sh = 15;
                    if (myY + sh >= listTop + 12 && myY <= listBot) {
                        fill(ms, listX + 10, myY, listX + listW - 4, myY + sh, 0xAA140B21);
                        drawStringWithShadow(ms, textRenderer, s.name, listX + 16, myY + 4, 0xFFC5B0DC);
                        String val = s.display();
                        int vw = textRenderer.getWidth(val);
                        drawStringWithShadow(ms, textRenderer, val, listX + listW - vw - 10, myY + 4, 0xFFD980FF);
                        addHit(listX + 10, myY, listX + listW - 4, myY + sh, null, m, s, false);
                    }
                    myY += sh + 2;
                }
            }
        }

        // Bottom hint bar
        fill(ms, panelX + sideW + 1, panelY + panelH - 17, panelX + panelW - 1, panelY + panelH - 1, 0xCC160B24);
        drawStringWithShadow(ms, textRenderer,
                "LMB: Toggle | RMB: Settings | Click [BIND] + Key or MouseBtn (ESC: clear)",
                listX, panelY + panelH - 12, 0xFFB899D6);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (bindingMod != null) {
            // Allow binding mouse buttons M1..M8 (or ESC in keyPressed to clear)
            bindingMod.mouseKey = button;
            bindingMod.key = -1;
            bindingMod = null;
            return true;
        }
        for (Hit h : hits) {
            if (mx < h.x1 || mx >= h.x2 || my < h.y1 || my >= h.y2) continue;
            if (h.cat != null && button == 0) {
                selectedCategory = h.cat;
                scroll = 0;
                return true;
            }
            if (h.bindSlot && h.mod != null) {
                bindingMod = h.mod;
                return true;
            }
            if (h.set != null) {
                h.set.change(button != 1);
                return true;
            }
            if (h.mod != null) {
                if (button == 0) h.mod.toggle();
                else if (button == 1) h.mod.expanded = !h.mod.expanded;
                else if (button == 2) bindingMod = h.mod;
                return true;
            }
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double amount) {
        scroll = Math.max(0, scroll - (int) (amount * 14));
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (bindingMod != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                bindingMod.key = -1;
                bindingMod.mouseKey = -1;
            } else {
                bindingMod.key = keyCode;
                bindingMod.mouseKey = -1;
            }
            bindingMod = null;
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
