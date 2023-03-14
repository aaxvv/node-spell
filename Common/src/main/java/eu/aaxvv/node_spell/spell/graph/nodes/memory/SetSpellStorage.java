package eu.aaxvv.node_spell.spell.graph.nodes.memory;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.NodeSpellCommon;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.SimpleFlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class SetSpellStorage extends SimpleFlowNode {
    public final Socket sIn;

    public SetSpellStorage() {
        super(NodeCategories.MEMORY, ModConstants.resLoc("set_spell_storage"));
        sIn = addInputSocket(Datatype.ANY, "socket.node_spell.value");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Value value = instance.getSocketValue(sIn, ctx);
        NodeSpellCommon.spellPersistentStorage.setValue(ctx.getCaster().uuid, value);
    }
}
