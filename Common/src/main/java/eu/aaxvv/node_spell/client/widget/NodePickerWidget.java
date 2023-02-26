package eu.aaxvv.node_spell.client.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.screen.SpellBookScreen;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.Nodes;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.*;

/**
 * This class and the entire graph editor GUI code is honestly a garbage fire.
 */
public class NodePickerWidget implements Renderable, GuiEventListener, NarratableEntry {
    private static Map<NodeCategory, List<Node>> nodesByCategory;
    private static Map<NodeCategory, Integer> maxNodeNameWidth;
    private static List<NodeCategory> categoriesOrdered;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private int expandedCategory;
    private final SpellBookScreen parent;

    /**
     * Because we want this to be dynamically sized, but bottom aligned, the y coordinate here is actually the bottom edge, not the top
     * @param x left edge
     * @param y bottom edge
     * @param width width
     */
    public NodePickerWidget(SpellBookScreen parent, int x, int y, int width) {
        this.parent = parent;
        this.x = x;
        this.width = width;
        this.expandedCategory = -1;

        initializeStatic();

        // dynamic height based on number of categories
        int rows = (int)Math.ceil(categoriesOrdered.size() / (float) NodeConstants.NODE_PICKER_COLUMNS);
        this.height = (rows * (10)) + 3;
        this.y = y - this.height;
    }

    private void initializeStatic() {
        // first time init of node lists
        if (nodesByCategory == null) {
            nodesByCategory = new HashMap<>();
            for (var entry : NodeCategories.REGISTRY_SUPPLIER.get().entrySet()) {
                nodesByCategory.put(entry.getValue(), new ArrayList<>());
            }

            for (var entry : Nodes.REGISTRY_SUPPLIER.get().entrySet()) {
                NodeCategory category = entry.getValue().getCategory();
                nodesByCategory.get(category).add(entry.getValue());
            }

            for (List<Node> list : nodesByCategory.values()) {
                list.sort(Comparator.comparing(n -> (Component.translatable(n.getTranslationKey()).toString())));
            }
        }

        if (categoriesOrdered == null) {
            categoriesOrdered = new ArrayList<>();

            categoriesOrdered = NodeCategories.REGISTRY_SUPPLIER.get().entrySet().stream()
                    .map(Map.Entry::getValue)
                    .sorted(Comparator.comparing(NodeCategory::getPriority))
                    .toList();
        }

        if (maxNodeNameWidth == null) {
            maxNodeNameWidth = new HashMap<>();

            for (var entry : nodesByCategory.entrySet()) {
                int maxWidth = entry.getValue().stream().mapToInt(node -> Minecraft.getInstance().font.width(Component.translatable(node.getTranslationKey()))).max().orElse(0);
                maxNodeNameWidth.put(entry.getKey(), maxWidth);
            }
        }
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float tickDelta) {
//        GuiComponent.fill(pose, this.x, this.y, this.x + this.width, this.y + this.height, NodeConstants.SPELL_BOOK_BG_COLOR);

        Font font = Minecraft.getInstance().font;
        int xStride = this.width / NodeConstants.NODE_PICKER_COLUMNS;

        for (int i = 0; i < categoriesOrdered.size(); i++) {
//            if (i >= categoriesOrdered.size()) {
//                break;
//            }

            int y = getCategoryY(i);
            int x = getCategoryX(i);

            GuiComponent.fill(pose, x - 1, y - 1, x + xStride - 2, y + 8, categoriesOrdered.get(i).packedColor);
            font.draw(pose, Component.translatable(categoriesOrdered.get(i).translationKey), x, y, NodeConstants.TITLE_TEXT_COLOR);

            if (mouseX >= x && mouseX < x + xStride && mouseY >= y && mouseY < y + 9) {
                GuiComponent.fill(pose, x - 1, y - 1, x + xStride - 1, y + 8, 0x20000000);
            }
        }

        GuiComponent.fill(pose, this.x, this.y, this.x + this.width, this.y + 1, NodeConstants.SPELL_BOOK_SEPARATOR_COLOR);

        if (this.expandedCategory != -1) {
            int popupX = getCategoryX(this.expandedCategory) - 3;
            NodeCategory category = categoriesOrdered.get(this.expandedCategory);
            List<Node> nodes = nodesByCategory.get(category);
            int popupHeight = (nodes.size() * 12) + 3;
            int popupY = getCategoryY(this.expandedCategory) - 2 - popupHeight;
            int popupWidth = maxNodeNameWidth.get(category) + 6;

            GuiComponent.fill(pose, popupX + 2, popupY + 2, popupX + popupWidth + 2, popupY + popupHeight + 2, 0xA0000000); // drop shadow
            GuiComponent.fill(pose, popupX, popupY, popupX + popupWidth, popupY + popupHeight, 0xFF000000);
            GuiComponent.fill(pose, popupX + 1, popupY + 1, popupX + popupWidth - 1, popupY + popupHeight - 1, category.packedColor);

            for (int i = 0; i < nodes.size(); i++) {
                int x = popupX + 3;
                int y = popupY + (12 * i) + 3;
                font.draw(pose, Component.translatable(nodes.get(i).getTranslationKey()), x, y, NodeConstants.TITLE_TEXT_COLOR);

                if (mouseX >= x && mouseX < x + popupWidth - 4 && mouseY >= y && mouseY < y + 9) {
                    GuiComponent.fill(pose, x - 1, y - 1, x + popupWidth - 4, y + 8, 0x20000000);
                }
            }
        }

    }

