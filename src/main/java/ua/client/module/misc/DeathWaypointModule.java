package ua.client.module.misc;

import java.util.Locale;
import net.minecraft.client.network.ClientPlayerEntity;
import ua.client.module.Category;
import ua.client.module.Module;
import ua.client.notify.NotificationManager;

public class DeathWaypointModule extends Module {
    private boolean recorded;
    private String lastDeathPos = "None";

    public DeathWaypointModule() {
        super("DeathWaypoint", Category.MISC);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc().player;
        if (p == null) return;
        if (!p.isAlive() && !recorded) {
            recorded = true;
            lastDeathPos = String.format(Locale.ROOT, "%.0f, %.0f, %.0f", p.getX(), p.getY(), p.getZ());
            NotificationManager.push("DeathPoint", lastDeathPos);
        } else if (p.isAlive()) {
            recorded = false;
        }
    }

    public String getLastDeathPos() {
        return lastDeathPos;
    }
}
