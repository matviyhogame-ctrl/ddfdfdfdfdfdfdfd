package ua.client.module.misc;

import net.minecraft.client.network.ClientPlayerEntity;
import ua.client.module.Category;
import ua.client.module.Module;

public class AutoSprintResetModule extends Module {
    public AutoSprintResetModule() {
        super("AutoSprintReset", Category.MISC);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc().player;
        if (p != null && p.hurtTime == 1) {
            p.setSprinting(true);
        }
    }
}
