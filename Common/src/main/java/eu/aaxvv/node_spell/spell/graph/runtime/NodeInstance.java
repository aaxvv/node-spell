package eu.aaxvv.node_spell.spell.graph.runtime;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.node_widget.Widget;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellDeserializationContext;
import eu.aaxvv.node_spell.spell.graph.Nodes;
import eu.aaxvv.node_spell.spell.graph.nodes.custom.SubSpellNode;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
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
public class NodeInstance implements InstanceDataContainer {
    /** The node type of this instance */
    private final Node base;

    /** Additional data which must be stored with this instance e.g. the value of a constant node */
    private Object instanceData;

    /** The socket instances of this node */
    private Map<Socket, SocketInstance> socketInstances;

    private int x;
    private int y;

    public NodeInstance(Node base) {
        this.base = base;
        this.instanceData = base.createInstanceData();
        this.socketInstances = new HashMap<>();
        base.getSockets().forEach(s -> this.socketInstances.put(s, s.createInstance(this)));
    }

    public static NodeInstance fromNbt(CompoundTag instanceNbt, SpellDeserializationContext context) {
        ResourceLocation baseNodeResLoc = ResourceLocation.tryParse(instanceNbt.getString("Base"));
        Node baseNode = Nodes.REGISTRY.get(baseNodeResLoc);
        if (baseNode == null) {
            return null;
        }

        NodeInstance instance;
        if (baseNode instanceof SubSpellNode subSpellNode) {
            // special case for sub-spell nodes
            instance = subSpellNode.fromNbt(instanceNbt, context);
        } else {
            instance = baseNode.createInstance();
            instance.deserialize(instanceNbt, context);
        }

        return instance;
    }

    @Override
    public Object getInstanceData() {
        return instanceData;
    }

    @Override
    public void setInstanceData(Object data) {
        if (!this.getInstanceData().getClass().isAssignableFrom(data.getClass())) {
            throw new IllegalArgumentException("Node instance data type does not match widget type.");
        }

        this.instanceData = data;
    }

    public Node getBaseNode() {
        return base;
    }

    public Widget<?> createWidget() {
        return this.getBaseNode().createWidget(this);
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

    public void setSocketInvalid(Socket socket) {
        SocketInstance instance = this.socketInstances.get(socket);
        instance.setCurrentValue(null);
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

    public Collection<SocketInstance> getSocketInstancesSorted() {
        return this.getBaseNode().getSockets().stream().map(this::getSocketInstance).toList();
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
        int inputX = x;
        int outputX = x + this.base.getWidth() - 1;
        return direction == Socket.Direction.IN ? inputX : outputX;
    }

    public int getSocketY(int index) {
        int socketStartY = y + this.base.getStyle().getSocketYOffset();
        return socketStartY + (index * ModConstants.Sizing.SOCKET_STEP_Y);
    }

    public int getLocalSocketX(Socket.Direction direction) {
        return direction == Socket.Direction.IN ? 0 : this.base.getWidth() - 1;
    }

    public int getLocalSocketY(int index) {
        return this.base.getStyle().getSocketYOffset() + (index * ModConstants.Sizing.SOCKET_STEP_Y);
    }

    public boolean containsPoint(int x, int y) {
        return x >= this.x && x < this.x + this.base.getMinWidth() && y >= this.y && y < this.y + this.base.getExpectedHeight();
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

    public void deserialize(CompoundTag nodeTag, SpellDeserializationContext context) {
        this.x = nodeTag.getInt("X");
        this.y = nodeTag.getInt("Y");
        if (nodeTag.contains("Data", Tag.TAG_COMPOUND)) {
            this.instanceData = this.base.deserializeInstanceData(nodeTag.getCompound("Data"), context);
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

    public NodeInstance copy() {
        NodeInstance copy = this.base.createInstance();
        if (this.getInstanceData() != null) {
            copy.setInstanceData(this.getInstanceData());
        }
        copy.setPosition(this.getX(), this.getY());
        return copy;
    }

    public void refreshSocketInstances() {
        Map<Socket, SocketInstance> prevInstances = this.socketInstances;
        this.socketInstances = new HashMap<>();

        for (Socket socket : this.base.getSockets()) {
            SocketInstance instance = prevInstances.remove(socket);

            if (instance == null) {
                this.socketInstances.put(socket, socket.createInstance(this));
            }
        }

        for (SocketInstance remaining : prevInstances.values()) {
            if (remaining.getBase().getDataType() == Datatype.FLOW) {
                // flow sockets are not represented on the actual spell object, but added afterwards by the pseudo node
                for (Socket baseSocket : this.base.getSockets()) {
                    if (baseSocket.getSerializationHash() == remaining.getSerializationHash()) {
                        remaining.setBase(baseSocket);
                        this.socketInstances.put(baseSocket, remaining);
                    }
                }
            }

            remaining.disconnectAll();
        }
    }

    @Override
    public String toString() {
        return "NodeInstance[" + this.getBaseNode().getDisplayName().getString() + "]";
    }
}
