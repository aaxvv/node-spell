package eu.aaxvv.node_spell.spell.graph;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.platform.registry.PlatformRegistryWrapper;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;

public class NodeCategories {
    public static PlatformRegistryWrapper<NodeCategory> REGISTRY;

    public static final NodeCategory INPUT = new NodeCategory(ModConstants.resLoc("input"), 0, ModConstants.Colors.RED);
    public static final NodeCategory FLOW = new NodeCategory(ModConstants.resLoc("spell_flow"), 100, ModConstants.Colors.LIGHT_GREY);
    public static final NodeCategory DATA_FLOW = new NodeCategory(ModConstants.resLoc("data_flow"), 150, ModConstants.Colors.LIGHT_GREY);
    public static final NodeCategory ACTION = new NodeCategory(ModConstants.resLoc("action"), 200, ModConstants.Colors.LIGHT_BLUE);

    public static final NodeCategory MATH = new NodeCategory(ModConstants.resLoc("math"), 300, ModConstants.Colors.GREEN);
    public static final NodeCategory VECTOR = new NodeCategory(ModConstants.resLoc("vector"), 400, ModConstants.Colors.GREEN);
    public static final NodeCategory COMPARISON = new NodeCategory(ModConstants.resLoc("comparison"), 500, ModConstants.Colors.GREEN);
    public static final NodeCategory LOGIC = new NodeCategory(ModConstants.resLoc("logic"), 600, ModConstants.Colors.GREEN);

    public static final NodeCategory ENTITY = new NodeCategory(ModConstants.resLoc("entity"), 700, ModConstants.Colors.GREEN);
    public static final NodeCategory BLOCK = new NodeCategory(ModConstants.resLoc("block"), 800, ModConstants.Colors.GREEN);
    public static final NodeCategory ITEM = new NodeCategory(ModConstants.resLoc("item"), 900, ModConstants.Colors.GREEN);

    public static final NodeCategory MEMORY = new NodeCategory(ModConstants.resLoc("memory"), 1000, ModConstants.Colors.RED);
    public static final NodeCategory LIST = new NodeCategory(ModConstants.resLoc("list"), 1100, ModConstants.Colors.GREEN);
    public static final NodeCategory STRING = new NodeCategory(ModConstants.resLoc("string"), 1200, ModConstants.Colors.GREEN);
    public static final NodeCategory CUSTOM = new NodeCategory(ModConstants.resLoc("custom"), 1300, ModConstants.Colors.DARK_GREY);

    public static void initRegistry(PlatformRegistryWrapper<NodeCategory> categoryRegistry) {
        REGISTRY = categoryRegistry;
    }

    public static void registerCategories() {
        ModConstants.LOG.info("Registering node categories.");
        register(INPUT);
        register(FLOW);
        register(DATA_FLOW);
        register(ACTION);

        register(MATH);
        register(VECTOR);
        register(COMPARISON);
        register(LOGIC);

        register(ENTITY);
        register(BLOCK);
        register(ITEM);

        register(MEMORY);
        register(LIST);
        register(STRING);
        register(CUSTOM);
    }

    private static void register(NodeCategory category) {
        REGISTRY.register(category.resourceLocation, category);
    }
}
