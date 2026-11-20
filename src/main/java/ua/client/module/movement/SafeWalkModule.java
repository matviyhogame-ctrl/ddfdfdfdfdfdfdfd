package ua.client.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ua.client.module.Category;
import ua.client.module.Module;
import ua.client.util.WorldUtil;

public class SafeWalkModule extends Module {
    public SafeWalkModule() {
        super("SafeWalk", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc().player;
        if (p == null || !p.isOnGround() || p.isSneaking()) return;
        Vec3d v = p.getVelocity();
        BlockPos nextPos = new BlockPos(p.getX() + v.x * 2.0, p.getY() - 0.5, p.getZ() + v.z * 2.0);
        if (!WorldUtil.isSolid(nextPos)) {
            p.setVelocity(0, v.y, 0);
        }
    }
}
