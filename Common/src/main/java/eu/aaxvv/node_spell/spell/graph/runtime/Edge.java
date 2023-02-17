package eu.aaxvv.node_spell.spell.graph.runtime;

import eu.aaxvv.node_spell.spell.value.Datatype;

public class Edge {
    private final SocketInstance startSocket;
    private final SocketInstance endSocket;
    private final Datatype datatype;

    private Edge(SocketInstance startSocket, SocketInstance endSocket) {
        this.startSocket = startSocket;
        this.endSocket = endSocket;

        if (startSocket.getBase().getDataType() != endSocket.getBase().getDataType()) {
            throw new IllegalArgumentException("Start and end data types of edge do not match.");
        }
        this.datatype = startSocket.getBase().getDataType();

        this.startSocket.addConnection(this);
        this.endSocket.addConnection(this);
    }

    public SocketInstance getStart() {
        return startSocket;
    }

    public SocketInstance getEnd() {
        return endSocket;
    }

    public Datatype getDatatype() {
        return datatype;
    }

    public SocketInstance getOpposite(SocketInstance of) {
        if (of == this.startSocket) {
            return endSocket;
        } else if (of == this.endSocket) {
            return startSocket;
        } else {
            throw new IllegalArgumentException("Provided socket is not part of this connection.");
        }
    }

    public static Edge create(SocketInstance start, SocketInstance end) {
        return new Edge(start, end);
    }

    public void remove() {
        this.startSocket.removeConnection(this);
        this.endSocket.removeConnection(this);
    }
}
