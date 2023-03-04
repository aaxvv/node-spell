package eu.aaxvv.node_spell.spell.graph.nodes.string;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class ToStringNode extends Node {
    public final Socket sIn;
    public final Socket sOut;
    public ToStringNode() {
        super(NodeCategories.STRING, ModConstants.resLoc("to_string"));
        this.sIn = addInputSocket(Datatype.STRING, "socket.node_spell.value");
        this.sOut = addOutputSocket(Datatype.STRING, "socket.node_spell.string");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Value inputValue = instance.getSocketValue(sIn, ctx);
        String stringRepr = inputValue.toString(ctx);
        instance.setSocketValue(sOut, Value.createString(stringRepr));
    }
}
