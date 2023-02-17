package eu.aaxvv.node_spell.spell.graph.runtime;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NodeInstance {
    private final Node<Object> base;
    private final Object instanceData;
    private final Map<Socket, SocketInstance> socketInstances;

    private int x;
    private int y;

    public NodeInstance(Node<Object> base) {
        this.base = base;
        this.instanceData = base.createInstanceData();
        this.socketInstances = new HashMap<>();
        base.getSockets().forEach(s -> this.socketInstances.put(s, s.createInstance(this)));
    }

    public Object getInstanceData() {
        return instanceData;
    }

    public Node<Object> getBaseNode() {
        return base;
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

    public void run(SpellContext ctx) {
        this.base.run(ctx, this);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setPosition(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
