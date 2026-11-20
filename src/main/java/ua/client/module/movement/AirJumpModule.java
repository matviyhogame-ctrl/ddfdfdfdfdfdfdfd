package ua.client.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import ua.client.module.Category;
import ua.client.module.Module;

public class AirJumpModule extends Module {
    private boolean prevJump;

    public AirJumpModule() {
        super("AirJump", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc().player;
        if (p == null) return;
        boolean jumpPressed = mc().options.keyJump.isPressed();
        if (jumpPressed && !prevJump && !p.isOnGround()) {
            p.jump();
        }
        prevJump = jumpPressed;
    }
}
