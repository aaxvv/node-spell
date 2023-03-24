package eu.aaxvv.node_spell.spell.graph.runtime;

import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellExecutionException;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a specific instance of a node socket.
 * <p>
 * This is used to store connections between node instances, as well as values of nodes during execution.
 */
public class SocketInstance {
    private Socket base;
    private final List<Edge> connections;
    private final NodeInstance parentInstance;
    private Value currentValue;

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

    public void setBase(Socket base) {
        this.base = base;
    }

    public void addConnection(Edge edge) {
        if (this.acceptsSingleConnection()) {
            if (this.connections.size() > 0) {
                this.connections.get(0).remove();
            }
            this.connections.clear();
            this.connections.add(edge);
        } else {
            this.connections.add(edge);
        }
    }

    public boolean acceptsSingleConnection() {
        return this.getBase().getDataType() == Datatype.FLOW ? this.getBase().getDirection().isOut() : this.getBase().getDirection().isIn();
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
            throw new IllegalStateException("Tried to get single connection on empty socket.");
        }
        return this.connections.get(0);
    }

    public Value getComputedValue(SpellContext ctx) {
        if (this.base.getDirection().isIn()) {
            if (this.connections.isEmpty()) {
                return this.base.getDataType().defaultValue.get();
            }

            return getSingleConnection().getOpposite(this).getComputedValue(ctx);
        } else {
            if (!this.parentInstance.getBaseNode().hasSideEffects()) {
                try {
                    this.parentInstance.run(ctx);
                } catch (SpellExecutionException ex) {
                    ex.addNodeContext(this.parentInstance);
                    throw ex;
                }
            }

            if (this.currentValue != null) {
                return this.currentValue;
            } else {
                SpellExecutionException ex = new SpellExecutionException(Component.translatable("error.node_spell.invalid_socket_access"));
                ex.addNodeContext(this.parentInstance);
                throw ex;
            }
        }
    }

    public int getX() {
        return this.getParentInstance().getSocketX(this.base.getDirection());
    }

    public int getY() {
        return this.getParentInstance().getSocketY(this.base.getPositionOnNode());
    }

    public int getLocalX() {
        return this.getParentInstance().getLocalSocketX(this.base.getDirection());
    }

    public int getLocalY() {
        return this.getParentInstance().getLocalSocketY(this.base.getPositionOnNode());
    }

    public boolean containsPoint(int x, int y) {
        return x >= this.getX() && x < this.getX() + 5 && y >= this.getY() && y < this.getY() + 5;
    }

    public void disconnectAll() {
        while (!this.connections.isEmpty()) {
            this.connections.get(0).remove();
        }
    }

    public int getSerializationHash() {
        return this.base.getSerializationHash();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
