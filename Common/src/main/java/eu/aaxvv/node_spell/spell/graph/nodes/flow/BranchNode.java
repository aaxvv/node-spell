package eu.aaxvv.node_spell.spell.graph.nodes.flow;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.FlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;

public class BranchNode extends FlowNode {
    public final Socket fIn;

    public final Socket sValue;
    public final Socket fTrueOut;
    public final Socket fFalseOut;

    public BranchNode() {
        super(NodeCategories.FLOW, ModConstants.resLoc("branch"));
        this.fIn = addInputSocket(Datatype.FLOW, "socket.node_spell.empty");
        this.sValue = addInputSocket(Datatype.BOOL, "socket.node_spell.value");
        this.fTrueOut = addOutputSocket(Datatype.FLOW, "socket.node_spell.true");
        this.fFalseOut = addOutputSocket(Datatype.FLOW, "socket.node_spell.false");
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {

    }

    @Override
    public Socket getFlowContinueSocket(SpellContext ctx, NodeInstance instance) {
        if (instance.getSocketValue(this.sValue, ctx).boolValue()) {
            return this.fTrueOut;
        } else {
            return this.fFalseOut;
        }
    }

    @Override
    public int getWidth() {
        return (int)(super.getWidth() * 1.5);
    }
}
