package eu.aaxvv.node_spell.client.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.spell.graph.NodeRegistry;
import eu.aaxvv.node_spell.spell.graph.nodes.NodeCategory;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class NodePickerWidget implements Renderable, GuiEventListener, NarratableEntry {
    private static Map<NodeCategory, List<Node>> nodesByCategory;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public NodePickerWidget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        if (nodesByCategory == null) {
            nodesByCategory = new HashMap<>();

            for (var entry : NodeRegistry.INSTANCE.entrySet()) {
                NodeCategory category = entry.getValue().getCategory();
                if (!nodesByCategory.containsKey(category)) {
                    nodesByCategory.put(category, new ArrayList<>());
                }
                nodesByCategory.get(category).add(entry.getValue());
            }
        }
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        GuiComponent.fill(pose, this.x, this.y, this.x + this.width, this.y + this.height, NodeConstants.SPELL_BOOK_BG_COLOR);

        NodeCategory[] categories = NodeCategory.values();

        Font font = Minecraft.getInstance().font;
        int xStride = this.width / 6;
        for (int i = 0; i < 12; i++) {
            if (i >= categories.length) {
                break;
            }

            int y = i < 6 ? this.y + 3 : this.y + 15;
            int x = this.x + (i % 6) * xStride + 2;

            font.draw(pose, Component.translatable(categories[i].translationKey), x, y, NodeConstants.TITLE_TEXT_COLOR);

            if (mouseX >= x && mouseX < x + xStride && mouseY >= y && mouseY < y + 9) {
                GuiComponent.fill(pose, x - 1, y - 1, x + xStride - 1, y + 8, 0x20000000);
            }
        }

        GuiComponent.fill(pose, this.x, this.y, this.x + this.width, this.y + 1, NodeConstants.SPELL_BOOK_SEPARATOR_COLOR);

    }

    /**
     * Called when a mouse click occurs.
     * @param x X coordinate of click
     * @param y X coordinate of click
     * @return {@code true} if the event was consumed, {@code false} otherwise
     */
    public boolean handleClick(int x, int y, Consumer<NodeInstance> newInstanceConsumer) {
        return false;
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narration) {
    }
}
