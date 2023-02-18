package eu.aaxvv.node_spell.spell.graph.nodes;

import eu.aaxvv.node_spell.util.ColorUtil;

public enum NodeCategory {
    INPUT("node_spell.node.category.input", 0.285f, 0.659f, 0.310f),
    FLOW("node_spell.node.category.flow", 0.285f, 0.659f, 0.310f),
    ACTION("node_spell.node.category.action", 0.285f, 0.659f, 0.310f),
    LOGIC("node_spell.node.category.logic", 0.285f, 0.659f, 0.310f),
    COMPARISON("node_spell.node.category.comparison", 0.285f, 0.659f, 0.310f),
    MATH("node_spell.node.category.math", 0.285f, 0.659f, 0.310f),
    MEMORY("node_spell.node.category.memory", 0.285f, 0.659f, 0.310f),
    VECTOR("node_spell.node.category.vector", 0.285f, 0.659f, 0.310f),
    LIST("node_spell.node.category.list", 0.285f, 0.659f, 0.310f),
    STRING("node_spell.node.category.string", 0.285f, 0.659f, 0.310f),
    CUSTOM("node_spell.node.category.custom", 0.285f, 0.659f, 0.310f);

    NodeCategory(String name, float r, float g, float b) {
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
        this.packedColor = ColorUtil.packColor(r,g, b, 1);
    }

    NodeCategory(String name, int packedColor) {
        this.name = name;
        this.packedColor = packedColor;
        float[] components = new float[4];
        ColorUtil.unpackColor(packedColor, components);
        this.r = components[1];
        this.g = components[2];
        this.b = components[3];
    }

    public final String name;
    public final float r;
    public final float g;
    public final float b;
    public final int packedColor;
}
