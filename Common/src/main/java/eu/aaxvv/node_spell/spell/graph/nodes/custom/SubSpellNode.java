package eu.aaxvv.node_spell.spell.graph.nodes.custom;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.Spell;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellDeserializationContext;
import eu.aaxvv.node_spell.spell.execution.SpellRunner;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.FlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.sub_spell.SubSpellInstanceData;
import net.minecraft.nbt.CompoundTag;

public class SubSpellNode extends FlowNode {
    public SubSpellNode() {
        super(NodeCategories.CUSTOM, ModConstants.resLoc("sub_spell"));
    }

    public NodeInstance create(Spell subSpell) {
        NodeInstance instance = this.createInstance();
        instance.setInstanceData(new SubSpellInstanceData(instance, subSpell));
        return instance;
    }

    @Override
    public Socket getFlowContinueSocket(SpellContext ctx, NodeInstance instance) {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {

    }

//    @Override
//    public Component getDisplayName(NodeInstance instance) {
//        SubSpellInstanceData data = ((SubSpellInstanceData) instance.getInstanceData());
//        String spellName = data.getReferencedSpell().getName();
//        return Component.literal(spellName);
//    }

    @Override
    public SpellRunner getSubRunner(SpellContext ctx, NodeInstance instance) {
        // TODO: spell runner
        return super.getSubRunner(ctx, instance);
    }

    @Override
    public boolean hasSideEffects() {
        //TODO: depends on if spell has flow input
        return super.hasSideEffects();
    }

    @Override
    public Object createInstanceData() {
        return super.createInstanceData();
    }

    @Override
    public void serializeInstanceData(Object instanceData, CompoundTag dataTag) {
        super.serializeInstanceData(instanceData, dataTag);
    }

    @Override
    public Object deserializeInstanceData(CompoundTag dataTag, SpellDeserializationContext context) {
        return super.deserializeInstanceData(dataTag, context);
    }


}
