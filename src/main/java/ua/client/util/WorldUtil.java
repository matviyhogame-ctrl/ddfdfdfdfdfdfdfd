package ua.client.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class WorldUtil {
    public static boolean isSolid(BlockPos pos) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null) return false;
        BlockState state = mc.world.getBlockState(pos);
        return !state.isAir() && state.getMaterial().isSolid();
    }

    public static boolean isSafeSpot(BlockPos feetPos) {
        return !isSolid(feetPos) && !isSolid(feetPos.up()) && isSolid(feetPos.down());
    }
}
