package eu.aaxvv.node_spell.spell.graph.runtime;

import eu.aaxvv.node_spell.client.widget.NodeConstants;
import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Value;

import java.util.*;

public class NodeInstance {
    /** The node type of this instance */
    private final Node base;

    /** Additional data which must be stored with this instance e.g. the value of a constant node */
    private final Object instanceData;

    /** The socket instances of this node */
    private final Map<Socket, SocketInstance> socketInstances;

    private int x;
    private int y;
    private boolean isDragged;  // not serialized

    public NodeInstance(Node base) {
        this.base = base;
        this.instanceData = base.createInstanceData();
        this.socketInstances = new HashMap<>();
        this.isDragged = false;
        base.getSockets().forEach(s -> this.socketInstances.put(s, s.createInstance(this)));
    }

    public Object getInstanceData() {
        return instanceData;
    }

    public Node getBaseNode() {
        return base;
    }

    public boolean isDragged() {
        return isDragged;
    }

    public void setDragged(boolean dragged) {
        isDragged = dragged;
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
        SocketInstance instance =  this.socketInstances.get(socket);
        if (instance == null) {
            throw new IllegalArgumentException("Socket does not exist on this instance.");
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
        int outputX = x + NodeConstants.DEFAULT_NODE_WIDTH - 3;
        return direction == Socket.Direction.IN ? inputX : outputX;
    }

    public int getSocketY(int index) {
        int socketStartY = y + NodeConstants.SOCKET_START_Y;
        return socketStartY + (index * NodeConstants.SOCKET_STEP_Y);
    }
}
