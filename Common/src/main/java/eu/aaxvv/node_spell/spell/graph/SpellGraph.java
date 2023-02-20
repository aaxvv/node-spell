package eu.aaxvv.node_spell.spell.graph;

import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

/**
 * Holds a graph of node instances and edges which can be executed as a spell.
 * <p>
 * This representation is what is edited in the spell editor.
 */
public class SpellGraph {
    private NodeInstance entrypoint;

    private final List<NodeInstance> nodeInstances;
    private final List<Edge> edges;

    private List<Argument> arguments;

    public SpellGraph() {
        this.edges = new ArrayList<>();
        this.nodeInstances = new ArrayList<>();
        this.arguments = new ArrayList<>();
    }

    public NodeInstance getEntrypoint() {
        return entrypoint;
    }

    public List<NodeInstance> getNodeInstances() {
        return Collections.unmodifiableList(this.nodeInstances);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(this.edges);
    }

    public NodeInstance addInstance(NodeInstance instance) {
        this.nodeInstances.add(instance);
        return instance;
    }

    public Edge addEdge(Edge edge) {
        this.edges.add(edge);
        recomputeEdges();
        return edge;
    }

    public void setEntrypoint(NodeInstance entrypoint) {
        this.entrypoint = entrypoint;
    }

    public void serialize(CompoundTag nbt) {

    }

    public void deserialize(CompoundTag nbt) {

    }

    public void moveEdge(Edge draggedEdge, SocketInstance socket, boolean isDraggingStart) {
        SocketInstance exisitingEnd = isDraggingStart ? draggedEdge.getStart() : draggedEdge.getEnd();
        removeEdge(draggedEdge);
        addEdge(Edge.create(exisitingEnd, socket));
    }

    public void removeEdge(Edge draggedEdge) {
        draggedEdge.remove();
        this.edges.remove(draggedEdge);
        recomputeEdges();
    }

    private void recomputeEdges() {
        this.edges.clear();
        Set<Edge> allEdges = new HashSet<>();
        for (NodeInstance instance : this.nodeInstances) {
            for (SocketInstance socket : instance.getSocketInstances()) {
                allEdges.addAll(socket.getConnections());
            }
        }

        this.edges.addAll(allEdges);
    }

    public record Argument(String name, Datatype type){};
}
