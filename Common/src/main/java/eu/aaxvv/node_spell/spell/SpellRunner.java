package eu.aaxvv.node_spell.spell;

import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.FlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Node;

import java.util.Optional;

public class SpellRunner {
    protected SpellGraph graph;
    protected SpellContext ctx;

    public void run() {
        NodeInstance entrypoint = this.graph.getEntrypoint();
        this.runFromNode(entrypoint);
    }

    protected void runFromNode(NodeInstance instance) {
        NodeInstance currentInstance = instance;
        while (currentInstance != null) {
            Node baseNode = currentInstance.getBaseNode();

            if (baseNode instanceof FlowNode flowNode) {
                SpellRunner subRunner = flowNode.getSubRunner(this.ctx, currentInstance);

                if (subRunner != null) {
                    subRunner.run();
                } else {
                    currentInstance.run(this.ctx);
                }

                Optional<NodeInstance> nextInstance = flowNode.getNextInstanceInFlow(this.ctx, currentInstance);
                currentInstance = nextInstance.orElse(null);
            } else {
                throw new IllegalStateException("Cannot execute non-flow node.");
            }

        }
    }
}
