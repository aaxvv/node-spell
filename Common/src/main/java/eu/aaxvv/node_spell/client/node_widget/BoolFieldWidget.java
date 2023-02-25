package eu.aaxvv.node_spell.client.node_widget;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import net.minecraft.client.gui.GuiComponent;

public class BoolFieldWidget extends Widget<Boolean> {
    private static final int OFF_COLOR = 0xFF404040;
    private static final int ON_COLOR = 0xFF7F7F7F;
    private static final int SWITCH_COLOR = 0xFFC6C6C6;
    private static final int SWITCH_WIDTH = 24;

    public BoolFieldWidget(NodeInstance parent, int width) {
        super(parent, width, 13);
    }

    @Override
    public void draw(PoseStack pose, int x, int y) {
        GuiComponent.fill(pose, x, y, x + this.width, y + this.height, 0xFF000000);
        GuiComponent.fill(pose, x + 1, y + 1, x + this.width - 1, y + this.height - 1, this.currentValue ? ON_COLOR : OFF_COLOR);

        int switchX = this.currentValue ? x + this.width - SWITCH_WIDTH - 2 :  x + 2;
        GuiComponent.fill(pose, switchX, y + 2, switchX + SWITCH_WIDTH, y + this.height - 2, SWITCH_COLOR);
    }

    @Override
    public void receiveMouseInput(int mouseX, int mouseY, int button) {
        this.currentValue = !this.currentValue;
        commitValue();
    }
}