    private int getCategoryX(int categoryIndex) {
        int xStride = this.width / NodeConstants.NODE_PICKER_COLUMNS;
        int xIndex = (categoryIndex % NodeConstants.NODE_PICKER_COLUMNS);
        return this.x + (xIndex * xStride) + 2;
    }

    private int getCategoryY(int categoryIndex) {
        return this.y + ((categoryIndex / NodeConstants.NODE_PICKER_COLUMNS) * 11) + 3;
    }

    /**
     * Called when a mouse click occurs.
     * @param mouseX X coordinate of click
     * @param mouseY X coordinate of click
     * @return {@code true} if the event was consumed, {@code false} otherwise
     */
    public boolean handleClick(int mouseX, int mouseY) {
        // if popup is up, either interact or close popup
        if (this.expandedCategory != -1) {
            Node hitNode = null;

            int popupX = getCategoryX(this.expandedCategory) - 3;
            NodeCategory category = categoriesOrdered.get(this.expandedCategory);
            List<Node> nodes = nodesByCategory.get(category);
            int popupHeight = (nodes.size() * 12) + 3;
            int popupY = getCategoryY(this.expandedCategory) - 2 - popupHeight;
            int popupWidth = maxNodeNameWidth.get(category) + 6;

            if (mouseX < popupX || mouseX >= popupX + popupWidth || mouseY < popupY || mouseY >= popupY + popupHeight) {
                // close popup
                this.expandedCategory = -1;
                return true;
            }

            for (int i = 0; i < nodes.size(); i++) {
                int x = popupX + 3;
                int y = popupY + (12 * i) + 3;
                if (mouseX >= x && mouseX < x + popupWidth - 4 && mouseY >= y && mouseY < y + 9) {
                    hitNode = nodes.get(i);
                }
            }

            if (hitNode == null) {
                return false;
            }

            NodeInstance inst = hitNode.createInstance();

            this.parent.getInteractionHandler().dragState = SpellBookScreen.DragState.DRAGGING_NODE;
            this.parent.getInteractionHandler().draggedObject = inst;
            Vector2i grabOffset = new Vector2i(-hitNode.getWidth() / 2, -hitNode.getExpectedHeight() / 2);
            this.parent.getInteractionHandler().grabOffset = grabOffset;
            this.parent.getCanvas().nodeAddedFromPicker(inst, mouseX + grabOffset.x, mouseY + grabOffset.y);
            this.expandedCategory = -1;
            return true;
        } else {
            // if popup is not up then open popup if interacted with category button
            int xStride = this.width / NodeConstants.NODE_PICKER_COLUMNS;
            for (int i = 0; i < 12; i++) {
                if (i >= categoriesOrdered.size()) {
                    break;
                }

                int y = getCategoryY(i);
                int x = getCategoryX(i);

                if (mouseX >= x && mouseX < x + xStride && mouseY >= y && mouseY < y + 9) {
                    if (nodesByCategory.get(categoriesOrdered.get(i)).size() > 0) {
                        this.expandedCategory = i;
                    }
                    return true;
                }
            }

            return false;
        }
    }

    public int getHeight() {
        return height;
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narration) {
    }
}
