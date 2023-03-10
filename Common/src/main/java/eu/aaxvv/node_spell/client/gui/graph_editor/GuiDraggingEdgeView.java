package eu.aaxvv.node_spell.client.gui.graph_editor;

import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;

public class GuiDraggingEdgeView extends GuiEdgeView {
    private final SocketInstance existingSocket;
    private int endpointLocalX;
    private int endpointLocalY;

    public GuiDraggingEdgeView(SocketInstance existingSocket) {
        super(null);
        this.existingSocket = existingSocket;
        this.endpointLocalX = this.existingSocket.getX();
        this.endpointLocalY = this.existingSocket.getY();
    }

    public GuiDraggingEdgeView(SocketInstance existingSocket, Edge previous) {
        super(previous);
        this.existingSocket = existingSocket;
        this.endpointLocalX = previous.getOpposite(existingSocket).getX();
        this.endpointLocalY = previous.getOpposite(existingSocket).getY();
    }

    public Edge complete(SocketInstance other) {
        if (Edge.typesCompatible(this.getExistingSocket(), other)) {
            // create new
            return Edge.create(this.getExistingSocket(), other);
        } else {
            // use existing
            if (this.isNew()) {
                return null;
            } else {
                return this.getInstance();
            }
        }
    }

    public void setLocalEndpoint(int x, int y) {
        this.endpointLocalX = x;
        this.endpointLocalY = y;
        invalidate();
    }

    @Override
    protected void invalidateEndpoints() {
        if (getParent() != null) {
            if (this.getExistingSocket().getBase().getDirection().isOut()) {
                cachedOutX = this.getExistingSocket().getX();
                cachedOutY = this.getExistingSocket().getY();
                cachedInX = this.endpointLocalX;
                cachedInY = this.endpointLocalY;
            } else {
                cachedInX = this.getExistingSocket().getX();
                cachedInY = this.getExistingSocket().getY();
                cachedOutX = this.endpointLocalX;
                cachedOutY = this.endpointLocalY;
            }

            cachedOutX += this.getParent().getGlobalX();
            cachedOutY += this.getParent().getGlobalY();
            cachedInX += this.getParent().getGlobalX();
            cachedInY += this.getParent().getGlobalY();
        }
    }

    public boolean isNew() {
        return this.getInstance() == null;
    }

    public SocketInstance getExistingSocket() {
        return existingSocket;
    }

    @Override
    protected Datatype getDatatype() {
        return this.existingSocket.getBase().getDataType();
    }

    @Override
    public int getWidth() {
        return Math.abs(this.existingSocket.getX() - this.endpointLocalX);
    }

    @Override
    public int getHeight() {
        return Math.abs(this.existingSocket.getY() - this.endpointLocalY);
    }

    @Override
    public int getLocalX() {
        return Math.min(this.existingSocket.getX(), this.endpointLocalX);
    }

    @Override
    public int getLocalY() {
        return Math.min(this.existingSocket.getY(), this.endpointLocalY);
    }
}
