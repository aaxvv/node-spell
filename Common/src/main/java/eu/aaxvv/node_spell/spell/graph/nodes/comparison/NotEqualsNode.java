package eu.aaxvv.node_spell.spell.graph.nodes.comparison;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class NotEqualsNode extends Node {
    public final Socket sA;
    public final Socket sB;
    public final Socket sResult;

    public NotEqualsNode() {
        super(NodeCategories.COMPARISON, ModConstants.resLoc("not_equal"));
        this.sA = addInputSocket(Datatype.ANY, "socket.node_spell.a");
        this.sB = addInputSocket(Datatype.ANY, "socket.node_spell.b");
        this.sResult = addOutputSocket(Datatype.BOOL, "socket.node_spell.result");
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Value valueA = instance.getSocketValue(this.sA, ctx);
        Value valueB = instance.getSocketValue(this.sB, ctx);

        instance.setSocketValue(this.sResult, Value.createBool(!(valueA.equals(valueB))));
    }
}
