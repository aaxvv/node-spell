package eu.aaxvv.node_spell.spell.value;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.helper.EntityHelper;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellExecutionException;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;


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
            throw new SpellExecutionException("Expected value of type BOOL but got: " + this.datatype);
        }
    }

    public Double numberValue() {
        if (this.datatype == Datatype.NUMBER) {
            return (Double) this.value;
        } else {
            throw new SpellExecutionException("Expected value of type NUMBER but got: " + this.datatype);
        }
    }

    public String stringValue() {
        if (this.datatype == Datatype.STRING) {
            return (String)this.value;
        } else {
            throw new SpellExecutionException("Expected value of type STRING but got: " + this.datatype);
        }
    }

    public Vec3 vectorValue() {
        if (this.datatype == Datatype.VECTOR) {
            return (Vec3)this.value;
        } else {
            throw new SpellExecutionException("Expected value of type VECTOR but got: " + this.datatype);
        }
    }

    public UUID entityValue() {
        if (this.datatype == Datatype.ENTITY) {
            return (UUID) this.value;
        } else {
            throw new SpellExecutionException("Expected value of type ENTITY but got: " + this.datatype);
        }
    }

    public ItemStack itemValue() {
        if (this.datatype == Datatype.ITEM) {
            return (ItemStack) this.value;
        } else {
            throw new SpellExecutionException("Expected value of type ITEM but got: " + this.datatype);
        }
    }

    public Block blockValue() {
        if (this.datatype == Datatype.BLOCK) {
            return (Block) this.value;
        } else {
            throw new SpellExecutionException("Expected value of type BLOCK but got: " + this.datatype);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Value> listValue() {
        if (this.datatype == Datatype.LIST) {
            return (List<Value>) this.value;
        } else {
            throw new SpellExecutionException("Expected value of type LIST but got: " + this.datatype);
        }
    }

    public Object uncheckedValue() {
        return this.value;
    }

    // ==== value constructors ====
    @SuppressWarnings("unchecked")
    public static Value create(Datatype type, Object value) {
        return switch (type) {
            case BOOL -> Value.createBool((Boolean) value);
            case NUMBER -> Value.createNumber((Double) value);
            case STRING -> Value.createString((String) value);
            case VECTOR -> Value.createVector((Vec3) value);
            case ENTITY -> Value.createEntity((UUID) value);
            case BLOCK -> Value.createBlock((Block) value);
            case ITEM -> Value.createItem((ItemStack) value);
            case LIST -> Value.createList((List<Value>) value);
            case FLOW -> throw new IllegalArgumentException("Cannot create value of type flow.");
            case ANY -> throw new IllegalArgumentException("Cannot create value of type any.");
        };
    }

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

    public static Value createEntity(UUID value) {
        return new Value(Datatype.ENTITY, value);
    }

    public static Value createBlock(Block value) {
        return new Value(Datatype.BLOCK, value);
    }

    public static Value createItem(ItemStack value) {
        return new Value(Datatype.ITEM, value.copy());
    }

    public static Value createList(List<Value> value) {
        return new Value(Datatype.LIST, value);
    }

    public String toString(SpellContext ctx) {
        return switch (this.getDatatype()) {
            case BOOL -> this.boolValue().toString();
            case NUMBER -> format.format(this.numberValue());
            case STRING -> this.stringValue();
            case VECTOR -> "(" + format.format(this.vectorValue().x) + ", " + format.format(this.vectorValue().y) + ", " + format.format(this.vectorValue().z) + ")";
            case ENTITY -> EntityHelper.getFromUuid(ctx.getLevel(), this.entityValue()).map(Entity::getDisplayName).map(Component::getString).orElseGet(() -> this.entityValue().toString());
            case BLOCK -> this.blockValue().getName().getString();
            case ITEM -> this.itemValue().toString();
            case LIST -> "[" + String.join(", ", this.listValue().stream().map(Value::toString).toList()) + "]";
            case FLOW -> "<FLOW>";
            case ANY -> "<ANY>";
        };
    }

    /**
     * @deprecated Use {@link Value#toString(SpellContext)} instead
     */
    @Override
    @Deprecated
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Value otherValue)) {
            return false;
        }

        if (otherValue.getDatatype() != this.getDatatype()) {
            return false;
        }

        return Objects.equals(this.value, otherValue.value);
    }

    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("Type", this.datatype.name());

        switch (this.datatype) {

            case BOOL -> {
                nbt.putBoolean("Val", (Boolean) this.value);
            }
            case NUMBER -> {
                nbt.putDouble("Val", (Double) this.value);
            }
            case STRING -> {
                nbt.putString("Val", (String) this.value);
            }
            case VECTOR -> {
                Vec3 vec = ((Vec3) this.value);
                ListTag vecList = new ListTag();
                vecList.add(DoubleTag.valueOf(vec.x));
                vecList.add(DoubleTag.valueOf(vec.y));
                vecList.add(DoubleTag.valueOf(vec.z));
                nbt.put("Val", vecList);
            }
            case ENTITY -> {
                IntArrayTag uuidTag = NbtUtils.createUUID(((Entity) this.value).getUUID());
                nbt.put("Val", uuidTag);
            }
            case BLOCK -> {
                ResourceLocation blockResLoc = BuiltInRegistries.BLOCK.getKey(((Block) this.value));
                nbt.putString("Val", blockResLoc.toString());
            }
            case ITEM -> {
                CompoundTag itemTag = new CompoundTag();
                ((ItemStack) this.value).save(itemTag);
                nbt.put("Val", itemTag);
            }
            case LIST -> {
                @SuppressWarnings("unchecked")
                List<Value> list = ((List<Value>) this.value);
                ListTag nbtList = new ListTag();
                list.forEach(val -> {
                    nbtList.add(val.toNbt());
                });
                nbt.put("Val", nbtList);
            }
            case FLOW -> {
                throw new IllegalArgumentException("Cannot serialize value of type flow.");
            }
            case ANY -> {
                throw new IllegalArgumentException("Cannot serialize value of type any.");
            }
        }

        return nbt;
    }

    public static Value fromNbt(CompoundTag nbt) {
        String typeName = nbt.getString("Type");
        Datatype type;
        try {
            type = Datatype.valueOf(typeName);
        } catch (IllegalArgumentException ex) {
            ModConstants.LOG.error("Failed to deserialize value.", ex);
            return null;
        }

        try {
            return switch (type) {
                case BOOL -> Value.createBool(nbt.getBoolean("Val"));
                case NUMBER -> Value.createNumber(nbt.getDouble("Val"));
                case STRING -> Value.createString(nbt.getString("Val"));
                case VECTOR -> {
                    ListTag components = nbt.getList("Val", Tag.TAG_DOUBLE);
                    Vec3 vec = new Vec3(((DoubleTag) components.get(0)).getAsDouble(), ((DoubleTag) components.get(1)).getAsDouble(), ((DoubleTag) components.get(2)).getAsDouble());
                    yield Value.createVector(vec);
                }
                case ENTITY -> {
                    Tag uuidTag = Objects.requireNonNull(nbt.get("Val"));
                    yield Value.createEntity(NbtUtils.loadUUID(uuidTag));
                }
                case BLOCK -> {
                    ResourceLocation blockResLoc = ResourceLocation.tryParse(nbt.getString("Val"));
                    yield Value.createBlock(BuiltInRegistries.BLOCK.get(blockResLoc));
                }
                case ITEM -> Value.createItem(ItemStack.of(nbt.getCompound("Val")));
                case LIST -> {
                    ListTag nbtList = nbt.getList("Val", Tag.TAG_COMPOUND);
                    List<Value> list = new ArrayList<>();
                    nbtList.forEach(tag -> list.add(Value.fromNbt((CompoundTag) tag)));
                    yield Value.createList(list);
                }
                case FLOW -> throw new IllegalArgumentException("Cannot deserialize value of type flow.");
                case ANY -> throw new IllegalArgumentException("Cannot deserialize value of type any.");
            };

        } catch (IllegalArgumentException | ClassCastException | NullPointerException | ResourceLocationException ex) {
            ModConstants.LOG.error("Failed to deserialize value.", ex);
            return null;
        }
    }
}
