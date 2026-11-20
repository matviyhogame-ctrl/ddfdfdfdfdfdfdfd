package ua.client.module.render;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import ua.client.config.ClientConfig;
import ua.client.module.Category;
import ua.client.module.Module;

public class BiomeHudModule extends Module {
    public BiomeHudModule() {
        super("BiomeHUD", Category.RENDER);
    }

    public void drawOverlay(MatrixStack ms, int screenHeight) {
        ClientPlayerEntity p = mc().player;
        if (p == null || mc().world == null) return;
        BlockPos pos = p.getBlockPos();
        Identifier id = mc().world.getRegistryManager().get(net.minecraft.util.registry.Registry.BIOME_KEY).getId(mc().world.getBiome(pos));
        String biome = id != null ? id.getPath() : "unknown";
        String text = "Biome: " + biome;
        int w = mc().textRenderer.getWidth(text) + 12;
        int y = screenHeight - 138;
        DrawableHelper.fill(ms, 8, y, 8 + w, y + 14, 0xB8120A1E);
        DrawableHelper.fill(ms, 8, y, 10, y + 14, ClientConfig.secondaryColor);
        mc().textRenderer.drawWithShadow(ms, text, 13, y + 3, 0xFFFFFFFF);
    }
}
