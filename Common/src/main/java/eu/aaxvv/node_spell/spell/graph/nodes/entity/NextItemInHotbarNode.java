package eu.aaxvv.node_spell.spell.graph.nodes.entity;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.helper.InventoryHelper;
import eu.aaxvv.node_spell.item.ModItems;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class NextItemInHotbarNode extends Node {
    public final Socket sEntity;
    public final Socket sItem;

    public NextItemInHotbarNode() {
        super(NodeCategories.ENTITY, ModConstants.resLoc("next_item_in_hotbar"));
        this.sEntity = addInputSocket(Datatype.ENTITY, "socket.node_spell.entity");
        this.sItem = addOutputSocket(Datatype.ITEM, "socket.node_spell.item");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Entity entity = ctx.getEntityOrThrow(instance.getSocketValue(this.sEntity, ctx).entityValue());

        if (!(entity instanceof ServerPlayer player)) {
            instance.setSocketValue(sItem, Value.createItem(ItemStack.EMPTY));
            return;
        }

        int wandSlot = InventoryHelper.findStackSlotInInventory(player, stack -> stack.is(ModItems.WAND));
        ItemStack foundStack;
        if (!Inventory.isHotbarSlot(wandSlot)) {
            foundStack = InventoryHelper.firstStackAfterSlot(player, -1);
        } else {
            foundStack = InventoryHelper.firstStackAfterSlot(player, wandSlot);
        }

        instance.setSocketValue(sItem, Value.createItem(foundStack));
    }

//    @Override
//    public int getMinWidth() {
//        return (int) (super.getMinWidth() * 1.3);
//    }
}
