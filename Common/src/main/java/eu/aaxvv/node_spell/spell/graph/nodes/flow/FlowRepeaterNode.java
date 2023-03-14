package eu.aaxvv.node_spell.spell.graph.nodes.flow;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.FlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.NodeStyle;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;

public class FlowRepeaterNode extends FlowNode {
    public final Socket sIn;
    public final Socket sOut;

    public FlowRepeaterNode() {
        super(NodeCategories.FLOW, ModConstants.resLoc("repeat_flow"));
        setStyle(new NodeStyle(ModConstants.Colors.WHITE, false));
        this.sIn = addInputSocket(Datatype.FLOW, "socket.node_spell.empty");
        this.sOut = addOutputSocket(Datatype.FLOW, "socket.node_spell.empty");
    }

    @Override
    public Socket getFlowContinueSocket(SpellContext ctx, NodeInstance instance) {
        return this.sOut;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {

    }

    @Override
    public int getWidth() {
        return 12;
    }

    @Override
    public int getExpectedHeight() {
        return 9;
    }
}
