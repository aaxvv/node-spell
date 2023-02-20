package eu.aaxvv.node_spell.spell.graph.runtime;

import eu.aaxvv.node_spell.spell.value.Datatype;

public class Edge {
    private final SocketInstance startSocket;
    private final SocketInstance endSocket;

    private Edge(SocketInstance startSocket, SocketInstance endSocket) {
        this.startSocket = startSocket;
        this.endSocket = endSocket;

        if (startSocket == null || endSocket == null) {
            return;
        }

        if (startSocket.getBase().getDataType() != endSocket.getBase().getDataType()) {
            throw new IllegalArgumentException("Start and end data types of edge do not match.");
        }

        if (startSocket.getBase().getDirection() == endSocket.getBase().getDirection()) {
            throw new IllegalArgumentException("Start and end sockets must not have same direction.");
        }

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
        return this.startSocket.getBase().getDataType();
    }

    public boolean isIncomplete() {
        return startSocket == null || endSocket == null;
    }

    public Edge complete(SocketInstance end) {
        return new Edge(this.startSocket, end);
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

    public static Edge createIncomplete(SocketInstance start) {
        return new Edge(start, null);
    }

    public void remove() {
        this.startSocket.removeConnection(this);
        this.endSocket.removeConnection(this);
    }
}
