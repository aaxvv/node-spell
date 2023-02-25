package eu.aaxvv.node_spell.spell.graph.nodes.flow;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.FlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;

public class EntryPointNode extends FlowNode {
    public final Socket fOut;

    public EntryPointNode() {
        super("Entrypoint", NodeCategories.FLOW, ModConstants.resLoc("entrypoint"));
        this.fOut = addOutputSocket(Datatype.FLOW, "Start");
    }

    @Override
    public Socket getFlowContinueSocket(SpellContext ctx, NodeInstance instance) {
        return this.fOut;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {

    }
}
