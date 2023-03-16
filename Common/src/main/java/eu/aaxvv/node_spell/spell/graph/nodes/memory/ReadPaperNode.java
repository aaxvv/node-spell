package eu.aaxvv.node_spell.spell.graph.nodes.memory;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.item.ModItems;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellExecutionException;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ReadPaperNode extends Node {
    public final Socket sPaper;
    public final Socket sValue;
    public ReadPaperNode() {
        super(NodeCategories.MEMORY, ModConstants.resLoc("read_paper"));
        this.sPaper = addInputSocket(Datatype.ITEM, "socket.node_spell.paper");
        this.sValue = addOutputSocket(Datatype.ANY, "socket.node_spell.value");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        ItemStack paperStack = instance.getSocketValue(this.sPaper, ctx).itemValue();

        if (paperStack == null || !paperStack.is(ModItems.WRITTEN_PAPER) || paperStack.isEmpty()) {
            throw new SpellExecutionException(Component.translatable("error.node_spell.item_not_written_paper"));
        }

        if (!paperStack.getOrCreateTag().contains("Value", Tag.TAG_COMPOUND)) {
            throw new SpellExecutionException(Component.translatable("error.node_spell.paper_no_value"));
        }

        CompoundTag valueTag = paperStack.getOrCreateTag().getCompound("Value");
        Value value = Value.fromNbt(valueTag);
        instance.setSocketValue(this.sValue, value);
    }
}
