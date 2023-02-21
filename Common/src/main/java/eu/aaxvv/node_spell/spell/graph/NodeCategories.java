package eu.aaxvv.node_spell.spell.graph;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.platform.Services;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import net.minecraft.core.Registry;

public class NodeCategories {
    public static final Registry<NodeCategory> REGISTRY = Services.PLATFORM.createNodeCategoryRegistry();

    public static final NodeCategory INPUT = new NodeCategory(ModConstants.resLoc("input"), 0, 0xFF3EA84F);
    public static final NodeCategory FLOW = new NodeCategory(ModConstants.resLoc("flow"), 100, 0xFF3EA84F);
    public static final NodeCategory ACTION = new NodeCategory(ModConstants.resLoc("action"), 200, 0xFF3EA84F);
    public static final NodeCategory MATH = new NodeCategory(ModConstants.resLoc("math"), 300, 0xFF3B94A5);
    public static final NodeCategory COMPARISON = new NodeCategory(ModConstants.resLoc("comparison"), 400, 0xFF3EA84F);
    public static final NodeCategory LOGIC = new NodeCategory(ModConstants.resLoc("logic"), 500, 0xFF3EA84F);
    public static final NodeCategory MEMORY = new NodeCategory(ModConstants.resLoc("memory"), 600, 0xFF3EA84F);
    public static final NodeCategory VECTOR = new NodeCategory(ModConstants.resLoc("vector"), 700, 0xFF3EA84F);
    public static final NodeCategory LIST = new NodeCategory(ModConstants.resLoc("list"), 800, 0xFF3EA84F);
    public static final NodeCategory STRING = new NodeCategory(ModConstants.resLoc("string"), 900, 0xFF3EA84F);
    public static final NodeCategory CUSTOM = new NodeCategory(ModConstants.resLoc("custom"), 1000, 0xFF3EA84F);

    public static void registerCategories() {
        register(INPUT);
        register(FLOW);
        register(ACTION);
        register(LOGIC);
        register(COMPARISON);
        register(MATH);
        register(MEMORY);
        register(VECTOR);
        register(LIST);
        register(STRING);
        register(CUSTOM);
    }

    private static void register(NodeCategory category) {
        Registry.register(REGISTRY, category.resourceLocation, category);
    }
}
