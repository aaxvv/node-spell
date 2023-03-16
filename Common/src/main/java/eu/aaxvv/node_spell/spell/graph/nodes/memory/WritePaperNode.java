package eu.aaxvv.node_spell.spell.graph.nodes.memory;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.helper.InventoryHelper;
import eu.aaxvv.node_spell.item.ModItems;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellExecutionException;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.SimpleFlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WritePaperNode extends SimpleFlowNode {
    public final Socket sHolder;
    public final Socket sValue;

    public WritePaperNode() {
        super(NodeCategories.MEMORY, ModConstants.resLoc("write_paper"));
        this.sValue = addInputSocket(Datatype.ANY, "socket.node_spell.value");
        this.sHolder = addInputSocket(Datatype.ENTITY, "socket.node_spell.holder");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Entity entity = ctx.getEntityOrThrow(instance.getSocketValue(this.sHolder, ctx).entityValue());
        Value value = instance.getSocketValue(this.sValue, ctx);

        ItemStack paperStack = null;

        if (entity instanceof ItemEntity itemEntity) {
            paperStack = itemEntity.getItem();
        } else if (entity instanceof ServerPlayer player) {
            paperStack = InventoryHelper.findStackInInventory(player, stack -> stack.is(Items.PAPER));
        } else {
            throw new SpellExecutionException(Component.translatable("error.node_spell.invalid_holder"));
        }

        if (paperStack == null || !paperStack.is(Items.PAPER) || paperStack.isEmpty()) {
            throw new SpellExecutionException(Component.translatable("error.node_spell.item_not_paper"));
        }

        ItemStack writtenStack = new ItemStack(ModItems.WRITTEN_PAPER);
        writtenStack.getOrCreateTag().put("Value", value.toNbt());

        if (entity instanceof ItemEntity itemEntity) {
            itemEntity.getItem().shrink(1);
            ItemEntity newEntity = new ItemEntity(ctx.getLevel(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), writtenStack, 0, 0, 0);
            ctx.getLevel().addFreshEntity(newEntity);

            if (itemEntity.getItem().isEmpty()) {
                itemEntity.remove(Entity.RemovalReason.DISCARDED);
            }
        } else {
            ServerPlayer player = (ServerPlayer) entity;
            paperStack.shrink(1);
            player.getInventory().placeItemBackInInventory(writtenStack);
        }
    }
}
