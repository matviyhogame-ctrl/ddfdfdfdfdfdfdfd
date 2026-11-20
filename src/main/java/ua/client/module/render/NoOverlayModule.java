package ua.client.module.render;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import ua.client.module.Category;
import ua.client.module.Module;

public class NoOverlayModule extends Module {
    public NoOverlayModule() {
        super("NoOverlay", Category.RENDER);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc().player;
        if (p == null) return;
        p.removeStatusEffect(StatusEffects.NAUSEA);
        p.removeStatusEffect(StatusEffects.BLINDNESS);
    }
}
