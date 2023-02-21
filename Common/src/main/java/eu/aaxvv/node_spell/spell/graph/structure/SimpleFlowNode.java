package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.resources.ResourceLocation;

/**
 * A FlowNode which only has one input and one output flow node.
 */
public abstract class SimpleFlowNode extends FlowNode {
    public final Socket fIn;
    public final Socket fOut;

    public SimpleFlowNode(String name, NodeCategory category, ResourceLocation resourceLocation) {
        super(name, category, resourceLocation);
        this.fIn = addInputSocket(Datatype.FLOW, "");
        this.fOut = addOutputSocket(Datatype.FLOW, "");
    }

    @Override
    public Socket getFlowContinueSocket(SpellContext ctx, NodeInstance instance) {
        return this.fOut;
    }
}
