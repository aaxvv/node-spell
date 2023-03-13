package eu.aaxvv.node_spell.spell.graph.nodes.action;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellExecutionException;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.SimpleFlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class PlaceBlockNode extends SimpleFlowNode {
    public final Socket sPos;
    public final Socket sBlock;

    public PlaceBlockNode() {
        super(NodeCategories.ACTION, ModConstants.resLoc("place_block"));
        this.sPos = addInputSocket(Datatype.VECTOR, "socket.node_spell.pos");
        this.sBlock = addInputSocket(Datatype.BLOCK, "socket.node_spell.block");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Container inventory = ctx.getCaster().container;
        BlockState blockState = instance.getSocketValue(this.sBlock, ctx).blockValue();
        Item blockItem = blockState.getBlock().asItem();

        if (blockItem == Items.AIR) {
            // TODO: display error ?
            return;
        }

        ItemStack usedStack = getUsedItem(inventory, blockItem);

        if (usedStack.isEmpty()) {
            throw new SpellExecutionException("Missing item");
        }

        boolean isCreative = ctx.getCaster().asPlayer().map(p -> p.getAbilities().instabuild).orElse(false);
        if (!isCreative) {
            usedStack.shrink(1);
        }
        Vec3 pos = instance.getSocketValue(this.sPos, ctx).vectorValue();;
        BlockPos blockPos = new BlockPos(pos.x, pos.y, pos.z);
        if (!ctx.getLevel().isLoaded(blockPos)) {
            throw new SpellExecutionException("Target position is not loaded");
        }

        if (!ctx.getLevel().getBlockState(blockPos).canBeReplaced()) {
            return;
        }

        int distance = ctx.getCaster().blockPos.distManhattan(new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        if (distance > ModConstants.SPELL_INTERACTION_RANGE) {
            throw new SpellExecutionException("Target position is out of range");
        }

        ctx.getLevel().setBlock(blockPos, blockState, Block.UPDATE_ALL);
    }

    public ItemStack getUsedItem(Container container, Item item) {
        for(int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
            if (stack.is(item)) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }
}
