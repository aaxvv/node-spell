package eu.aaxvv.node_spell.spell.graph.nodes.math;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class MapRangeNode extends Node {
    public final Socket sLowIn;
    public final Socket sHighIn;
    public final Socket sLowOut;
    public final Socket sHighOut;
    public final Socket sValIn;
    public final Socket sOut;

    public MapRangeNode() {
        super(NodeCategories.MATH, ModConstants.resLoc("map_range"));
        this.sLowIn = addInputSocket(Datatype.NUMBER, "socket.node_spell.low_in");
        this.sHighIn = addInputSocket(Datatype.NUMBER, "socket.node_spell.high_in");
        this.sLowOut = addInputSocket(Datatype.NUMBER, "socket.node_spell.low_out");
        this.sHighOut = addInputSocket(Datatype.NUMBER, "socket.node_spell.high_out");
        this.sValIn = addInputSocket(Datatype.NUMBER, "socket.node_spell.value");

        this.sOut = addOutputSocket(Datatype.NUMBER, "socket.node_spell.result");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        double val = instance.getSocketValue(this.sValIn, ctx).numberValue();
        double lowIn = instance.getSocketValue(this.sLowIn, ctx).numberValue();
        double highIn = instance.getSocketValue(this.sHighIn, ctx).numberValue();

        double t = (val - lowIn) / (highIn - lowIn);

        double lowOut = instance.getSocketValue(this.sLowOut, ctx).numberValue();
        double highOut = instance.getSocketValue(this.sHighOut, ctx).numberValue();

        double result = lowOut + (t * (highOut - lowOut));

        instance.setSocketValue(this.sOut, Value.createNumber(result));
    }

    @Override
    public int getWidth() {
        return (int) (super.getWidth() * 1.25f);
    }
}
