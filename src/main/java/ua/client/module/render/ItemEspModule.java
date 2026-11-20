package ua.client.module.render;

import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import ua.client.RenderUtil;
import ua.client.module.Category;
import ua.client.module.Module;

public class ItemEspModule extends Module {
    public ItemEspModule() {
        super("ItemESP", Category.RENDER);
    }

    @Override
    public void onRender(MatrixStack ms, Camera cam, float td) {
        if (mc().world == null) return;
        Vec3d c = cam.getPos();
        Matrix4f m = ms.peek().getModel();
        RenderUtil.begin();
        for (Entity e : mc().world.getEntities()) {
            if (!(e instanceof ItemEntity)) continue;
            double dx = MathHelper.lerp(td, e.lastRenderX, e.getX()) - e.getX();
            double dy = MathHelper.lerp(td, e.lastRenderY, e.getY()) - e.getY();
            double dz = MathHelper.lerp(td, e.lastRenderZ, e.getZ()) - e.getZ();
            Box b = e.getBoundingBox().expand(0.06).offset(dx - c.x, dy - c.y, dz - c.z);
            RenderUtil.box(m, b, 0.85f, 0.45f, 1.0f, 0.9f);
        }
        RenderUtil.end();
    }
}
