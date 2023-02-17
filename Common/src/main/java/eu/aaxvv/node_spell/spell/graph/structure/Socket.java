package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;

public class Socket {
    private final Datatype dataType;
    private final String name;
    private final Node<?> parentNode;
    private final Direction direction;

    public Socket(Datatype dataType, String name, Node<?> parentNode, Direction direction) {
        this.dataType = dataType;
        this.name = name;
        this.parentNode = parentNode;
        this.direction = direction;
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

    public Node<?> getParentNode() {
        return parentNode;
    }

    public Datatype getDataType() {
        return dataType;
    }

    public enum Direction {
        IN,
        OUT
    }
}
