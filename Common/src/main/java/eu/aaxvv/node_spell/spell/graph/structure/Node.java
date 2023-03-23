package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.node_widget.Widget;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellDeserializationContext;
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
    private final Component displayName;
    private final NodeCategory category;
    private final List<Socket> sockets;
    protected int inSocketCount;
    protected int outSocketCount;
    private final ResourceLocation resourceLocation;
    private NodeStyle style;

    public Node(NodeCategory category, ResourceLocation resourceLocation) {
        this(resourceLocation.toLanguageKey("node"), category, resourceLocation);
    }

    public Node(String translationKey, NodeCategory category, ResourceLocation resourceLocation) {
        this.translationKey = translationKey;
        this.displayName = Component.translatable(translationKey);
        this.category = category;
        this.sockets = new ArrayList<>();
        this.inSocketCount = 0;
        this.outSocketCount = 0;
        this.resourceLocation = resourceLocation;
        this.style = NodeStyle.getDefault();
    }

    protected void addSocket(Socket socket) {
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

    protected void clearSockets() {
        this.sockets.clear();
        this.inSocketCount = 0;
        this.outSocketCount = 0;
    }
    protected final Socket addInputSocket(Datatype datatype, String translationKey) {
        Socket socket = new Socket(datatype, translationKey, Socket.Direction.IN, this.inSocketCount);
        addSocket(socket);
        this.inSocketCount++;
        return socket;
    }

    protected final Socket addOutputSocket(Datatype datatype, String translationKey) {
        Socket socket = new Socket(datatype, translationKey, Socket.Direction.OUT, this.outSocketCount);
        addSocket(socket);
        this.outSocketCount++;
        return socket;
    }

    public Component getDisplayName() {
        return this.displayName;
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

    protected void setStyle(NodeStyle style) {
        this.style = style;
    }

    public NodeStyle getStyle() {
        return style;
    }

    public int getExpectedHeight() {
        return this.style.getSocketYOffset() + Math.max(this.inSocketCount, this.outSocketCount) * ModConstants.Sizing.SOCKET_STEP_Y - 3;
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

    public Object deserializeInstanceData(CompoundTag dataTag, SpellDeserializationContext context) {
        return null;
    }
}
