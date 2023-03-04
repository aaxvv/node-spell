package eu.aaxvv.node_spell.client.gui.graph_editor;

import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NodeGraphView {
    private final SpellGraph graph;
    private final GuiElement edgeParent;
    private final GuiElement nodeParent;

    private Map<NodeInstance, GuiNodeView> instances;
    private Map<Edge, GuiEdgeView> edges;

    // store dragging edge here?

    public NodeGraphView(SpellGraph graph, GuiElement edgeParent, GuiElement nodeParent) {
        this.graph = graph;
        this.edgeParent = edgeParent;
        this.nodeParent = nodeParent;
        this.instances = new HashMap<>();
        this.edges = new HashMap<>();
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

    public GuiNodeView addNewNode(Node baseNode) {
        NodeInstance instance = baseNode.createInstance();
        this.graph.addInstance(instance);
        return addNode(instance);
    }

    public GuiNodeView addNode(NodeInstance node) {
        GuiNodeView view = new GuiNodeView(node, this);
        this.instances.put(node, view);
        this.nodeParent.addChild(view);
        return view;
    }

    public void removeNode(GuiNodeView node) {
        this.graph.removeInstance(node.getInstance());
        this.instances.remove(node.getInstance());
        this.nodeParent.removeChild(node);
    }

    public GuiEdgeView addNewEdge(Edge edge) {
        this.graph.addEdge(edge);
        return addEdge(edge);
    }

    public GuiEdgeView addEdge(Edge edge) {
        GuiEdgeView view = new GuiEdgeView(edge, this);
        this.edges.put(edge, view);
        this.edgeParent.addChild(view);
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
        //TODO
    }
}
