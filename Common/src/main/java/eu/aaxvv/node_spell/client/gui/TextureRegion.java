package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public record TextureRegion(ResourceLocation texture, int x, int y, int w, int h, int texW, int texH) {
    public TextureRegion(ResourceLocation texture, int x, int y, int w, int h) {
        this(texture, x, y, w, h, 256, 256);
    }

    public void blit(PoseStack pose, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.texture);
        GuiComponent.blit(pose, x, y, this.x, this.y, this.w, this.h, this.texW, this.texH);
        RenderSystem.disableBlend();
    }

    public void blitOffset(PoseStack pose, int x, int y, int xOffset, int yOffset) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.texture);
        GuiComponent.blit(pose, x, y, this.x + xOffset, this.y + yOffset, this.w, this.h, this.texW, this.texH);
        RenderSystem.disableBlend();
    }
}
