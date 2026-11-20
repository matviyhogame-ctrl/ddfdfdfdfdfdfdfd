package ua.client.module.misc;

import net.minecraft.client.network.ClientPlayerEntity;
import ua.client.module.Category;
import ua.client.module.Module;

public class AntiAFKModule extends Module {
    private int ticks;

    public AntiAFKModule() {
        super("AntiAFK", Category.MISC);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc().player;
        if (p == null) return;
        ticks++;
        if (ticks % 200 == 0) {
            p.yaw += 15.0f;
            if (p.isOnGround()) p.jump();
        }
    }
}
