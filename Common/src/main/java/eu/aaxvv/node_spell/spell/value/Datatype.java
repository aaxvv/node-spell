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
    BOOL(0.8f, 0.2f, 0.2f),
    NUMBER(0.2f, 0.6f, 0.8f),
    STRING(0.8f, 0.2f, 0.5f),
    VECTOR(0.8f, 0.8f, 0.2f),
    ENTITY(0.2f, 0.2f, 0.8f),
    BLOCK(0.2f, 0.8f, 0.2f),
    ITEM(0.8f, 0.2f, 0.8f),
    LIST(0.8f, 0.6f, 0.2f),
    FLOW(0.3f, 0.3f, 0.3f),
    ANY(0.7f, 0.7f, 0.7f);

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

}
