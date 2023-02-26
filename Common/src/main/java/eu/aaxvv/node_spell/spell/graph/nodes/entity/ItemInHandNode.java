package eu.aaxvv.node_spell.spell.graph.nodes.entity;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.item.ModItems;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemInHandNode extends Node {
    public final Socket sEntity;
    public final Socket sItem;

    public ItemInHandNode() {
        super(NodeCategories.ENTITY, ModConstants.resLoc("entity_item_in_hand"));
        this.sEntity = addInputSocket(Datatype.ENTITY, "socket.node_spell.entity");
        this.sItem = addOutputSocket(Datatype.ITEM, "socket.node_spell.item");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Entity entity = instance.getSocketValue(sEntity, ctx).entityValue();
        if (!(entity instanceof Player player)) {
            instance.setSocketValue(sItem, Value.createItem(ItemStack.EMPTY));
            return;
        }

        ItemStack mainHandStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHandStack = player.getItemInHand(InteractionHand.OFF_HAND);

        if (mainHandStack.is(ModItems.WAND)) {
            instance.setSocketValue(sItem, Value.createItem(offHandStack));
        } else {
            instance.setSocketValue(sItem, Value.createItem(mainHandStack));
        }
    }
}
