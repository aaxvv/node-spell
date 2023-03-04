package eu.aaxvv.node_spell.client.gui.graph_editor;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.util.RenderUtil;

public class GuiNodePicker extends GuiElement {
    public GuiNodePicker(int width, int height) {
        super(width, height);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        RenderUtil.drawGuiElementDebugRect(pose, this, 0xFFFF00FF);
        super.render(pose, mouseX, mouseY, tickDelta);
    }
}
