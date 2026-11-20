package ua.client.module.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import ua.client.module.Category;
import ua.client.module.Module;
import ua.client.module.Setting;

public class AutoEatModule extends Module {
    private final Setting minHunger = add(Setting.num("MinHunger", 14, 2, 20, 1));
    private int prevSlot = -1;

    public AutoEatModule() {
        super("AutoEat", Category.PLAYER);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc().player;
        if (p == null || mc().interactionManager == null) return;
        if (p.getHungerManager().getFoodLevel() <= minHunger.i()) {
            for (int i = 0; i < 9; i++) {
                ItemStack s = p.inventory.getStack(i);
                if (s.isFood()) {
                    if (prevSlot == -1) prevSlot = p.inventory.selectedSlot;
                    p.inventory.selectedSlot = i;
                    mc().options.keyUse.setPressed(true);
                    return;
                }
            }
        } else if (prevSlot != -1) {
            mc().options.keyUse.setPressed(false);
            p.inventory.selectedSlot = prevSlot;
            prevSlot = -1;
        }
    }
}
