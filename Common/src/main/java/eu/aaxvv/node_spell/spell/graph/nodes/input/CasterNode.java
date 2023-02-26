package eu.aaxvv.node_spell.spell.graph.nodes.input;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class CasterNode extends Node {
    public final Socket sOut;
    public CasterNode() {
        super(NodeCategories.INPUT, ModConstants.resLoc("caster"));
        sOut = addOutputSocket(Datatype.ENTITY, "socket.node_spell.caster");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        instance.setSocketValue(sOut, Value.createEntity(ctx.getCaster()));
    }
}
