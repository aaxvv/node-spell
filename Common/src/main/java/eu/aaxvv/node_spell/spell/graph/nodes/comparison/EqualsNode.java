package eu.aaxvv.node_spell.spell.graph.nodes.comparison;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class EqualsNode extends Node {
    public final Socket sA;
    public final Socket sB;
    public final Socket sResult;

    public EqualsNode() {
        super("Equals", NodeCategories.COMPARISON, ModConstants.resLoc("equals"));
        this.sA = addInputSocket(Datatype.ANY, "a");
        this.sB = addInputSocket(Datatype.ANY, "b");
        this.sResult = addOutputSocket(Datatype.BOOL, "Result");
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Value valueA = instance.getSocketValue(this.sA, ctx);
        Value valueB = instance.getSocketValue(this.sA, ctx);

        instance.setSocketValue(this.sResult, Value.createBool(valueA.equals(valueB)));
    }
}
