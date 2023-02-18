package eu.aaxvv.node_spell.spell.value;


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

    }

    public final float r;
    public final float g;
    public final float b;

}
