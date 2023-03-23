package eu.aaxvv.node_spell.spell.graph.nodes.custom;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.Spell;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellDeserializationContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class SubSpellNode extends Node {
    public SubSpellNode() {
        super(NodeCategories.CUSTOM, ModConstants.resLoc("sub_spell"));
    }

    public static Node create(Spell subSpell) {
        return new SubSpellPseudoNode(subSpell);
    }

    public NodeInstance fromNbt(CompoundTag nodeTag, SpellDeserializationContext context) {
        UUID spellId = nodeTag.getCompound("Data").getUUID("SpellId");
        Spell spell = context.findSpell(spellId);
        if (spell != null) {
            spell.addDependent(context.getCurrentSpell());
        }
        NodeInstance instance = new NodeInstance(SubSpellNode.create(spell));
        instance.deserialize(nodeTag, context);
        return instance;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {

    }
}
