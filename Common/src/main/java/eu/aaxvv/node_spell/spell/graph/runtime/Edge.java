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

        if (!socketsCompatible(startSocket, endSocket)) {
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

    public SocketInstance getInSocket() {
        if (this.startSocket.getBase().getDirection().isIn()) {
            return this.startSocket;
        } else {
            return this.endSocket;
        }
    }

    public SocketInstance getOutSocket() {
        if (this.startSocket.getBase().getDirection().isIn()) {
            return this.endSocket;
        } else {
            return this.startSocket;
        }
    }

    public Datatype getDatatype() {
        if (this.endSocket == null) {
            return this.startSocket.getBase().getDataType();
        }

        // prefer the non-ANY socket if available
        if (this.startSocket.getBase().getDataType() == Datatype.ANY) {
            return this.endSocket.getBase().getDataType();
        } else {
            return this.startSocket.getBase().getDataType();
        }
    }

//    public boolean isIncomplete() {
//        return startSocket == null || endSocket == null;
//    }
//
//    public Edge complete(SocketInstance end) {
//        return new Edge(this.startSocket, end);
//    }

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

//    public static Edge createIncomplete(SocketInstance start) {
//        return new Edge(start, null);
//    }

    public void remove() {
        this.startSocket.removeConnection(this);
        this.endSocket.removeConnection(this);
    }

    public static boolean socketsCompatible(SocketInstance start, SocketInstance end) {
        if (start.getParentInstance() == end.getParentInstance()) {
            return false;
        }

        return typesCompatible(start.getBase(), end.getBase());
    }

    public static boolean typesCompatible(Socket start, Socket end) {
        if (start.getDirection() == end.getDirection()) {
            return false;
        }

        if (start.getDirection() == Socket.Direction.IN) {
            return start.getDataType().isAssignableFrom(end.getDataType());
        } else {
            return end.getDataType().isAssignableFrom(start.getDataType());
        }
    }
}
