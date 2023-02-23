package eu.aaxvv.node_spell.spell.value;


import eu.aaxvv.node_spell.util.ColorUtil;

/**
 * Color scheme:
 * Bool - Red
 * Number - Turquoise
 * String - Magenta
 * Vector - Yellow
 * Entity - Blue
 * Block - Green
 * Item - Purple
 * List - Orange
 * Flow - Dark Grey
 * ANY - Light Grey
 */
public enum Datatype {
    BOOL(0xFFCC3333),
    NUMBER(0xFF3399CC),
    STRING(0xFFCC33A0),
    VECTOR(0xFFCCC633),
    ENTITY(0xFF3333CC),
    BLOCK(0xFF33CC33),
    ITEM(0xFF8535CC),
    LIST(0xFFCC8828),
    FLOW(0xFF444444),
    ANY(0xFF999999);

    Datatype(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.packedColor = ColorUtil.packColor(r, g, b, 1);
    }

    Datatype(int packedColor) {
        this.packedColor = packedColor;
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

    public boolean isAssignableFrom(Datatype other) {
        if (this == Datatype.ANY) {
            return true;
        }

        return this == other;
    }

}
