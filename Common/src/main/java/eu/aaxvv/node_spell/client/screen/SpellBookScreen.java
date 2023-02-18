package eu.aaxvv.node_spell.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.widget.NodeCanvasWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;

/**
 * Look at BookEditScreen for inspiration
 */
public class SpellBookScreen extends Screen {
    // normal inventory width. can be extended if needed (e.g. beacon ui is 230)
    private final int mainAreaWidth = 288;
    // height of double chest screen
    private final int mainAreaHeight = 208;
    private final int bgColor = 0xFFD8D1A9;
    private NodeCanvasWidget canvas;
    public SpellBookScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        //TODO the canvas my also need to contain the node list in order to make dragging from canvas to list possible?
        this.canvas = addRenderableWidget(new NodeCanvasWidget((this.width / 2) - (this.mainAreaWidth / 2), (this.height / 2) - (this.mainAreaHeight / 2), mainAreaWidth - 64, mainAreaHeight));
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        this.fillGradient(pose, 0, 0, this.width, this.height, -1072689136, -804253680);

        int x = (this.width / 2) - (this.mainAreaWidth / 2);
        int y = (this.height / 2) - (this.mainAreaHeight / 2);
        GuiComponent.fill(pose, x, y, mainAreaWidth + x, mainAreaHeight + y, bgColor);

        int nodeListX = x + this.mainAreaWidth - 63;

        Minecraft.getInstance().font.draw(pose, "Math", nodeListX + 4, y + 4, 0xFF000000);
        Minecraft.getInstance().font.draw(pose, "Subtract", nodeListX + 4 + 8, y + 4 + 9, 0xFF000000);
        Minecraft.getInstance().font.draw(pose, "Divide", nodeListX + 4 + 8, y + 4 + 18, 0xFF000000);

        GuiComponent.fill(pose, nodeListX, y, nodeListX - 1, y+ mainAreaHeight, 0xFF000000);

        super.render(pose, mouseX, mouseY, tickDelta);
    }

    @Override
    public void mouseMoved(double x, double y) {
        super.mouseMoved(x, y);
    }

    @Override
    public boolean mouseDragged(double x, double y, int activeButton, double dx, double dy) {
        return super.mouseDragged(x, y, activeButton, dx, dy);
    }



    private class DragInfo {
        public boolean isDragging;
        public Vector2i startPoint;
        public Object draggedObject;

        public void mouseDown(int x, int y) {

        }

        public void mouseMove(int x, int y) {

        }

        public void mouseUp(int x, int y) {
            // if moved some distance -> drag, else click
        }

    }
}
