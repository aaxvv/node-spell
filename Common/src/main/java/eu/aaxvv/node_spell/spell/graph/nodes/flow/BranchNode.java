package eu.aaxvv.node_spell.spell.graph.nodes.flow;

import eu.aaxvv.node_spell.spell.SpellContext;
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
        super("Branch", "Flow");
        this.fIn = addInputSocket(Datatype.FLOW, "");
        this.sValue = addInputSocket(Datatype.BOOL, "Value");
        this.fTrueOut = addInputSocket(Datatype.FLOW, "True");
        this.fFalseOut = addInputSocket(Datatype.FLOW, "False");
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
}
