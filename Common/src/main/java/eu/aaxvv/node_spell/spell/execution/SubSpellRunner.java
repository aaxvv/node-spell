package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.nodes.sub_spell.SubSpellOutputNode;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SubSpellRunner extends SpellRunner {
    private final NodeInstance parentInstance;
    private final Map<NodeInstance, SocketInstance> socketMappings;

    public SubSpellRunner(SpellGraph graph, NodeInstance parentInstance, SpellContext ctx) {
        super(graph, ctx);
        this.parentInstance = parentInstance;
        this.socketMappings = new HashMap<>();

        for (Map.Entry<NodeInstance, Socket> entry : graph.getExternalSockets().entrySet()) {
            this.socketMappings.put(entry.getKey(), this.parentInstance.getSocketInstance(entry.getValue()));
        }
    }

    @Override
    public void stop() {
        for (NodeInstance instance : getInnerSocketNodes()) {
            if (instance.getBaseNode() instanceof SubSpellOutputNode output) {
                output.run(this.ctx, instance);
            }
        }
        super.stop();
    }

    public NodeInstance getParentInstance() {
        return parentInstance;
    }

    public SocketInstance getSocketInstance(NodeInstance internalNode) {
        return this.socketMappings.get(internalNode);
    }

    public Set<NodeInstance> getInnerSocketNodes() {
        return this.socketMappings.keySet();
    }

    @Override
    public SubSpellRunner getAsSubSpell() {
        return this;
    }

    public void calculateOutputsOnce() {
        stop();
    }
}
