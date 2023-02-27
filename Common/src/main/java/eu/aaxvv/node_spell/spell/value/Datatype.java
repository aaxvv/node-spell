package eu.aaxvv.node_spell.spell.value;


import eu.aaxvv.node_spell.util.ColorUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.function.Supplier;

/**
 * Color scheme:
 * Bool - Red
 * Number - Turquoise
 * String - Magenta
 * Vector - Orange
 * Entity - Blue
 * Block - Green
 * Item - Purple
 * List - Light Green
 * Flow - Dark Grey
 * ANY - Light Grey
 */
public enum Datatype {
    BOOL(0xFFCC3333, () -> Value.createBool(false)),
    NUMBER(0xFF3399CC, () -> Value.createNumber(0.0)),
    STRING(0xFFCC33A0, () -> Value.createString("")),
    VECTOR(0xFFCC8828, () -> Value.createVector(Vec3.ZERO)),
    ENTITY(0xFF3333CC, () -> Value.createEntity(null)),
    BLOCK(0xFF33CC33, () -> Value.createBlock(Blocks.AIR)),
    ITEM(0xFF8535CC, () -> Value.createItem(ItemStack.EMPTY)),
    LIST(0xFF66CC66, () -> Value.createList(Collections.emptyList())),
    FLOW(0xFF444444, () -> null),
    ANY(0xFF999999, () -> null);

//    Datatype(float r, float g, float b) {
//        this.r = r;
//        this.g = g;
//        this.b = b;
//        this.packedColor = ColorUtil.packColor(r, g, b, 1);
//    }

    Datatype(int packedColor, Supplier<Value> defaultValue) {
        this.packedColor = packedColor;
        this.defaultValue = defaultValue;
        float[] components = new float[4];
        ColorUtil.unpackColor(packedColor, components);
        this.r = components[1];
        this.g = components[2];
        this.b = components[3];
    }

    public final float r;
    public final float g;
    public final float b;
    public final int packedColor;
    public final Supplier<Value> defaultValue;

    public boolean isAssignableFrom(Datatype other) {
        if (this == Datatype.ANY) {
            return true;
        }

        return this == other;
    }

}
