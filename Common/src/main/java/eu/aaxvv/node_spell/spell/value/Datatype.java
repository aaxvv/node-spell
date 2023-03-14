package eu.aaxvv.node_spell.spell.value;


import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.util.ColorUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.function.Supplier;

public enum Datatype {
    BOOL(ModConstants.Colors.RED, () -> Value.createBool(false)),
    NUMBER(ModConstants.Colors.LIGHT_BLUE, () -> Value.createNumber(0.0)),
    STRING(ModConstants.Colors.PINK, () -> Value.createString("")),
    VECTOR(ModConstants.Colors.ORANGE, () -> Value.createVector(Vec3.ZERO)),
    ENTITY(ModConstants.Colors.DARK_BLUE, () -> Value.createEntity(null)),
    BLOCK(ModConstants.Colors.GREEN, () -> Value.createBlock(Blocks.AIR.defaultBlockState())),
    ITEM(ModConstants.Colors.PURPLE, () -> Value.createItem(ItemStack.EMPTY)),
    LIST(ModConstants.Colors.YELLOW, () -> Value.createList(Collections.emptyList())),
    FLOW(ModConstants.Colors.DARK_GREY, () -> null),
    ANY(ModConstants.Colors.LIGHT_GREY, () -> null);

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
        if ((this == Datatype.FLOW || other == Datatype.FLOW) && this != other) {
            return false;
        }

        if (this == Datatype.ANY || other == Datatype.ANY) {
            return true;
        }

        return this == other;
    }

}
