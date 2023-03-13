package eu.aaxvv.node_spell.spell.graph.nodes.input;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class RandomNode extends Node {
    public final Socket sOut;
    public RandomNode() {
        super(NodeCategories.INPUT, ModConstants.resLoc("random"));
        this.sOut = addOutputSocket(Datatype.NUMBER, "socket.node_spell.value");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        double value = ctx.getLevel().getRandom().nextDouble();
        instance.setSocketValue(this.sOut, Value.createNumber(value));
    }
}
