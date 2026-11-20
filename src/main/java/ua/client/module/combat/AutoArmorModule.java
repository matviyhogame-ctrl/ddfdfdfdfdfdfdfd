package ua.client.module.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import ua.client.module.Category;
import ua.client.module.Module;

public class AutoArmorModule extends Module {
    public AutoArmorModule() {
        super("AutoArmor", Category.COMBAT);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc().player;
        if (p == null || mc().interactionManager == null || p.currentScreenHandler != p.playerScreenHandler) return;
        EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        for (int s = 0; s < slots.length; s++) {
            EquipmentSlot eq = slots[s];
            int armorIdx = 3 - s;
            ItemStack current = p.inventory.armor.get(armorIdx);
            int currentProt = protectionScore(current, eq);
            int bestSlot = -1;
            int bestScore = currentProt;

            for (int i = 0; i < 36; i++) {
                ItemStack candidate = p.inventory.main.get(i);
                int score = protectionScore(candidate, eq);
                if (score > bestScore) {
                    bestScore = score;
                    bestSlot = i;
                }
            }
            if (bestSlot != -1) {
                int handlerSlot = bestSlot < 9 ? 36 + bestSlot : bestSlot;
                int targetHandlerSlot = 5 + s;
                if (!current.isEmpty()) {
                    mc().interactionManager.clickSlot(p.playerScreenHandler.syncId, targetHandlerSlot, 0, SlotActionType.QUICK_MOVE, p);
                }
                mc().interactionManager.clickSlot(p.playerScreenHandler.syncId, handlerSlot, 0, SlotActionType.QUICK_MOVE, p);
                return;
            }
        }
    }

    private int protectionScore(ItemStack stack, EquipmentSlot slot) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ArmorItem)) return -1;
        ArmorItem armor = (ArmorItem) stack.getItem();
        if (armor.getSlotType() != slot) return -1;
        return armor.getProtection() * 10 + (int) armor.getToughness();
    }
}
