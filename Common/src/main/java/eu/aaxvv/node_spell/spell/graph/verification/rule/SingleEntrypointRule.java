package eu.aaxvv.node_spell.spell.graph.verification.rule;

import eu.aaxvv.node_spell.spell.graph.Nodes;
import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.verification.VerificationResult;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SingleEntrypointRule implements VerificationRule {
    @Override
    public void check(SpellGraph graph, Consumer<VerificationResult> resultConsumer) {
        List<NodeInstance> entrypoints = new ArrayList<>();

        for (NodeInstance instance : graph.getNodeInstances()) {
            if (instance.getBaseNode() == Nodes.ENTRY_POINT) {
                entrypoints.add(instance);
            }
        }

        if (entrypoints.isEmpty()) {
            resultConsumer.accept(VerificationResult.error(Component.translatable("gui.node_spell.spell_verification.no_entrypoint")));
        } else if (entrypoints.size() > 1) {
            resultConsumer.accept(VerificationResult.error(Component.translatable("gui.node_spell.spell_verification.multiple_entrypoints"), entrypoints));
        } else {
            graph.setEntrypoint(entrypoints.get(0));
        }
    }
}
