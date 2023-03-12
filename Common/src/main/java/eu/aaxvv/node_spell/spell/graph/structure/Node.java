package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.node_widget.Widget;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a type of node which can be used in a spell graph.
 * <p>
 * Like the vanilla Block class these Nodes are singletons and don't represent a specific node instance,
 * but the general layout and behavior of this type of node.
 *
 * @see NodeInstance
 */
public abstract class Node {
    private final String translationKey;
    private final NodeCategory category;
    private final List<Socket> sockets;
    private int inSocketCount;
    private int outSocketCount;
    private final ResourceLocation resourceLocation;

    public Node(NodeCategory category, ResourceLocation resourceLocation) {
        this.translationKey = resourceLocation.toLanguageKey("node");
        this.category = category;
        this.sockets = new ArrayList<>();
        this.inSocketCount = 0;
        this.outSocketCount = 0;
        this.resourceLocation = resourceLocation;
    }

    public Node(String translationKey, NodeCategory category, ResourceLocation resourceLocation) {
        this.translationKey = translationKey;
        this.category = category;
        this.sockets = new ArrayList<>();
        this.inSocketCount = 0;
        this.outSocketCount = 0;
        this.resourceLocation = resourceLocation;
    }

    private void addSocket(Socket socket) {
        for (Socket existingSocket : this.sockets) {
            if (existingSocket.getSerializationHash() == socket.getSerializationHash()) {
                throw new RuntimeException(String.format("All node sockets must be differentiable by hash. (Conflict: %s / %s)", socket, existingSocket));
            }
        }
        this.sockets.add(socket);
    }

    public List<Socket> getSockets() {
        return Collections.unmodifiableList(this.sockets);
    }

    protected final Socket addInputSocket(Datatype datatype, String translationKey) {
        Socket socket = new Socket(datatype, translationKey, this, Socket.Direction.IN, this.inSocketCount);
        addSocket(socket);
        this.inSocketCount++;
        return socket;
    }

    protected final Socket addOutputSocket(Datatype datatype, String translationKey) {
        Socket socket = new Socket(datatype, translationKey, this, Socket.Direction.OUT, this.outSocketCount);
        addSocket(socket);
        this.outSocketCount++;
        return socket;
    }

    public Component getDisplayName() {
        return Component.translatable(this.translationKey);
    }

    public NodeInstance createInstance() {
        return new NodeInstance(this);
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public NodeCategory getCategory() {
        return category;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public boolean hasSideEffects() {
        return false;
    }

    public int getExecutionDelay() {
        return 1;
    }

    public int getExpectedHeight() {
        return ModConstants.Sizing.SOCKET_START_Y + Math.max(this.inSocketCount, this.outSocketCount) * ModConstants.Sizing.SOCKET_STEP_Y - (ModConstants.Sizing.SOCKET_STEP_Y / 4);
    }

    public int getWidth() {
        return ModConstants.Sizing.DEFAULT_NODE_WIDTH;
    }

    public Object createInstanceData() {
        return null;
    }

    public Widget<?> createWidget(NodeInstance instance) {
        return null;
    }

    public abstract void run(SpellContext ctx, NodeInstance instance);

    public void serializeInstanceData(Object instanceData, CompoundTag dataTag) {
    }

    public Object deserializeInstanceData(CompoundTag dataTag) {
        return null;
    }
}
