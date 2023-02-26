package eu.aaxvv.node_spell.spell.graph.nodes.action;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.SimpleFlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class DebugPrintNode extends SimpleFlowNode {
    public final Socket sVal;
    public DebugPrintNode() {
        super(NodeCategories.ACTION, ModConstants.resLoc("print"));
        this.sVal = addInputSocket(Datatype.ANY, "socket.node_spell.value");
    }

    @Override
    public Object createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        if (ctx.getCaster() instanceof ServerPlayer player) {
           Value value = instance.getSocketValue(sVal, ctx);
           player.displayClientMessage(Component.literal(String.valueOf(value)), true);
        }
    }
}
