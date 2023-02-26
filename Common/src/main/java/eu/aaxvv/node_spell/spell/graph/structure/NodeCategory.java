package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.util.ColorUtil;
import net.minecraft.resources.ResourceLocation;

public class NodeCategory {
    public final ResourceLocation resourceLocation;
    public final String translationKey;
    public final int priority;
    public final int packedColor;
    public final float r;
    public final float g;
    public final float b;

    public NodeCategory(ResourceLocation resourceLocation, int priority, int packedColor) {
        this(resourceLocation, resourceLocation.toLanguageKey("node.category"), priority, packedColor);
    }

    public NodeCategory(ResourceLocation resourceLocation, String translationKey, int priority, int packedColor) {
        this.resourceLocation = resourceLocation;
        this.translationKey = translationKey;

        this.priority = priority;
        this.packedColor = packedColor;

        float[] components = new float[4];
        ColorUtil.unpackColor(packedColor, components);
        this.r = components[1];
        this.g = components[2];
        this.b = components[3];
    }

    public int getPriority() {
        return priority;
    }
}
