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

    private void addSocket(Socket socket) {
        this.sockets.add(socket);
    }

    public final List<Socket> getSockets() {
        return Collections.unmodifiableList(this.sockets);
    }

    protected final Socket addInputSocket(Datatype datatype, String name) {
        Socket socket = new Socket(datatype, name, this, Socket.Direction.IN);
        addSocket(socket);
        return socket;
    }

    protected final Socket addOutputSocket(Datatype datatype, String name) {
        Socket socket = new Socket(datatype, name, this, Socket.Direction.OUT);
        addSocket(socket);
        return socket;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Node(String name, String category) {
        this.name = name;
        this.category = category;
        this.sockets = new ArrayList<>();
    }

    public abstract I createInstanceData();

    public void draw() {
        // box
        // title bar + name
        // sockets + names
    }

    //TODO: these nodes should be singletons in a registry

    public abstract void run(SpellContext ctx, NodeInstance instance);
}
