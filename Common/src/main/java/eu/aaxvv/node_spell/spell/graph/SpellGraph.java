package eu.aaxvv.node_spell.spell.graph;

import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
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

    private Map<String, Datatype> arguments;

    public SpellGraph() {
        this.edges = new ArrayList<>();
        this.nodeInstances = new ArrayList<>();
        this.arguments = new HashMap<>();
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
        return edge;
    }

    public void setEntrypoint(NodeInstance entrypoint) {
        this.entrypoint = entrypoint;
    }

    public void serialize(CompoundTag nbt) {

    }

    public void deserialize(CompoundTag nbt) {

    }
}
