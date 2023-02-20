package eu.aaxvv.node_spell.spell.graph;

import eu.aaxvv.node_spell.platform.Services;
import eu.aaxvv.node_spell.spell.graph.nodes.AddNode;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import net.minecraft.core.Registry;

public class NodeRegistry {
    public static final Registry<Node> INSTANCE = Services.PLATFORM.createNodeRegistry();

    public static void registerNodes() {
        register(new AddNode());
    }

    private static void register(Node node) {
        Registry.register(INSTANCE, node.getResourceLocation(), node);
    }
}
