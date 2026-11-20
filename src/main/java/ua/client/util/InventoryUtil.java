package ua.client.util;

import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

public class InventoryUtil {
    public static int findHotbarSlot(ClientPlayerEntity player, Predicate<ItemStack> predicate) {
        if (player == null) return -1;
        for (int i = 0; i < 9; i++) {
            if (predicate.test(player.inventory.getStack(i))) return i;
        }
        return -1;
    }

    public static int countItem(ClientPlayerEntity player, Predicate<ItemStack> predicate) {
        if (player == null) return 0;
        int total = 0;
        for (int i = 0; i < player.inventory.size(); i++) {
            ItemStack stack = player.inventory.getStack(i);
            if (!stack.isEmpty() && predicate.test(stack)) total += stack.getCount();
        }
        return total;
    }
}
