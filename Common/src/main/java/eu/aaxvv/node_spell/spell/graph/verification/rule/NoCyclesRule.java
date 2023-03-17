package eu.aaxvv.node_spell.spell.graph.verification.rule;

import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.graph.structure.FlowNode;
import eu.aaxvv.node_spell.spell.graph.verification.VerificationResult;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.network.chat.Component;

import java.util.*;
import java.util.function.Consumer;

public class NoCyclesRule implements VerificationRule {
    @Override
    public void check(SpellGraph graph, Consumer<VerificationResult> resultConsumer) {
        Set<NodeInstance> cycleNodes = new HashSet<>();
        Deque<NodeInstance> dfsStack = new ArrayDeque<>();

        NodeInstance entrypoint = graph.getEntrypoint();
        if (entrypoint == null) {
            return;
        }

        recursiveDfs(entrypoint, dfsStack, true, cycleNodes);

        if (!cycleNodes.isEmpty()) {
            resultConsumer.accept(VerificationResult.error(Component.translatable("gui.node_spell.spell_verification.cycle_found"), cycleNodes.stream().toList()));
        }
    }

    private void recursiveDfs(NodeInstance nextInstance, Deque<NodeInstance> dfsStack, boolean traversingFlow, Set<NodeInstance> cycleNodes) {
        if (dfsStack.contains(nextInstance) && (traversingFlow == (nextInstance.getBaseNode() instanceof FlowNode))) {
            Iterator<NodeInstance> stackIter = dfsStack.descendingIterator();
            while (stackIter.hasNext()) {
                NodeInstance next = stackIter.next();
                cycleNodes.add(next);

                if (next == nextInstance) {
                    break;
                }
            }
            return;
        }

        dfsStack.addLast(nextInstance);

        for (SocketInstance socket : nextInstance.getSocketInstances()) {
            if (traversingFlow) {
                if (socket.getBase().getDirection().isOut() && socket.getBase().getDataType() == Datatype.FLOW) {
                    // continue forward in execution flow
                    for (Edge connected : socket.getConnections()) {
                        recursiveDfs(connected.getOpposite(socket).getParentInstance(), dfsStack, true, cycleNodes);
                    }
                } else if (socket.getBase().getDirection().isIn() && socket.getBase().getDataType() != Datatype.FLOW) {
                    // begin traversing backwards in data flow
                    for (Edge connected : socket.getConnections()) {
                        recursiveDfs(connected.getOpposite(socket).getParentInstance(), dfsStack, false, cycleNodes);
                    }
                }
            } else {
                if (socket.getBase().getDirection().isIn() && socket.getBase().getDataType() != Datatype.FLOW) {
                    // continue backwards in data flow
                    for (Edge connected : socket.getConnections()) {
                        recursiveDfs(connected.getOpposite(socket).getParentInstance(), dfsStack, false, cycleNodes);
                    }
                }
            }
        }

        dfsStack.removeLast();
    }
}
