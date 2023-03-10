package eu.aaxvv.node_spell.client.gui.node_widget;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;

public class BoolFieldWidget extends Widget<Boolean> {
    private static final int OFF_COLOR = ModConstants.Colors.DARK_GREY;
    private static final int ON_COLOR = ModConstants.Colors.LIGHT_GREY;
    private static final int SWITCH_COLOR = ModConstants.Colors.WHITE;
    private static final int SWITCH_WIDTH = 12;

    public BoolFieldWidget(NodeInstance parent, int width) {
        super(parent, width, 10);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        GuiComponent.fill(pose, this.getGlobalX(), this.getGlobalY(), this.getGlobalX() + this.getWidth(), this.getGlobalY() + this.getHeight(), 0xFF000000);
        GuiComponent.fill(pose, this.getGlobalX() + 1, this.getGlobalY() + 1, this.getGlobalX() + this.getWidth() - 1, this.getGlobalY() + this.getHeight() - 1, this.currentValue ? ON_COLOR : OFF_COLOR);

        int switchX = this.currentValue ? this.getGlobalX() + this.getWidth() - SWITCH_WIDTH - 2 :  this.getGlobalX() + 2;
        GuiComponent.fill(pose, switchX, this.getGlobalY() + 2, switchX + SWITCH_WIDTH, this.getGlobalY() + this.getHeight() - 2, SWITCH_COLOR);

        Minecraft.getInstance().font.draw(pose, this.currentValue ? "True" : "False", this.getGlobalX() + this.getWidth() + 2, this.getGlobalY() + 1, 0xFF000000);
    }

    @Override
    public boolean onMouseDown(double screenX, double screenY, int button) {
        this.currentValue = !this.currentValue;
        commitValue();
        return true;
    }
}
