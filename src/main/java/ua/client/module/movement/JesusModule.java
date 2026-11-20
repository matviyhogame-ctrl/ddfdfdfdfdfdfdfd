package ua.client.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import ua.client.module.Category;
import ua.client.module.Module;

public class JesusModule extends Module {
    public JesusModule() {
        super("Jesus", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc().player;
        if (p == null) return;
        if (p.isTouchingWater() || p.isInLava()) {
            Vec3d v = p.getVelocity();
            p.setVelocity(v.x, Math.max(0.11, v.y), v.z);
        }
    }
}
