package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Node<I> {
    private final List<Socket> sockets;
    private final String name;
    private final String category;
    private final List<Socket> sockets;
    private int inSocketCount;
    private int outSocketCount;

    public Node(String name, String category) {
        this.name = name;
        this.category = category;
        this.sockets = new ArrayList<>();
        this.inSocketCount = 0;
        this.outSocketCount = 0;
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

    public String getCategory() {
        return category;
    }

    public int getExpectedHeight() {
        return NodeConstants.SOCKET_START_Y + Math.max(this.inSocketCount, this.outSocketCount) * NodeConstants.SOCKET_STEP_Y;
    }

    public abstract Object createInstanceData();

    public abstract void run(SpellContext ctx, NodeInstance instance);
}
