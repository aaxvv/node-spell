package eu.aaxvv.node_spell.client.gui.graph_editor;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.gui.base.GuiFlowContainer;
import eu.aaxvv.node_spell.client.gui.base.GuiTextButton;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class GuiNodePicker extends GuiElement {
    private static final int CATEGORY_ITEM_HEIGHT = 11;
    private static final int CATEGORY_BACKGROUND_COLOR = ModConstants.Colors.PAPER_GRID;
    private static final int PICKER_BACKGROUND_COLOR = ModConstants.Colors.PAPER_BG;
    private static final int SEPARATOR_COLOR = ModConstants.Colors.PAPER_BORDER;

    private final GuiFlowContainer flowContainer;
    private final NodeCategoryLookup nodeCategoryLookup;

    public GuiNodePicker(int width, int height) {
        super(width, height);
        this.nodeCategoryLookup = new NodeCategoryLookup();
        this.flowContainer = new GuiFlowContainer(this.getWidth(), this.nodeCategoryLookup.getMaxCategoryWidth(), CATEGORY_ITEM_HEIGHT);
        createButtons();
        addChild(this.flowContainer);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        RenderUtil.drawRect(pose, this.getGlobalX(), this.getGlobalY(), this.getWidth(), this.getHeight(), PICKER_BACKGROUND_COLOR);
        RenderUtil.drawRect(pose, this.getGlobalX(), this.getGlobalY(), this.getWidth(), 1, SEPARATOR_COLOR);

        super.render(pose, mouseX, mouseY, tickDelta);
    }

    private void createButtons() {
        List<GuiTextButton> buttons = new ArrayList<>();
        for (NodeCategory category : this.nodeCategoryLookup.getCategoriesOrdered()) {
            GuiTextButton button = new GuiTextButton(0, CATEGORY_ITEM_HEIGHT, Component.translatable(category.translationKey));
            button.setBackgroundColor(CATEGORY_BACKGROUND_COLOR);
            button.setClickCallback(() -> this.openCategoryPopup(button, category));
            buttons.add(button);
        }

        this.flowContainer.addAll(buttons);
    }

    private void openCategoryPopup(GuiElement source, NodeCategory category) {
        ModConstants.LOG.info("Open category '{}'", category.translationKey);
    }

    @Override
    public void invalidate() {
        this.flowContainer.setLocalPosition(0, 1);
        this.flowContainer.setWidth(this.getWidth());
        this.setHeight(this.flowContainer.getHeight() + 1);
        super.invalidate();
    }
}
