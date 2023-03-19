package eu.aaxvv.node_spell.spell.graph.nodes.comparison;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class InRangeNode extends Node {
    public final Socket sVal;
    public final Socket sLow;
    public final Socket sHigh;
    public final Socket sOut;
    public InRangeNode() {
        super(NodeCategories.COMPARISON, ModConstants.resLoc("in_range"));
        this.sVal = addInputSocket(Datatype.NUMBER, "socket.node_spell.val");
        this.sLow = addInputSocket(Datatype.NUMBER, "socket.node_spell.min");
        this.sHigh = addInputSocket(Datatype.NUMBER, "socket.node_spell.max");
        this.sOut = addOutputSocket(Datatype.BOOL, "socket.node_spell.bool");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        double low = instance.getSocketValue(this.sLow, ctx).numberValue();
        double high = instance.getSocketValue(this.sHigh, ctx).numberValue();
        double value = instance.getSocketValue(this.sVal, ctx).numberValue();

        boolean result = (low <= value) && (value <= high);
        instance.setSocketValue(this.sOut, Value.createBool(result));
    }
}
