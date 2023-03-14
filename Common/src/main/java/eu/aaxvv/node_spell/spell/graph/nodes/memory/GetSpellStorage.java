package eu.aaxvv.node_spell.spell.graph.nodes.memory;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.NodeSpellCommon;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class GetSpellStorage extends Node {
    public final Socket sOut;

    public GetSpellStorage() {
        super(NodeCategories.MEMORY, ModConstants.resLoc("get_spell_storage"));
        sOut = addOutputSocket(Datatype.ANY, "socket.node_spell.value");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Value value = NodeSpellCommon.spellPersistentStorage.getValue(ctx.getCaster().uuid);
        instance.setSocketValue(sOut, value);
    }
}
