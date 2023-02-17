package eu.aaxvv.node_spell.spell.graph.nodes;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class AddNode extends Node<Void> {
    public final Socket sA;
    public final Socket sB;
    public final Socket sResult;

    public AddNode() {
        super("Add", "Arithmetic");
        this.sA = addInputSocket(Datatype.NUMBER, "a");
        this.sB = addInputSocket(Datatype.NUMBER, "b");
        this.sResult = addOutputSocket(Datatype.NUMBER, "Result");
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        double valueA = instance.getSocketValue(this.sA, ctx).numberValue();
        double valueB = instance.getSocketValue(this.sA, ctx).numberValue();

        instance.setSocketValue(this.sResult, Value.createNumber(valueA + valueB));
    }
}
