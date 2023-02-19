package eu.aaxvv.node_spell.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.widget.NodeCanvasWidget;
import eu.aaxvv.node_spell.client.widget.NodeConstants;
import eu.aaxvv.node_spell.client.widget.NodePickerWidget;
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
    private NodePickerWidget picker;
    private DragInfo dragInfo;
    public SpellBookScreen(Component title) {
        super(title);
        this.dragInfo = new DragInfo();
    }

    @Override
    protected void init() {
        //TODO the canvas may also need to contain the node list in order to make dragging from canvas to list possible?
        int x = (this.width / 2) - (this.mainAreaWidth / 2);
        int y = (this.height / 2) - (this.mainAreaHeight / 2);
        this.canvas = addRenderableWidget(new NodeCanvasWidget(x, y, mainAreaWidth, mainAreaHeight - NodeConstants.NODE_PICKER_WIDGET_HEIGHT));
        this.picker = addRenderableWidget(new NodePickerWidget(x, y + mainAreaHeight - NodeConstants.NODE_PICKER_WIDGET_HEIGHT, mainAreaWidth, NodeConstants.NODE_PICKER_WIDGET_HEIGHT));
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        this.fillGradient(pose, 0, 0, this.width, this.height, -1072689136, -804253680);

        int x = (this.width / 2) - (this.mainAreaWidth / 2);
        int y = (this.height / 2) - (this.mainAreaHeight / 2);
        GuiComponent.fill(pose, x, y, mainAreaWidth + x, mainAreaHeight + y, bgColor);

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

    @Override
    public boolean mouseClicked(double $$0, double $$1, int $$2) {
        return super.mouseClicked($$0, $$1, $$2);
    }

    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        return super.mouseReleased($$0, $$1, $$2);
    }

    private class DragInfo {
        public DragState dragState;
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

    private enum DragState {
        NOT_DRAGGING,
        PANNING_CANVAS,
        DRAGGING_NODE,
        DRAGGING_EDGE,

    }
}
