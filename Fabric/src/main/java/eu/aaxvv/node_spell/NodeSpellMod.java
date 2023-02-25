package eu.aaxvv.node_spell;

import eu.aaxvv.node_spell.item.ModItems;
import eu.aaxvv.node_spell.network.FabricPacketHandler;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.Nodes;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class NodeSpellMod implements ModInitializer {
    @Override
    public void onInitialize() {
        createRegistries();
//        NodeSpellCommon.init();
        Nodes.registerNodes();
        NodeCategories.registerCategories();
        FabricPacketHandler.init();
        ModItems.register((resLoc, item) -> Registry.register(BuiltInRegistries.ITEM, resLoc, item));
    }

    private void createRegistries() {
        Registry<Node> nodeRegistry = FabricRegistryBuilder.createSimple(Node.class, ModConstants.resLoc("nodes")).buildAndRegister();
        Nodes.initRegistry(() -> nodeRegistry);

        Registry<NodeCategory> nodeCategoryRegistry = FabricRegistryBuilder.createSimple(NodeCategory.class, ModConstants.resLoc("node_categories")).buildAndRegister();;
        NodeCategories.initRegistry(() -> nodeCategoryRegistry);
    }
}
