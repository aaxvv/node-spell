package eu.aaxvv.node_spell.spell.value;


import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.util.ColorUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.function.Supplier;

public enum Datatype {
    BOOL(ModConstants.Colors.RED,           "bool", () -> Value.createBool(false)),
    NUMBER(ModConstants.Colors.LIGHT_BLUE,  "number", () -> Value.createNumber(0.0)),
    STRING(ModConstants.Colors.PINK,        "string", () -> Value.createString("")),
    VECTOR(ModConstants.Colors.ORANGE,      "vector", () -> Value.createVector(Vec3.ZERO)),
    ENTITY(ModConstants.Colors.DARK_BLUE,   "entity", () -> Value.createEntity(null)),
    BLOCK(ModConstants.Colors.GREEN,        "block", () -> Value.createBlock(Blocks.AIR.defaultBlockState())),
    ITEM(ModConstants.Colors.PURPLE,        "item", () -> Value.createItem(ItemStack.EMPTY)),
    LIST(ModConstants.Colors.YELLOW,        "list", () -> Value.createList(Collections.emptyList())),
    FLOW(ModConstants.Colors.DARK_GREY,     "flow", () -> null),
    ANY(ModConstants.Colors.LIGHT_GREY,     "any", () -> null);

    Datatype(int packedColor, String translation, Supplier<Value> defaultValue) {
        this.packedColor = packedColor;
        this.defaultValue = defaultValue;
        float[] components = new float[4];
        ColorUtil.unpackColor(packedColor, components);
        this.r = components[1];
        this.g = components[2];
        this.b = components[3];
        this.translationKey = "datatype.node_spell." + translation;
    }

    public final float r;
    public final float g;
    public final float b;
    public final int packedColor;
    public final Supplier<Value> defaultValue;
    public final String translationKey;

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
