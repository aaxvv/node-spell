package eu.aaxvv.node_spell.client.gui.graph_editor;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.gui.elements.GuiScrollContainer;
import eu.aaxvv.node_spell.client.gui.elements.GuiTextButton;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.function.Consumer;

public class GuiNodeListPopup extends GuiElement {
    private static final int MAX_HEIGHT = 100;
    private static final int DEFAULT_WIDTH = 48;
    private static final int DEFAULT_HEIGHT = 11 + 2 + 4;

    private final GuiScrollContainer nodeList;
    private Consumer<Node> nodeClickedCallback;

    public GuiNodeListPopup(List<Node> nodes) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.nodeList = new GuiScrollContainer(width - 4, MAX_HEIGHT - 4);
        this.nodeList.setLocalPosition(1, 1);
        this.nodeList.setHeight(DEFAULT_HEIGHT - 2);
        this.nodeList.setWidth(DEFAULT_WIDTH - 2);
        this.addChild(this.nodeList);

        createInitialElements(nodes);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        RenderUtil.drawRect(pose, this.getGlobalX(), this.getGlobalY(), this.getWidth(), this.getHeight(), ModConstants.Colors.PAPER_BORDER);
        RenderUtil.drawRect(pose, this.getGlobalX() + 1, this.getGlobalY() + 1, this.getWidth() - 2, this.getHeight() - 2, ModConstants.Colors.PAPER_BG);

        super.render(pose, mouseX, mouseY, tickDelta);
    }

    private void createInitialElements(List<Node> nodes) {
        if (nodes.isEmpty()) {
            return;
        }

        int maxNameWidth = DEFAULT_WIDTH;

        for (Node node : nodes) {
            int nameWidth = Minecraft.getInstance().font.width(node.getDisplayName());
            maxNameWidth = Math.max(maxNameWidth, nameWidth);
            GuiTextButton button = new GuiTextButton(0, 11, node.getDisplayName());
            button.setClickCallback(() -> this.nodeClicked(node));
            this.nodeList.addChild(button);
        }

        int requiredWidth = maxNameWidth + 4 + (this.nodeList.getInnerPadding()*2) + this.nodeList.getScrollBarWidth();
        this.nodeList.setWidth(requiredWidth);
        this.setWidth(requiredWidth + 2);

        int requiredHeight = this.nodeList.getRequiredHeight();
        int height = Mth.clamp(requiredHeight + 2, DEFAULT_HEIGHT, MAX_HEIGHT);
        this.setHeight(height);
        this.nodeList.setHeight(height - 2);
    }

    private void nodeClicked(Node node) {
        if (this.nodeClickedCallback != null) {
            this.nodeClickedCallback.accept(node);
        }
        this.getContext().getPopupPane().closePopup(this);
    }

    public void setNodeClickedCallback(Consumer<Node> nodeClickedCallback) {
        this.nodeClickedCallback = nodeClickedCallback;
    }
}
