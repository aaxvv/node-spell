package eu.aaxvv.node_spell.spell.graph.nodes.flow;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellRunner;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.FlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

import java.util.List;

public class ForLoopNode extends FlowNode {
    public final Socket sStartIdx;
    public final Socket sEndIdx;
    public final Socket sIndex;

    public final Socket fIn;
    public final Socket fComplete;
    public final Socket fRepeat;

    public ForLoopNode() {
        super(NodeCategories.FLOW, ModConstants.resLoc("for_range"));
        this.fIn = addInputSocket(Datatype.FLOW, "socket.node_spell.empty");
        this.fComplete = addOutputSocket(Datatype.FLOW, "socket.node_spell.done");
        this.sStartIdx = addInputSocket(Datatype.NUMBER, "socket.node_spell.start_idx");
        this.sEndIdx = addInputSocket(Datatype.NUMBER, "socket.node_spell.end_idx");
        this.fRepeat = addOutputSocket(Datatype.FLOW, "socket.node_spell.repeat");
        this.sIndex = addOutputSocket(Datatype.NUMBER, "socket.node_spell.index");
    }

    @Override
    public Socket getFlowContinueSocket(SpellContext ctx, NodeInstance instance) {
        return this.fComplete;
    }

    @Override
    public InstanceData createInstanceData() {
        return new InstanceData();
    }

    @Override
    public int getWidth() {
        return (int)(super.getWidth() * 1.5);
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {

    }

    @Override
    public SpellRunner getSubRunner(SpellContext ctx, NodeInstance instance) {
        return new SubRunner(instance, ctx);
    }

    public static class InstanceData {
        public InstanceData() {
            this.index = 0;
        }
        public int index;
    }

    private class SubRunner extends SpellRunner {
        private final NodeInstance nodeInstance;
        public SubRunner(NodeInstance nodeInstance, SpellContext ctx) {
            super(ctx);
            this.nodeInstance = nodeInstance;
        }

        @Override
        public boolean run() {
            List<Edge> loopBodyConnections = nodeInstance.getSocketConnections(ForLoopNode.this.fRepeat);
            if (loopBodyConnections.size() == 0) {
                return true;
            } else if (loopBodyConnections.size() > 1) {
                throw new IllegalStateException("Flow Output cannot have multiple outgoing connections.");
            }
            NodeInstance firstLoopNode = loopBodyConnections.get(0).getOpposite(nodeInstance.getSocketInstance(ForLoopNode.this.fRepeat)).getParentInstance();

            long startIdx = Math.round(nodeInstance.getSocketValue(ForLoopNode.this.sStartIdx, super.ctx).numberValue());
            long endIdx = Math.round(nodeInstance.getSocketValue(ForLoopNode.this.sEndIdx, super.ctx).numberValue());

            for (long i = startIdx; i < endIdx; i++) {
                this.nodeInstance.setSocketValue(ForLoopNode.this.sIndex, Value.createNumber(i));
                this.runFromNode(firstLoopNode);
            }

            return true;
        }
    }
}
