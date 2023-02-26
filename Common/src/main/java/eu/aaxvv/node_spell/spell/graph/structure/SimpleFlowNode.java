package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.resources.ResourceLocation;

/**
 * A FlowNode which only has one input and one output flow node.
 */
public abstract class SimpleFlowNode extends FlowNode {
    public final Socket fIn;
    public final Socket fOut;

    public SimpleFlowNode(NodeCategory category, ResourceLocation resourceLocation) {
        super(category, resourceLocation);
        this.fIn = addInputSocket(Datatype.FLOW, "");
        this.fOut = addOutputSocket(Datatype.FLOW, "");
    }

    public SimpleFlowNode(String translationKey, NodeCategory category, ResourceLocation resourceLocation) {
        super(translationKey, category, resourceLocation);
        this.fIn = addInputSocket(Datatype.FLOW, "");
        this.fOut = addOutputSocket(Datatype.FLOW, "");
    }

    @Override
    public Socket getFlowContinueSocket(SpellContext ctx, NodeInstance instance) {
        return this.fOut;
    }
}
