package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.nodes.NodeCategory;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.aaxvv.node_spell.client.widget.NodeConstants;
import net.minecraft.resources.ResourceLocation;

/**
 * Represents a type of node which can be used in a spell graph.
 * <p>
 * Like the vanilla Block class these Nodes are singletons and don't represent a specific node instance,
 * but the general layout and behavior of this type of node.
 *
 * @see NodeInstance
 */
public abstract class Node {
    private final String name;
    private final NodeCategory category;
    private final List<Socket> sockets;
    private int inSocketCount;
    private int outSocketCount;
    private final ResourceLocation resourceLocation;

    public Node(String name, NodeCategory category, ResourceLocation resourceLocation) {
        this.name = name;
        this.category = category;
        this.sockets = new ArrayList<>();
        this.inSocketCount = 0;
        this.outSocketCount = 0;
        this.resourceLocation = resourceLocation;
    }

    private void addSocket(Socket socket) {
        this.sockets.add(socket);
    }

    public final List<Socket> getSockets() {
        return Collections.unmodifiableList(this.sockets);
    }

    protected final Socket addInputSocket(Datatype datatype, String name) {
        Socket socket = new Socket(datatype, name, this, Socket.Direction.IN, this.inSocketCount);
        addSocket(socket);
        this.inSocketCount++;
        return socket;
    }

    protected final Socket addOutputSocket(Datatype datatype, String name) {
        Socket socket = new Socket(datatype, name, this, Socket.Direction.OUT, this.outSocketCount);
        addSocket(socket);
        this.outSocketCount++;
        return socket;
    }

    public NodeInstance createInstance() {
        return new NodeInstance(this);
    }

    public String getName() {
        return name;
    }

    public NodeCategory getCategory() {
        return category;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public int getExpectedHeight() {
        return NodeConstants.SOCKET_START_Y + Math.max(this.inSocketCount, this.outSocketCount) * NodeConstants.SOCKET_STEP_Y;
    }

    public int getWidth() {
        return NodeConstants.DEFAULT_NODE_WIDTH;
    }

    public abstract Object createInstanceData();

    public abstract void run(SpellContext ctx, NodeInstance instance);
}
