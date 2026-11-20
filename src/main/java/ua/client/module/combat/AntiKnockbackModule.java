package ua.client.module.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import ua.client.module.Category;
import ua.client.module.Module;
import ua.client.module.Setting;

public class AntiKnockbackModule extends Module {
    private final Setting horizontal = add(Setting.num("Horizontal %", 0, 0, 100, 5));
    private final Setting vertical = add(Setting.num("Vertical %", 0, 0, 100, 5));

    public AntiKnockbackModule() {
        super("AntiKnockback", Category.COMBAT);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc().player;
        if (p == null || p.hurtTime <= 0) return;
        double hFactor = horizontal.value / 100.0;
        double vFactor = vertical.value / 100.0;
        Vec3d v = p.getVelocity();
        p.setVelocity(v.x * hFactor, v.y * vFactor, v.z * hFactor);
    }
}
