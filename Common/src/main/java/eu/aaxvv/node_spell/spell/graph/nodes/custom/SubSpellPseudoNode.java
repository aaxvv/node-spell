package eu.aaxvv.node_spell.spell.graph.nodes.custom;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.Spell;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.FlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import net.minecraft.network.chat.Component;

import java.util.List;

public class SubSpellPseudoNode extends FlowNode {
    private Spell spell;

    public SubSpellPseudoNode() {
        super(NodeCategories.CUSTOM, ModConstants.resLoc("sub_spell"));
    }

    @Override
    public Socket getFlowContinueSocket(SpellContext ctx, NodeInstance instance) {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {

    }

    @Override
    public boolean hasSideEffects() {
        return super.hasSideEffects();
    }

    @Override
    public Component getDisplayName() {
        return Component.literal(this.spell.getName());
    }

    @Override
    public List<Socket> getSockets() {
        return this.spell.getGraph().getExternalSockets().values().stream().toList();
    }

    @Override
    public NodeInstance createInstance() {
        //TODO
//        return Nodes.SUB_SPELL.create(this.spell);
        return null;
    }
}
