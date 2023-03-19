package eu.aaxvv.node_spell.spell.graph.nodes.memory;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.item.ModItems;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class IsWrittenPaperNode extends Node {
    public final Socket sItem;
    public final Socket sResult;

    public IsWrittenPaperNode() {
        super(NodeCategories.MEMORY, ModConstants.resLoc("is_written_paper"));
        this.sItem = addInputSocket(Datatype.ITEM, "socket.node_spell.item");
        this.sResult = addOutputSocket(Datatype.BOOL, "socket.node_spell.result");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        ItemStack paperStack = instance.getSocketValue(this.sItem, ctx).itemValue();

        if (paperStack == null || !paperStack.is(ModItems.WRITTEN_PAPER) || paperStack.isEmpty()) {
            instance.setSocketValue(this.sResult, Value.createBool(false));
            return;
        }

        if (!paperStack.getOrCreateTag().contains("Value", Tag.TAG_COMPOUND)) {
            instance.setSocketValue(this.sResult, Value.createBool(false));
            return;
        }

        instance.setSocketValue(this.sResult, Value.createBool(true));
    }

    @Override
    public int getWidth() {
        return (int) (super.getWidth() * 1.2);
    }
}
