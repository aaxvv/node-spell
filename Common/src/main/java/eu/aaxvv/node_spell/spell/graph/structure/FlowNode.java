package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.SpellRunner;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;

import java.util.Optional;

public abstract class FlowNode<I> extends Node<I> {

    public FlowNode(String name, String category) {
        super(name, category);
    }

    public abstract Socket getFlowContinueSocket(SpellContext ctx, NodeInstance instance);

    public Optional<NodeInstance> getNextInstanceInFlow(SpellContext ctx, NodeInstance instance) {
        Socket flowOutSocket = getFlowContinueSocket(ctx, instance);
        return instance.getSingleSocketPartner(flowOutSocket);
    }

    public SpellRunner getSubRunner(SpellContext ctx, NodeInstance instance) {
        return null;
    }
}
