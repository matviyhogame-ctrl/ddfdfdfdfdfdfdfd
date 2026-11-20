package ua.client.module.render;

import net.minecraft.client.options.Perspective;
import ua.client.module.Category;
import ua.client.module.Module;

public class FreelookModule extends Module {
    private Perspective prev;

    public FreelookModule() {
        super("Freelook", Category.RENDER);
    }

    @Override
    public void onEnable() {
        if (mc().options != null) {
            prev = mc().options.getPerspective();
            mc().options.setPerspective(Perspective.THIRD_PERSON_BACK);
        }
    }

    @Override
    public void onDisable() {
        if (mc().options != null && prev != null) {
            mc().options.setPerspective(prev);
            prev = null;
        }
    }
}
