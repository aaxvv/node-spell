package eu.aaxvv.node_spell.spell.graph.nodes.block_and_item;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class BlockFromItemNode extends Node {
    public final Socket sItem;
    public final Socket sBlock;

    public BlockFromItemNode() {
        super("Item -> Block", NodeCategories.ENTITY, ModConstants.resLoc("entity_block_from_item"));
        this.sItem = addInputSocket(Datatype.ITEM, "Item");
        this.sBlock = addOutputSocket(Datatype.BLOCK, "Block");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        ItemStack itemStack = instance.getSocketValue(sItem, ctx).itemValue();
        if (!(itemStack.getItem() instanceof BlockItem blockItem)) {
            instance.setSocketValue(sBlock, Value.createBlock(Blocks.AIR));
            return;
        }

        Block block = blockItem.getBlock();
        instance.setSocketValue(sBlock, Value.createBlock(block));
    }
}
