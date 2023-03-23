package eu.aaxvv.node_spell.spell.graph.nodes.sub_spell;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class IsSubSpellNode extends Node {
    public final Socket sOut;

    public IsSubSpellNode() {
        super(NodeCategories.SUB_SPELL, ModConstants.resLoc("is_sub_spell"));
        this.sOut = addOutputSocket(Datatype.BOOL, "socket.node_spell.bool");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        boolean isSubSpell = ctx.getCurrentRunner().getAsSubSpell() != null;
        instance.setSocketValue(this.sOut, Value.createBool(isSubSpell));
    }
}
