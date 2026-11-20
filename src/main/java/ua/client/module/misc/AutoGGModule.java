package ua.client.module.misc;

import net.minecraft.client.network.ClientPlayerEntity;
import ua.client.module.Category;
import ua.client.module.Module;

public class AutoGGModule extends Module {
    private boolean sentForDeath;

    public AutoGGModule() {
        super("AutoGG", Category.MISC);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc().player;
        if (p == null) return;
        if (!p.isAlive() && !sentForDeath) {
            sentForDeath = true;
            p.sendChatMessage("gg");
        } else if (p.isAlive()) {
            sentForDeath = false;
        }
    }
}
