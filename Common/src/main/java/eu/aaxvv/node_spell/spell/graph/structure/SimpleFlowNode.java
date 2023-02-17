package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;

public abstract class SimpleFlowNode<I> extends FlowNode<I> {
    public final Socket fIn;
    public final Socket fOut;

    public SimpleFlowNode(String name, String category) {
        super(name, category);
        this.fIn = addInputSocket(Datatype.FLOW, "");
        this.fOut = addOutputSocket(Datatype.FLOW, "");
    }

    @Override
    public Socket getFlowContinueSocket(SpellContext ctx, NodeInstance instance) {
        return this.fOut;
    }
}
