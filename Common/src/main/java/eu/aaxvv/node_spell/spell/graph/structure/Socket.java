package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.network.chat.Component;

import java.util.Objects;

/**
 * A connection on the side of a node.
 * <p>
 * This class does not represent an instance of a socket, it only holds a description of a socket which will appear on specific node type.
 */
public class Socket {
    private final Datatype dataType;
    private String translationKey;
    private Component displayName;
    private final Direction direction;
    private int positionOnNode;

    public Socket(Datatype dataType, String translationKey, Direction direction, int positionOnNode) {
        this.dataType = dataType;
        this.translationKey = translationKey;
        this.direction = direction;
        this.positionOnNode = positionOnNode;
        this.displayName = Component.translatable(this.translationKey);
    }

    public Socket(Datatype dataType, String translationKey, Direction direction, int positionOnNode, boolean isLiteralName) {
        this.dataType = dataType;
        this.translationKey = translationKey;
        this.direction = direction;
        this.positionOnNode = positionOnNode;
        this.displayName = isLiteralName ? Component.literal(translationKey) : Component.translatable(this.translationKey);
    }

    public SocketInstance createInstance(NodeInstance parentInstance) {
        return new SocketInstance(this, parentInstance);
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setTranslationKey(String translationKey, boolean isLiteralName) {
        this.translationKey = translationKey;
        this.displayName = isLiteralName ? Component.literal(translationKey) : Component.translatable(this.translationKey);
    }

    public String getTranslationKey() {
        return translationKey;
    }
    public Component getDisplayName() {
        return this.displayName;
    }

    public Datatype getDataType() {
        return dataType;
    }

    public int getPositionOnNode() {
        return positionOnNode;
    }

    public void setPositionOnNode(int positionOnNode) {
        this.positionOnNode = positionOnNode;
    }

    public enum Direction {
        IN,
        OUT;

        public boolean isIn() {
            return this == IN;
        }

        public boolean isOut() {
            return this == OUT;
        }

        public Direction getOpposite() {
            if (this == IN) {
                return OUT;
            } else {
                return IN;
            }
        }
    }

    public int getSerializationHash() {
        return Socket.calculateSerializationHash(this.getDataType(), this.getDirection(), this.getTranslationKey());
    }

    public static int calculateSerializationHash(Datatype type, Direction direction, String translationKey) {
        int typeIdx = type.ordinal();
        int directionIdx = direction.ordinal();
        int nameHash = translationKey.hashCode();
        return Objects.hash(nameHash, typeIdx, directionIdx);
    }

    @Override
    public String toString() {
        return Component.translatable(this.translationKey) + "[" + this.dataType + ", " + this.direction + "]";
    }
}
