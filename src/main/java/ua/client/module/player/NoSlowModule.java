package ua.client.module.player;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import ua.client.module.Category;
import ua.client.module.Module;

public class NoSlowModule extends Module {
    public NoSlowModule() {
        super("NoSlow", Category.PLAYER);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc().player;
        if (p == null || !p.isUsingItem()) return;
        Vec3d v = p.getVelocity();
        if (Math.abs(v.x) < 0.18 && Math.abs(v.z) < 0.18 && (p.input.movementForward != 0 || p.input.movementSideways != 0)) {
            p.setVelocity(v.x * 1.35, v.y, v.z * 1.35);
        }
    }
}
