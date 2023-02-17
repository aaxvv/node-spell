package eu.aaxvv.node_spell.spell.graph.runtime;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SocketInstance {
    private final Socket base;
    private final List<Edge> connections;
    private final NodeInstance parentInstance;
    private Value currentValue;

    private int x;
    private int y;


    public void setCurrentValue(Value currentValue) {
        this.currentValue = currentValue;
    }

    public SocketInstance(Socket base, NodeInstance parentInstance) {
        this.base = base;
        this.parentInstance = parentInstance;
        this.connections = new ArrayList<>();
    }

    public Socket getBase() {
        return this.base;
    }

    public void addConnection(Edge edge) {
        if (this.base.getDirection() == Socket.Direction.IN) {
            this.connections.forEach(Edge::remove);
            this.connections.clear();
            this.connections.add(edge);
        } else {
            this.connections.add(edge);
        }

    }
    public void removeConnection(Edge edge) {
        if (this.connections.remove(edge)) {
            edge.remove();
        }
    }

    public List<Edge> getConnections() {
        return Collections.unmodifiableList(this.connections);
    }

    public NodeInstance getParentInstance() {
        return parentInstance;
    }

    public Edge getSingleConnection() {
        if (this.connections.isEmpty()) {
            throw new IllegalStateException("Cannot calculate value of socket with missing input.");
        }
        return this.connections.get(0);
    }

    public Value getComputedValue(SpellContext ctx) {
        if (this.base.getDirection() == Socket.Direction.IN) {
            return getSingleConnection().getOpposite(this).getComputedValue(ctx);
        } else {
            this.parentInstance.run(ctx);
            return this.currentValue;
        }
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
