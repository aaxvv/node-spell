package eu.aaxvv.node_spell.client.gui.graph_editor;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.gui.base.UnboundedGuiElement;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public class GuiGraphEditor extends UnboundedGuiElement {
    private static final int SELECTION_RECT_COLOR = 0x553333DD;

    private final NodeGraphView graphView;
    private final GuiElement edgeLayer;
    private final GuiElement nodeLayer;
    private final Set<GuiNodeView> selectedNodes;
    private Vector2i selectionStart;
    private Vector2i selectionEnd;
    private CurrentAction currentAction;

    public GuiGraphEditor(SpellGraph graph) {
        super();
        this.selectedNodes = new HashSet<>();
        this.edgeLayer = new UnboundedGuiElement();
        this.addChild(edgeLayer);
        this.nodeLayer = new UnboundedGuiElement();
        this.addChild(nodeLayer);
        this.graphView = new NodeGraphView(graph, this.edgeLayer, this.nodeLayer);
        this.graphView.setNodeClickedCallback(this::nodeClicked);
        this.currentAction = CurrentAction.NONE;
    }

    private void nodeClicked(GuiNodeView node, double screenX, double screenY) {
        if (!this.selectedNodes.contains(node)) {
            this.selectedNodes.clear();
            this.selectedNodes.add(node);
        }

        updateSelectionState();
        this.currentAction = CurrentAction.DRAGGING_NODES;

        Vector2i dragStartPos = this.toLocal((int) screenX, (int) screenY);
        for (GuiNodeView selectedNode : this.selectedNodes) {
            selectedNode.setDragOffset(new Vector2i(selectedNode.getLocalX(), selectedNode.getLocalY()).sub(dragStartPos).negate());
        }
    }

    @Override
    public boolean onMouseDown(double screenX, double screenY, int button) {
        if (super.onMouseDown(screenX, screenY, button)) {
            return true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            // left click on empty space
            this.selectedNodes.clear();
            this.currentAction = CurrentAction.DRAGGING_SELECTION;
            updateSelectionState();
            requestFocus();
            return true;
        }

        return false;
    }

    @Override
    public boolean onMouseDragged(double screenX, double screenY, int button, double dX, double dY) {
        if (super.onMouseDragged(screenX, screenY, button, dX, dY)) {
            return true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (this.currentAction == CurrentAction.DRAGGING_NODES) {
                dragSelectedNodes(screenX, screenY);
            } else if (this.currentAction == CurrentAction.DRAGGING_SELECTION) {
                boxSelect(screenX, screenY);
            }
            return false;
        }

        return false;
    }

    private void dragSelectedNodes(double screenX, double screenY) {
        Vector2i dragPos = this.toLocal((int) screenX, (int) screenY);

        for (GuiNodeView node : this.selectedNodes) {
            Vector2i offset = node.getDragOffset();
            node.setLocalPosition(dragPos.x - offset.x, dragPos.y - offset.y);
            this.graphView.invalidateConnected(node);
        }
    }

    private void boxSelect(double screenX, double screenY) {
        if (this.selectionStart == null) {
            this.selectionStart = this.toLocal((int) screenX, (int) screenY);
            this.selectionEnd = new Vector2i(this.selectionStart);
            return;
        }

        // continue dragging
        this.selectionEnd = this.toLocal((int) screenX, (int) screenY);

        int x = Math.min(this.selectionStart.x, this.selectionEnd.x);
        int y = Math.min(this.selectionStart.y, this.selectionEnd.y);
        int w = Math.abs(this.selectionStart.x - this.selectionEnd.x);
        int h = Math.abs(this.selectionStart.y - this.selectionEnd.y);

        this.selectedNodes.clear();
        for (GuiNodeView node : this.graphView.getNodes()) {
            if (node.intersectsRectLocal(x, y, w, h)) {
                this.selectedNodes.add(node);
            }
        }
        updateSelectionState();
    }

    @Override
    public boolean onMouseUp(double screenX, double screenY, int button) {
        if (super.onMouseUp(screenX, screenY, button)) {
            return true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            this.selectionStart = null;
            this.selectionEnd = null;
            this.currentAction = CurrentAction.NONE;
            releaseFocus();
            return true;
        }

        return false;
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.onKeyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_DELETE || keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            this.selectedNodes.forEach(this.graphView::removeNode);
            this.selectedNodes.clear();
            return true;
        }

        return false;
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        super.render(pose, mouseX, mouseY, tickDelta);

        if (selectionStart != null) {
            int x = Math.min(this.selectionStart.x, this.selectionEnd.x) + this.getGlobalX();
            int y = Math.min(this.selectionStart.y, this.selectionEnd.y) + this.getGlobalY();

            RenderUtil.drawRect(pose, x, y, Math.abs(this.selectionStart.x - this.selectionEnd.x), Math.abs(this.selectionStart.y - this.selectionEnd.y), SELECTION_RECT_COLOR);
        }
    }

    private void updateSelectionState() {
        for (GuiNodeView node : this.graphView.getNodes()) {
            node.setSelected(this.selectedNodes.contains(node));
        }
    }

    private enum CurrentAction {
        NONE,
        DRAGGING_NODES,
        DRAGGING_SELECTION,
        DRAGGING_EDGE
    }
}
