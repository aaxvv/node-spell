package eu.aaxvv.node_spell.spell.graph;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellDeserializationContext;
import eu.aaxvv.node_spell.spell.graph.nodes.custom.SubSpellPseudoNode;
import eu.aaxvv.node_spell.spell.graph.nodes.sub_spell.SubSpellInputNode;
import eu.aaxvv.node_spell.spell.graph.nodes.sub_spell.SubSpellOutputNode;
import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.*;

/**
 * Holds a graph of node instances and edges which can be executed as a spell.
 * <p>
 * This representation is what is edited in the spell editor.
 */
public class SpellGraph {
    private NodeInstance entrypoint;
    private boolean isSubSpell;

    private final List<NodeInstance> nodeInstances;
    private final List<Edge> edges;

    private final Map<NodeInstance, Socket> externalSockets;

    public SpellGraph() {
        this.edges = new ArrayList<>();
        this.nodeInstances = new ArrayList<>();
        this.externalSockets = new HashMap<>();
        this.isSubSpell = false;
        this.entrypoint = null;
    }

    public NodeInstance getEntrypoint() {
        return entrypoint;
    }

    public void setEntrypoint(NodeInstance entrypoint) {
        this.entrypoint = entrypoint;
    }
    public void setIsSubSpell(boolean subSpell) {
        this.isSubSpell = subSpell;
    }

    public boolean isSubSpell() {
        return this.isSubSpell;
    }


    public List<NodeInstance> getNodeInstances() {
        return Collections.unmodifiableList(this.nodeInstances);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(this.edges);
    }

    public NodeInstance addInstance(NodeInstance instance) {
        this.nodeInstances.add(instance);
        recomputeExternalSockets();
        return instance;
    }

    public void moveInstanceToTop(NodeInstance instance) {
        this.nodeInstances.remove(instance);
        this.nodeInstances.add(instance);
    }

    public void removeInstance(NodeInstance instance) {
        this.nodeInstances.remove(instance);

        for (SocketInstance socket : instance.getSocketInstances()) {
            socket.disconnectAll();
        }
        recomputeEdges();
        recomputeExternalSockets();
    }

    public Edge addEdge(Edge edge) {
        if (edge.getStart().getParentInstance() == edge.getEnd().getParentInstance()) {
            edge.remove();
            return null;
        }
        this.edges.add(edge);
        recomputeEdges();
        return edge;
    }

    public void serialize(CompoundTag nbt) {
        ListTag instanceList = new ListTag();
        for (NodeInstance instance : this.nodeInstances) {
            CompoundTag nodeTag = new CompoundTag();
            instance.serialize(nodeTag);
            instanceList.add(nodeTag);
        }

        ListTag edgeList = new ListTag();
        for (Edge edge : this.edges) {
            int startNodeIdx = this.nodeInstances.indexOf(edge.getStart().getParentInstance());
            int endNodeIdx = this.nodeInstances.indexOf(edge.getEnd().getParentInstance());
            int startSocketHash = edge.getStart().getSerializationHash();
            int endSocketHash = edge.getEnd().getSerializationHash();

            edgeList.add(new IntArrayTag(new int[] {startNodeIdx, endNodeIdx, startSocketHash, endSocketHash}));
        }

        nbt.put("Nodes", instanceList);
        nbt.put("Edges", edgeList);
        if (this.entrypoint != null && this.nodeInstances.contains(this.entrypoint)) {
            nbt.putInt("Entrypoint", this.nodeInstances.indexOf(this.entrypoint));
        }
    }

    public void deserialize(CompoundTag nbt, SpellDeserializationContext context) {
        ListTag instanceList = nbt.getList("Nodes", Tag.TAG_COMPOUND);
        for (int i = 0; i < instanceList.size(); i++) {
            CompoundTag instanceNbt = instanceList.getCompound(i);
            NodeInstance instance = NodeInstance.fromNbt(instanceNbt, context);
            if (instance != null) {
                this.nodeInstances.add(instance);
            }
        }

        ListTag edgeList = nbt.getList("Edges", Tag.TAG_INT_ARRAY);
        for (int i = 0; i < edgeList.size(); i++) {
            int[] edgeNbt = edgeList.getIntArray(i);
            if (edgeNbt.length != 4) {
                continue;
            }

            Edge edge = buildEdge(edgeNbt);
            if (edge != null) {
                this.edges.add(edge);
            }
        }

        if (nbt.contains("Entrypoint")) {
            int entryPointIndex = nbt.getInt("Entrypoint");
            if (entryPointIndex >= 0 && entryPointIndex < this.nodeInstances.size()) {
                this.entrypoint = this.nodeInstances.get(nbt.getInt("Entrypoint"));
            }
        }

        recomputeExternalSockets();
    }

    private Edge buildEdge(int[] nbtData) {
        try {
            NodeInstance startNode = this.nodeInstances.get(nbtData[0]);
            NodeInstance endNode = this.nodeInstances.get(nbtData[1]);
            if (startNode == null || endNode == null) {
                ModConstants.LOG.warn("Could not deserialize edge due to missing node instance.");
                return null;
            }

            SocketInstance startSocket = startNode.getSocketWithHash(nbtData[2]);
            SocketInstance endSocket = endNode.getSocketWithHash(nbtData[3]);

            if (startSocket == null || endSocket == null) {
                ModConstants.LOG.warn("Could not deserialize edge due to missing socket instance.");
                return null;
            } else {
                return Edge.create(startSocket, endSocket);
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException ex) {
            ModConstants.LOG.warn("Could not deserialize edge.", ex);
            return null;
        }
    }

    public void moveEdge(Edge draggedEdge, SocketInstance socket, boolean isDraggingStart) {
        SocketInstance existingEnd = isDraggingStart ? draggedEdge.getEnd() : draggedEdge.getStart();
        removeEdge(draggedEdge);
        addEdge(Edge.create(existingEnd, socket));
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

//    public void findEntrypoint() {
//        boolean found = false;
//        for (NodeInstance instance : this.nodeInstances) {
//            if (instance.getBaseNode() == Nodes.ENTRY_POINT) {
//                if (found) {
//                    ModConstants.LOG.error("Spell graph had multiple entry points.");
//                    break;
//                }
//
//                this.entrypoint = instance;
//                found = true;
//            }
//        }
//    }

    public void clear() {
        this.nodeInstances.clear();
        this.edges.clear();
        this.entrypoint = null;
    }

    public Map<NodeInstance, Socket> getExternalSockets() {
        return externalSockets;
    }

    public void recomputeExternalSockets() {
        this.externalSockets.clear();

        for (NodeInstance instance : this.nodeInstances) {
            if (instance.getBaseNode() instanceof SubSpellInputNode input) {
                Socket socket = input.createSocket(instance);
                this.externalSockets.put(instance, socket);
            } else if (instance.getBaseNode() instanceof SubSpellOutputNode output) {
                Socket socket = output.createSocket(instance);
                this.externalSockets.put(instance, socket);
            }
        }
    }

    public void refreshSubSpellNodes() {
        for (NodeInstance instance : this.nodeInstances) {
            if (instance.getBaseNode() instanceof SubSpellPseudoNode node) {
                node.refresh();
                instance.refreshSocketInstances();
            }
        }
    }
}
