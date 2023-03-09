package eu.aaxvv.node_spell.client.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.Optional;

/**
 * TODO: look at for inspiration in rendering
 * {@link  net.minecraft.client.gui.screens.advancements.AdvancementsScreen#render}
 */
public class NodeCanvasWidget implements Renderable, GuiEventListener, NarratableEntry {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final GraphRenderer renderer;
    private int prevWindowPanX;
    private int prevWindowPanY;
    private final SpellGraph graph;

    private Edge draggedEdge;
    private boolean isDraggingStart;
    private Vector2i dragPos;
    public NodeCanvasWidget(int x, int y, int width, int height, SpellGraph graph) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.graph = graph;
        this.renderer = new GraphRenderer(this, x, y, width, height, graph);
        this.prevWindowPanX = 0;
        this.prevWindowPanY = 0;
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        this.renderer.renderGraph(pose, mouseX, mouseY, tickDelta);
    }

    public void startWindowPan() {
        this.prevWindowPanX = this.renderer.getWindowPanX();
        this.prevWindowPanY = this.renderer.getWindowPanY();
    }

    public void setWindowPanOffset(int dx, int dy) {
        this.renderer.setWindowPan(this.prevWindowPanX + dx, this.prevWindowPanY + dy);
    }

    public void startDragEdge(SocketInstance instance, int x, int y) {
        // if socket is empty or is output -> start new edge
        // if it is input and not empty -> move existing edge
        if (!instance.acceptsSingleConnection() || instance.getConnections().size() == 0) {
            // start new connection
            this.draggedEdge = Edge.createIncomplete(instance);
            this.isDraggingStart = false;
        } else {
            // move existing
            this.draggedEdge = instance.getSingleConnection();
            this.isDraggingStart = this.draggedEdge.getStart() == instance;
        }

        int localX = x - this.x - this.renderer.getWindowPanX();
        int localY = y - this.y - this.renderer.getWindowPanY();
        this.dragPos = new Vector2i(localX, localY);
    }

    public void setDraggedEdgePos(int x, int y) {
        int localX = x - this.x - this.renderer.getWindowPanX();
        int localY = y - this.y - this.renderer.getWindowPanY();
        this.dragPos = new Vector2i(localX, localY);
    }

    public void stopDragEdge(int x, int y) {
        Optional<Object> target = this.getObjectAtLocation(x, y);

        if (target.isPresent() && target.get() instanceof SocketInstance socket) {
            // hit a socket
            if (this.draggedEdge.isIncomplete()) {
                // only add if data types match and direction is different
                if (Edge.typesCompatible(this.draggedEdge.getStart(), socket)) { // && this.draggedEdge.getDatatype() == socket.getBase().getDataType() && this.draggedEdge.getStart().getBase().getDirection() != socket.getBase().getDirection()
                    this.graph.addEdge(this.draggedEdge.complete(socket));
                }
            } else {
                SocketInstance existingSocket = this.isDraggingStart ? this.draggedEdge.getEnd() : this.draggedEdge.getStart();
                if (Edge.typesCompatible(existingSocket, socket)) { // this.draggedEdge.getDatatype() == socket.getBase().getDataType() && existingDirection != socket.getBase().getDirection()
                    this.graph.moveEdge(this.draggedEdge, socket, this.isDraggingStart);
                }
            }
        } else {
            // hit something else -> delete edge
            if (!this.draggedEdge.isIncomplete()) {
                this.graph.removeEdge(this.draggedEdge);
            }
        }

        this.draggedEdge = null;
    }

    public Edge getDraggedEdge() {
        return draggedEdge;
    }

    public Vector2i getDragPos() {
        return dragPos;
    }

    public boolean isDraggingStart() {
        return isDraggingStart;
    }

    public Optional<Object> getObjectAtLocation(int x, int y) {
        int localX = x - this.x - this.renderer.getWindowPanX();
        int localY = y - this.y - this.renderer.getWindowPanY();

        // first check sockets, then nodes
        // iterate in reverse to pick topmost nodes first
        for (int i = this.graph.getNodeInstances().size() - 1; i >= 0; i--) {
            NodeInstance instance = this.graph.getNodeInstances().get(i);
            for (SocketInstance socket : instance.getSocketInstances()) {
                if (socket.containsPoint(localX, localY)) {
                    return Optional.of(socket);
                }
            }

//            if (instance.getWidget() != null && instance.getWidget().isHit(localX, localY)) {
//                return Optional.of(instance.getWidget());
//            }

            if (instance.containsPoint(localX, localY)) {
                return Optional.of(instance);
            }
        }

        return Optional.empty();
    }

    public Vector2i getNodePositionGlobal(NodeInstance nodeInstance) {
        int globalX = nodeInstance.getX() + this.x + this.renderer.getWindowPanX();
        int globalY = nodeInstance.getY() + this.y + this.renderer.getWindowPanY();

        return new Vector2i(globalX, globalY);
    }

    public void setNodePositionLocal(NodeInstance nodeInstance, int x, int y) {
        int localX = x - this.x - this.renderer.getWindowPanX();
        int localY = y - this.y - this.renderer.getWindowPanY();

        nodeInstance.setPosition(localX, localY);
    }

    public Vector2i toLocal(int x, int y) {
        int localX = x - this.x - this.renderer.getWindowPanX();
        int localY = y - this.y - this.renderer.getWindowPanY();

        return new Vector2i(localX, localY);
    }

    public boolean containsPoint(int x, int y) {
        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput var1) {

    }

    public void nodeAddedFromPicker(NodeInstance instance, int x, int y) {
        this.graph.addInstance(instance);
        int localX = x - this.x - this.renderer.getWindowPanX();
        int localY = y - this.y - this.renderer.getWindowPanY();
        instance.setPosition(localX, localY);
    }

    public void deleteNode(NodeInstance instance) {
        this.graph.removeInstance(instance);
    }

    public void clearGraph() {
        this.graph.clear();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
