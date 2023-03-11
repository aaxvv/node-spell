package eu.aaxvv.node_spell.client.gui.graph_editor;

import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class NodeGraphView {
    private final SpellGraph graph;
    private final GuiElement edgeParent;
    private final GuiElement nodeParent;
    private TriConsumer<GuiNodeView, Double, Double> nodeClickedCallback;
    private TriConsumer<GuiNodeView, Double, Double> nodeReleasedCallback;
    private Consumer<SocketInstance> socketClickedCallback;
    private Consumer<SocketInstance> socketReleasedCallback;


    private Map<NodeInstance, GuiNodeView> instances;
    private Map<Edge, GuiEdgeView> edges;
    private GuiDraggingEdgeView draggingEdge;

    // store dragging edge here?

    public NodeGraphView(SpellGraph graph, GuiElement edgeParent, GuiElement nodeParent) {
        this.graph = graph;
        this.edgeParent = edgeParent;
        this.nodeParent = nodeParent;

        this.instances = new HashMap<>();
        this.edges = new HashMap<>();
        this.draggingEdge = null;

        createInitialElements();
    }

    public void createInitialElements() {
        for (NodeInstance instance : this.graph.getNodeInstances()) {
            addNode(instance);
        }

        for (Edge edge : this.graph.getEdges()) {
            addEdge(edge);
        }
    }

    public Collection<GuiNodeView> getNodes() {
        return this.instances.values();
    }

    public Collection<GuiEdgeView> getEdges() {
        return this.edges.values();
    }

    public GuiNodeView addNewNode(NodeInstance instance) {
        this.graph.addInstance(instance);
        return addNode(instance);
    }

    public GuiNodeView addNode(NodeInstance node) {
        GuiNodeView view = new GuiNodeView(node);
        this.instances.put(node, view);
        this.nodeParent.addChild(view);
        view.setNodeInteractCallback((x, y, mouseDown) -> this.nodeInteract(view, x, y, mouseDown));
        view.setSocketInteractCallback(this::socketInteract);
        return view;
    }

    public void removeNode(GuiNodeView node) {
        this.getConnectedEdges(node).forEach(this::removeEdge);
        this.graph.removeInstance(node.getInstance());
        this.instances.remove(node.getInstance());
        this.nodeParent.removeChild(node);
    }

    public GuiEdgeView addNewEdge(Edge edge) {
        Edge toAdd = this.graph.addEdge(edge);
        if (toAdd == null) {
            return null;
        }
        return addEdge(toAdd);
    }

    public GuiEdgeView addEdge(Edge edge) {
        GuiEdgeView view = new GuiEdgeView(edge);
        this.edges.put(edge, view);
        this.edgeParent.addChild(view);
        recomputeConnected(edge.getStart());
        recomputeConnected(edge.getEnd());
        return view;
    }

    public void removeEdge(GuiEdgeView edge) {
        this.graph.removeEdge(edge.getInstance());
        this.edges.remove(edge.getInstance());
        this.edgeParent.removeChild(edge);
    }

    public GuiNodeView getNodeFor(NodeInstance parentInstance) {
        return this.instances.get(parentInstance);
    }

    public void invalidateConnected(GuiNodeView node) {
        for (SocketInstance socket : node.getInstance().getSocketInstances()) {
            for (Edge edge : socket.getConnections()) {
                GuiEdgeView view = this.edges.get(edge);
                if (view != null) {
                    view.invalidate();
                }
            }
        }
    }

    public void recomputeConnected(SocketInstance socket) {
        Iterator<Map.Entry<Edge, GuiEdgeView>> edgeIter = this.edges.entrySet().iterator();

        while (edgeIter.hasNext()) {
            Map.Entry<Edge, GuiEdgeView> entry = edgeIter.next();

            if (entry.getKey().getStart() != socket && entry.getKey().getEnd() != socket) {
                continue;
            }

            if (!this.graph.getEdges().contains(entry.getKey())) {
                this.edgeParent.removeChild(entry.getValue());
                edgeIter.remove();
            }
        }
    }

    public List<GuiEdgeView> getConnectedEdges(GuiNodeView node) {
        List<GuiEdgeView> edges = new ArrayList<>();

        for (SocketInstance socket : node.getInstance().getSocketInstances()) {
            for (Edge edge : socket.getConnections()) {
                GuiEdgeView view = this.edges.get(edge);
                if (view != null) {
                    edges.add(view);
                }
            }
        }

        return edges;
    }

    public void setNodeClickedCallback(TriConsumer<GuiNodeView, Double, Double> nodeClickedCallback) {
        this.nodeClickedCallback = nodeClickedCallback;
    }

    private void nodeClicked(GuiNodeView node, double screenX, double screenY) {
        if (this.nodeClickedCallback != null)  {
            this.nodeClickedCallback.accept(node, screenX, screenY);
        }
    }

    public void setNodeReleasedCallback(TriConsumer<GuiNodeView, Double, Double> nodeReleasedCallback) {
        this.nodeReleasedCallback = nodeReleasedCallback;
    }

    private void nodeReleased(GuiNodeView node, double screenX, double screenY) {
        if (this.nodeReleasedCallback != null)  {
            this.nodeReleasedCallback.accept(node, screenX, screenY);
        }
    }

    private void nodeInteract(GuiNodeView node, double screenX, double screenY, boolean mouseDown) {
        if (mouseDown) {
            if (this.nodeClickedCallback != null)  {
                this.nodeClickedCallback.accept(node, screenX, screenY);
            }
        } else {
            if (this.nodeReleasedCallback != null)  {
                this.nodeReleasedCallback.accept(node, screenX, screenY);
            }
        }
    }

    public void setSocketClickedCallback(Consumer<SocketInstance> socketClickedCallback) {
        this.socketClickedCallback = socketClickedCallback;
    }

    public void setSocketReleasedCallback(Consumer<SocketInstance> socketReleasedCallback) {
        this.socketReleasedCallback = socketReleasedCallback;
    }

    private void socketInteract(SocketInstance socket, boolean mouseDown) {
        if (mouseDown) {
            if (this.socketClickedCallback != null) {
                this.socketClickedCallback.accept(socket);
            }
        } else {
            if (this.socketReleasedCallback != null) {
                this.socketReleasedCallback.accept(socket);
            }
        }
    }

    public void startDragEdge(SocketInstance socket) {
        // if socket is empty or is output -> start new edge
        // if it is input and not empty -> move existing edge
        if (!socket.acceptsSingleConnection() || socket.getConnections().size() == 0) {
            // start new connection
            this.draggingEdge = new GuiDraggingEdgeView(socket);
        } else {
            // move existing
            Edge prevEdge = socket.getSingleConnection();
            this.removeEdge(this.edges.get(prevEdge));
            this.draggingEdge = new GuiDraggingEdgeView(prevEdge.getOpposite(socket), prevEdge);
        }

        this.edgeParent.addChild(this.draggingEdge);
    }

    public void stopDragEdge(NodeInstance node, SocketInstance socket) {
        if (this.draggingEdge == null) {
            return;
        }

        if (socket != null) {
            Edge newEdge = this.draggingEdge.complete(socket);
            if (newEdge != null) {
                this.addNewEdge(newEdge);
            }

        } else if (node != null) {
            for (SocketInstance possibleSocket : node.getSocketInstancesSorted()) {
                if (Edge.typesCompatible(this.draggingEdge.getExistingSocket(), possibleSocket)) {
                    Edge newEdge = this.draggingEdge.complete(possibleSocket);
                    if (newEdge != null) {
                        this.addNewEdge(newEdge);
                        break;
                    }
                }
            }

        } else {
            // TODO: maybe create matching node?
       }

        this.edgeParent.removeChild(this.draggingEdge);
        this.draggingEdge = null;
    }

    public void updateDragPos(int localX, int localY) {
        if (this.draggingEdge != null) {
            this.draggingEdge.setLocalEndpoint(localX, localY);
        }
    }

    public Predicate<Node> getDraggingEdgeTargetFilter() {
        if (this.draggingEdge == null) {
            return node -> true;
        }

        Socket existingSocket = this.draggingEdge.getExistingSocket().getBase();

        return node -> {
            for (Socket socket : node.getSockets()) {
                if (Edge.typesCompatible(existingSocket, socket)) {
                    return true;
                }
            }

            return false;
        };
    }

    public boolean isDraggingNewEdge() {
        return this.draggingEdge != null && this.draggingEdge.isNew();
    }
}
