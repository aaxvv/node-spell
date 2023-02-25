package eu.aaxvv.node_spell.spell.graph.runtime;

import eu.aaxvv.node_spell.client.node_widget.Widget;
import eu.aaxvv.node_spell.client.widget.NodeConstants;
import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.Nodes;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

/**
 * Represents a specific instance of a node within a spell graph.
 * <p>
 * It kind of sucks how editor/client stuff is also stored in here, but I don't care enough to do it better right now.
 */
public class NodeInstance {
    /** The node type of this instance */
    private final Node base;

    /** Additional data which must be stored with this instance e.g. the value of a constant node */
    private Object instanceData;

    private final Widget<?> widget;

    /** The socket instances of this node */
    private final Map<Socket, SocketInstance> socketInstances;

    private int x;
    private int y;

    public NodeInstance(Node base) {
        this.base = base;
        this.instanceData = base.createInstanceData();
        this.widget = base.createWidget(this);
        this.socketInstances = new HashMap<>();
        base.getSockets().forEach(s -> this.socketInstances.put(s, s.createInstance(this)));
    }

    public static NodeInstance fromNbt(CompoundTag instanceNbt) {
        ResourceLocation baseNodeResLoc = ResourceLocation.tryParse(instanceNbt.getString("Base"));
        Node baseNode = Nodes.REGISTRY_SUPPLIER.get().get(baseNodeResLoc);
        if (baseNode == null) {
            return null;
        }

        NodeInstance instance = baseNode.createInstance();
        instance.deserialize(instanceNbt);
        if (instance.widget != null) {
            instance.widget.rollbackValue();
        }
        return instance;
    }

    public Object getInstanceData() {
        return instanceData;
    }

    public void setInstanceData(Object data) {
        if (!this.getInstanceData().getClass().isAssignableFrom(data.getClass())) {
            throw new IllegalArgumentException("Node instance data type does not match widget type.");
        }

        this.instanceData = data;
    }

    public Node getBaseNode() {
        return base;
    }

    public Widget<?> getWidget() {
        return widget;
    }

    public Value getSocketValue(Socket socket, SpellContext ctx) {
        SocketInstance instance = this.socketInstances.get(socket);
        if (instance == null) {
            throw new IllegalArgumentException("Socket does not exist on this instance.");
        }
        return instance.getComputedValue(ctx);
    }

    public void setSocketValue(Socket socket, Value value) {
        if (socket.getDirection() != Socket.Direction.OUT) {
            throw new IllegalArgumentException("Cannot set value of an input socket.");
        }

        SocketInstance instance = this.socketInstances.get(socket);
        if (instance == null) {
            throw new IllegalArgumentException("Socket does not exist on this instance.");
        }
        instance.setCurrentValue(value);
    }

    public List<Edge> getSocketConnections(Socket socket) {
        SocketInstance instance =  this.socketInstances.get(socket);
        if (instance == null) {
            throw new IllegalArgumentException("Socket does not exist on this instance.");
        }
        return instance.getConnections();
    }

    public Optional<Edge> getSingleSocketConnection(Socket socket) {
        SocketInstance instance =  this.socketInstances.get(socket);
        if (instance == null) {
            throw new IllegalArgumentException("Socket does not exist on this instance.");
        }
        return Optional.ofNullable(instance.getSingleConnection());
    }

    public Optional<NodeInstance> getSingleSocketPartner(Socket socket) {
        SocketInstance instance = this.socketInstances.get(socket);
        if (instance == null) {
            throw new IllegalArgumentException("Socket does not exist on this instance.");
        }

        if (instance.getConnections().isEmpty()) {
            return Optional.empty();
        }

        Edge connection = instance.getSingleConnection();
        if (connection == null) {
            return Optional.empty();
        }

        return Optional.of(connection.getOpposite(instance).getParentInstance());
    }

    public SocketInstance getSocketInstance(Socket socket) {
        SocketInstance instance =  this.socketInstances.get(socket);
        if (instance == null) {
            throw new IllegalArgumentException("Socket does not exist on this instance.");
        }
        return instance;
    }

    public Collection<SocketInstance> getSocketInstances() {
        return this.socketInstances.values();
    }

    public void run(SpellContext ctx) {
        this.base.run(ctx, this);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        setPosition(x, this.y);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        setPosition(this.x, y);
    }

    public int getSocketX(Socket.Direction direction) {
        int inputX = x - 2;
        int outputX = x + this.base.getWidth() - 3;
        return direction == Socket.Direction.IN ? inputX : outputX;
    }

    public int getSocketY(int index) {
        int socketStartY = y + NodeConstants.SOCKET_START_Y;
        return socketStartY + (index * NodeConstants.SOCKET_STEP_Y);
    }

    public boolean containsPoint(int x, int y) {
        return x >= this.x && x < this.x + this.base.getWidth() && y >= this.y && y < this.y + this.base.getExpectedHeight();
    }

    public void serialize(CompoundTag nodeTag) {
        nodeTag.putString("Base", this.base.getResourceLocation().toString());
        nodeTag.putInt("X", this.x);
        nodeTag.putInt("Y", this.y);
        if (this.getInstanceData() != null) {
            CompoundTag dataTag = new CompoundTag();
            this.base.serializeInstanceData(this.instanceData, dataTag);
            nodeTag.put("Data", dataTag);
        }
    }

    public void deserialize(CompoundTag nodeTag) {
        this.x = nodeTag.getInt("X");
        this.y = nodeTag.getInt("Y");
        if (nodeTag.contains("Data", Tag.TAG_COMPOUND)) {
            this.instanceData = this.base.deserializeInstanceData(nodeTag.getCompound("Data"));
        }
    }

    public SocketInstance getSocketWithHash(int socketHash) {
        for (SocketInstance socket : this.getSocketInstances()) {
            if (socket.getSerializationHash() == socketHash) {
                return socket;
            }
        }

        return null;
    }
}
