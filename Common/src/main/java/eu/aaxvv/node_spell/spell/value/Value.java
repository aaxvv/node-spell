package eu.aaxvv.node_spell.spell.value;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


/**
 * An immutable dynamic value used by spell nodes to pass data around.
 */
public class Value {
    private final Datatype datatype;
    private final Object value;
    private static final NumberFormat format = new DecimalFormat("#0.##", DecimalFormatSymbols.getInstance(Locale.US));

    public Value(Datatype datatype, Object value) {
        this.datatype = datatype;
        this.value = value;
    }

    public Datatype getDatatype() {
        return datatype;
    }

    public boolean isOfType(Datatype type) {
        return this.datatype == type;
    }

    // ==== value getters ====

    public Optional<?> tryGetType(Datatype type) {
        if (this.datatype == type) {
            return Optional.of(this.value);
        } else {
            return Optional.empty();
        }
    }

    public Boolean boolValue() {
        if (this.datatype == Datatype.BOOL) {
            return (Boolean)this.value;
        } else {
            throw new IllegalStateException("Requested type does not match actual type: " + this.datatype);
        }
    }

    public Double numberValue() {
        if (this.datatype == Datatype.NUMBER) {
            return (Double) this.value;
        } else {
            throw new IllegalStateException("Requested type does not match actual type: " + this.datatype);
        }
    }

    public String stringValue() {
        if (this.datatype == Datatype.STRING) {
            return (String)this.value;
        } else {
            throw new IllegalStateException("Requested type does not match actual type: " + this.datatype);
        }
    }

    public Vec3 vectorValue() {
        if (this.datatype == Datatype.VECTOR) {
            return (Vec3)this.value;
        } else {
            throw new IllegalStateException("Requested type does not match actual type: " + this.datatype);
        }
    }

    public Entity entityValue() {
        if (this.datatype == Datatype.ENTITY) {
            return (Entity)this.value;
        } else {
            throw new IllegalStateException("Requested type does not match actual type: " + this.datatype);
        }
    }

    public ItemStack itemValue() {
        if (this.datatype == Datatype.ITEM) {
            return (ItemStack) this.value;
        } else {
            throw new IllegalStateException("Requested type does not match actual type: " + this.datatype);
        }
    }

    public Block blockValue() {
        if (this.datatype == Datatype.BLOCK) {
            return (Block) this.value;
        } else {
            throw new IllegalStateException("Requested type does not match actual type: " + this.datatype);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Value> listValue() {
        if (this.datatype == Datatype.LIST) {
            return (List<Value>) this.value;
        } else {
            throw new IllegalStateException("Requested type does not match actual type: " + this.datatype);
        }
    }

    // ==== value constructors ====

    public static Value createBool(boolean value) {
        return new Value(Datatype.BOOL, value);
    }

    public static Value createNumber(double value) {
        return new Value(Datatype.NUMBER, value);
    }

    public static Value createString(String value) {
        return new Value(Datatype.STRING, value);
    }

    public static Value createVector(Vec3 value) {
        return new Value(Datatype.VECTOR, value);
    }

    public static Value createEntity(Entity value) {
        return new Value(Datatype.ENTITY, value);
    }

    public static Value createBlock(Block value) {
        return new Value(Datatype.BLOCK, value);
    }

    public static Value createItem(ItemStack value) {
        return new Value(Datatype.ITEM, value);
    }

    public static Value createList(List<Value> value) {
        return new Value(Datatype.LIST, value);
    }

    @Override
    public String toString() {
        return switch (this.getDatatype()) {
            case BOOL -> this.boolValue().toString();
            case NUMBER -> format.format(this.numberValue());
            case STRING -> this.stringValue();
            case VECTOR -> "(" + format.format(this.vectorValue().x) + ", " + format.format(this.vectorValue().y) + ", " + format.format(this.vectorValue().z) + ")";
            case ENTITY -> this.entityValue().getDisplayName().toString();
            case BLOCK -> this.blockValue().getName().toString();
            case ITEM -> this.itemValue().toString();
            case LIST -> "[" + String.join(", ", this.listValue().stream().map(Value::toString).toList()) + "]";
            case FLOW -> "<FLOW>";
            case ANY -> "<ANY>";
        };
    }
}
