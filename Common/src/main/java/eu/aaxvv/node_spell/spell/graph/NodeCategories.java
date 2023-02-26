package eu.aaxvv.node_spell.spell.graph;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.core.Registry;

import java.util.function.Supplier;

public class NodeCategories {
    public static Supplier<Registry<NodeCategory>> REGISTRY_SUPPLIER;

    public static final NodeCategory INPUT = new NodeCategory(ModConstants.resLoc("input"), 0, Datatype.BLOCK.packedColor);
    public static final NodeCategory FLOW = new NodeCategory(ModConstants.resLoc("flow"), 100, Datatype.ANY.packedColor);
    public static final NodeCategory ACTION = new NodeCategory(ModConstants.resLoc("action"), 200, Datatype.BOOL.packedColor);
    public static final NodeCategory MEMORY = new NodeCategory(ModConstants.resLoc("memory"), 300, Datatype.LIST.packedColor);
    public static final NodeCategory LIST = new NodeCategory(ModConstants.resLoc("list"), 400, Datatype.VECTOR.packedColor);

    public static final NodeCategory MATH = new NodeCategory(ModConstants.resLoc("math"), 500, Datatype.NUMBER.packedColor);
    public static final NodeCategory VECTOR = new NodeCategory(ModConstants.resLoc("vector"), 600, Datatype.NUMBER.packedColor);
    public static final NodeCategory COMPARISON = new NodeCategory(ModConstants.resLoc("comparison"), 700, Datatype.NUMBER.packedColor);
    public static final NodeCategory LOGIC = new NodeCategory(ModConstants.resLoc("logic"), 800, Datatype.NUMBER.packedColor);
    public static final NodeCategory STRING = new NodeCategory(ModConstants.resLoc("string"), 900, Datatype.STRING.packedColor);

    public static final NodeCategory ENTITY = new NodeCategory(ModConstants.resLoc("entity"), 1000, Datatype.VECTOR.packedColor);
    public static final NodeCategory BLOCK = new NodeCategory(ModConstants.resLoc("block"), 1200, Datatype.VECTOR.packedColor);
    public static final NodeCategory ITEM = new NodeCategory(ModConstants.resLoc("item"), 1300, Datatype.VECTOR.packedColor);
    public static final NodeCategory CUSTOM = new NodeCategory(ModConstants.resLoc("custom"), 1400, Datatype.FLOW.packedColor);

    public static void initRegistry(Supplier<Registry<NodeCategory>> nodeRegistry) {
        REGISTRY_SUPPLIER = nodeRegistry;
    }

    public static void registerCategories() {
        ModConstants.LOG.info("Registering node categories.");
        register(INPUT);
        register(FLOW);
        register(ACTION);
        register(MEMORY);
        register(LIST);

        register(MATH);
        register(VECTOR);
        register(COMPARISON);
        register(LOGIC);
        register(STRING);

        register(ENTITY);
        register(BLOCK);
        register(ITEM);
        register(CUSTOM);
    }

    private static void register(NodeCategory category) {
        Registry.register(REGISTRY_SUPPLIER.get(), category.resourceLocation, category);
    }
}
