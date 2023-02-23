package eu.aaxvv.node_spell.spell.graph;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.platform.Services;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.core.Registry;

public class NodeCategories {
    public static final Registry<NodeCategory> REGISTRY = Services.PLATFORM.createNodeCategoryRegistry();

    public static final NodeCategory INPUT = new NodeCategory(ModConstants.resLoc("input"), 0, Datatype.BLOCK.packedColor);
    public static final NodeCategory FLOW = new NodeCategory(ModConstants.resLoc("flow"), 100, Datatype.ANY.packedColor);
    public static final NodeCategory MATH = new NodeCategory(ModConstants.resLoc("math"), 200, Datatype.NUMBER.packedColor);
    public static final NodeCategory COMPARISON = new NodeCategory(ModConstants.resLoc("comparison"), 300, Datatype.NUMBER.packedColor);
    public static final NodeCategory LOGIC = new NodeCategory(ModConstants.resLoc("logic"), 400, Datatype.NUMBER.packedColor);
    public static final NodeCategory VECTOR = new NodeCategory(ModConstants.resLoc("vector"), 500, Datatype.VECTOR.packedColor);

    public static final NodeCategory ACTION = new NodeCategory(ModConstants.resLoc("action"), 600, Datatype.BOOL.packedColor);
    public static final NodeCategory MEMORY = new NodeCategory(ModConstants.resLoc("memory"), 700, Datatype.LIST.packedColor);
    public static final NodeCategory LIST = new NodeCategory(ModConstants.resLoc("list"), 800, Datatype.VECTOR.packedColor);
    public static final NodeCategory STRING = new NodeCategory(ModConstants.resLoc("string"), 900, Datatype.VECTOR.packedColor);
    public static final NodeCategory CUSTOM = new NodeCategory(ModConstants.resLoc("custom"), 1000, Datatype.FLOW.packedColor);

    public static void registerCategories() {
        register(INPUT);
        register(FLOW);
        register(MATH);
        register(COMPARISON);
        register(LOGIC);
        register(VECTOR);

        register(ACTION);
        register(MEMORY);
        register(LIST);
        register(STRING);
        register(CUSTOM);
    }

    private static void register(NodeCategory category) {
        Registry.register(REGISTRY, category.resourceLocation, category);
    }
}
