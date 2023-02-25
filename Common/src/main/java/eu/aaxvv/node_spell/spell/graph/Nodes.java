package eu.aaxvv.node_spell.spell.graph;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.graph.nodes.AddNode;
import eu.aaxvv.node_spell.spell.graph.nodes.action.DebugPrintNode;
import eu.aaxvv.node_spell.spell.graph.nodes.constant.BoolConstantNode;
import eu.aaxvv.node_spell.spell.graph.nodes.constant.NumberConstantNode;
import eu.aaxvv.node_spell.spell.graph.nodes.constant.StringConstantNode;
import eu.aaxvv.node_spell.spell.graph.nodes.flow.BranchNode;
import eu.aaxvv.node_spell.spell.graph.nodes.flow.ForLoopNode;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import net.minecraft.core.Registry;

import java.util.function.Supplier;

public class Nodes {
    public static Supplier<Registry<Node>> REGISTRY_SUPPLIER;

    public static final Node NUMBER_CONSTANT = new NumberConstantNode();
    public static final Node BOOL_CONSTANT = new BoolConstantNode();
    public static final Node STRING_CONSTANT = new StringConstantNode();

    public static final Node ADD = new AddNode();

    public static final Node BRANCH = new BranchNode();
    public static final Node FOR_RANGE = new ForLoopNode();

    public static final Node PRINT = new DebugPrintNode();

    public static void initRegistry(Supplier<Registry<Node>> nodeRegistry) {
        REGISTRY_SUPPLIER = nodeRegistry;
    }

    public static void registerNodes() {
        ModConstants.LOG.info("Registering node types.");
        register(NUMBER_CONSTANT);
        register(BOOL_CONSTANT);
        register(STRING_CONSTANT);

        register(ADD);

        register(BRANCH);
        register(FOR_RANGE);

        register(PRINT);
    }

    private static void register(Node node) {
        Registry.register(REGISTRY_SUPPLIER.get(), node.getResourceLocation(), node);
    }
}
