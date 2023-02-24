package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;

import java.util.Objects;

/**
 * A connection on the side of a node.
 * <p>
 * This class does not represent an instance of a socket, it only holds a description of a socket which will appear on specific node type.
 */
public class Socket {
    private final Datatype dataType;
    private final String name;
    private final Node parentNode;
    private final Direction direction;
    private final int positionOnNode;

    public Socket(Datatype dataType, String name, Node parentNode, Direction direction, int positionOnNode) {
        this.dataType = dataType;
        this.name = name;
        this.parentNode = parentNode;
        this.direction = direction;
        this.positionOnNode = positionOnNode;
    }

    public SocketInstance createInstance(NodeInstance parentInstance) {
        return new SocketInstance(this, parentInstance);
    }

    public Direction getDirection() {
        return this.direction;
    }

    public String getName() {
        return name;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public Datatype getDataType() {
        return dataType;
    }

    public int getPositionOnNode() {
        return positionOnNode;
    }

    public enum Direction {
        IN,
        OUT
    }

    public int getSerializationHash() {
        int typeIdx = this.getDataType().ordinal();
        int direction = this.getDirection().ordinal();
        int nameHash = this.getName().hashCode();
        return Objects.hash(nameHash, typeIdx, direction);
    }

    @Override
    public String toString() {
        return this.name + "[" + this.dataType + ", " + this.direction + "]";
    }
}
