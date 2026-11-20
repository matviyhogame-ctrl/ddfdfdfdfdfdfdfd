package ua.client.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ua.client.module.Extras;
import ua.client.module.ModuleManager;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void pasta$zoom(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        Extras.FullZoom z = ModuleManager.get(Extras.FullZoom.class);
        if (z != null && z.enabled) {
            cir.setReturnValue(cir.getReturnValue() / Math.max(1.0, z.zoom.value));
        }
    }
}
