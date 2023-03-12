package eu.aaxvv.node_spell.spell.graph.verification.rule;

import eu.aaxvv.node_spell.spell.graph.Nodes;
import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.verification.VerificationResult;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class SingleEntrypointRule implements VerificationRule {
    @Override
    public void check(SpellGraph graph, Consumer<VerificationResult> resultConsumer) {
        boolean entrypointFound = false;

        for (NodeInstance instance : graph.getNodeInstances()) {
            if (instance.getBaseNode() == Nodes.ENTRY_POINT) {
                if (entrypointFound) {
                    resultConsumer.accept(VerificationResult.error(Component.translatable("gui.node_spell.spell_verification.multiple_entrypoints"), instance));
                    return;
                } else {
                    entrypointFound = true;
                    graph.setEntrypoint(instance);
                }
            }
        }

        if (!entrypointFound) {
            resultConsumer.accept(VerificationResult.error(Component.translatable("gui.node_spell.spell_verification.no_entrypoint")));
        }
    }
}
