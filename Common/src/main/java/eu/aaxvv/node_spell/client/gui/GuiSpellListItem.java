package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.util.RenderUtil;

public class GuiSpellListItem extends GuiElement {
    private static final int ITEM_HEIGHT = 16;

    public GuiSpellListItem(String spellName) {
        super(0, ITEM_HEIGHT);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        RenderUtil.drawGuiElementDebugRect(pose, this, 0xFF00FF00);
    }
}
