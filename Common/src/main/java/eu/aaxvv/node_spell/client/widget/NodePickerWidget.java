package eu.aaxvv.node_spell.client.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.spell.graph.nodes.NodeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class NodePickerWidget implements Renderable, GuiEventListener, NarratableEntry {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public NodePickerWidget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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

            if (mouseX >= x - 1 && mouseX < x + xStride - 1 && mouseY >= y - 1 && mouseY < y + 8) {
                GuiComponent.fill(pose, x - 1, y - 1, x + xStride - 1, y + 8, 0x20000000);
            }
        }

        GuiComponent.fill(pose, this.x, this.y, this.x + this.width, this.y + 1, NodeConstants.SPELL_BOOK_SEPARATOR_COLOR);

    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narration) {
    }
}
