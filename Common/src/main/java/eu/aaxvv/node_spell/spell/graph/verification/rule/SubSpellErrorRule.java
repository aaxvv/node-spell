package eu.aaxvv.node_spell.spell.graph.verification.rule;

import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.nodes.custom.SubSpellPseudoNode;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.verification.VerificationResult;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SubSpellErrorRule implements VerificationRule {
    @Override
    public void check(SpellGraph graph, Consumer<VerificationResult> resultConsumer) {
        List<NodeInstance> missingNodes = new ArrayList<>();
        List<NodeInstance> errorNodes = new ArrayList<>();

        for (NodeInstance instance : graph.getNodeInstances()) {
            if (instance.getBaseNode() instanceof SubSpellPseudoNode pseudo) {
                if (pseudo.getSpell() == null) {
                    missingNodes.add(instance);
                } else if (pseudo.hasErrors()) {
                    errorNodes.add(instance);
                }
            }
        }

        if (!missingNodes.isEmpty()) {
            resultConsumer.accept(VerificationResult.error(Component.translatable("gui.node_spell.spell_verification.sub_spell_missing"), missingNodes));
        }

        if (!errorNodes.isEmpty()) {
            resultConsumer.accept(VerificationResult.error(Component.translatable("gui.node_spell.spell_verification.sub_spell_errors"), errorNodes));
        }
    }
}
