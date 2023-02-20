package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.SpellRunner;
import eu.aaxvv.node_spell.spell.graph.nodes.NodeCategory;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/**
 * A type of node which participates in spell "flow" i.e. has sockets for control flow of the program.
 * @see SimpleFlowNode
 */
public abstract class FlowNode extends Node {

    public FlowNode(String name, NodeCategory category, ResourceLocation resourceLocation) {
        super(name, category, resourceLocation);
    }

    /**
     * Returns the flow socket from which spell execution should continue after running this node
     * <p>
     * If more precise control over the execution of following nodes is required (e.g. in a loop) use {@link FlowNode#getSubRunner(SpellContext, NodeInstance)}.
     * @param ctx The spell context
     * @param instance The current node instance
     * @return the flow socket to continue from
     */
    public abstract Socket getFlowContinueSocket(SpellContext ctx, NodeInstance instance);

    public Optional<NodeInstance> getNextInstanceInFlow(SpellContext ctx, NodeInstance instance) {
        Socket flowOutSocket = getFlowContinueSocket(ctx, instance);
        return instance.getSingleSocketPartner(flowOutSocket);
    }

    /**
     * If this method returns a SubRunner, it is fully executed before continuing via {@link FlowNode#getFlowContinueSocket(SpellContext, NodeInstance)}.
     *
     * @param ctx The spell context
     * @param instance The current node instance
     * @return A SpellRunner or {@code null} if execution should continue normally
     */
    public SpellRunner getSubRunner(SpellContext ctx, NodeInstance instance) {
        return null;
    }
}
