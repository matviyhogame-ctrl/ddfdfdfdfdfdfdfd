package ua.client.module.player;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import ua.client.module.Category;
import ua.client.module.Module;

public class AutoToolModule extends Module {
    public AutoToolModule() {
        super("AutoTool", Category.PLAYER);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc().player;
        if (p == null || mc().world == null || !mc().options.keyAttack.isPressed()) return;
        if (mc().crosshairTarget == null || mc().crosshairTarget.getType() != HitResult.Type.BLOCK) return;
        BlockHitResult bhr = (BlockHitResult) mc().crosshairTarget;
        BlockState state = mc().world.getBlockState(bhr.getBlockPos());
        float bestSpeed = 1.0f;
        int bestSlot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack st = p.inventory.getStack(i);
            float spd = st.getMiningSpeedMultiplier(state);
            if (spd > bestSpeed) {
                bestSpeed = spd;
                bestSlot = i;
            }
        }
        if (bestSlot != -1) p.inventory.selectedSlot = bestSlot;
    }
}
