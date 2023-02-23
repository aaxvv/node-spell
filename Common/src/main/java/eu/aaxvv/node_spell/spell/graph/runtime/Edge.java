package eu.aaxvv.node_spell.spell.graph.runtime;

import eu.aaxvv.node_spell.spell.graph.structure.Socket;
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

        if (!typesCompatible(startSocket, endSocket)) {
            throw new IllegalArgumentException("Incompatible start and end sockets.");
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
        if (this.endSocket == null) {
            return this.startSocket.getBase().getDataType();
        }

        if (this.startSocket.getBase().getDirection() == Socket.Direction.IN) {
            return this.endSocket.getBase().getDataType();
        } else {
            return this.startSocket.getBase().getDataType();
        }
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

    public static boolean typesCompatible(SocketInstance start, SocketInstance end) {
        if (start.getBase().getDirection() == end.getBase().getDirection()) {
            return false;
        }

        if (start.getBase().getDirection() == Socket.Direction.IN) {
            return start.getBase().getDataType().isAssignableFrom(end.getBase().getDataType());
        } else {
            return end.getBase().getDataType().isAssignableFrom(start.getBase().getDataType());
        }
    }
}
