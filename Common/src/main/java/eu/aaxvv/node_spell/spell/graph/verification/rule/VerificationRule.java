package eu.aaxvv.node_spell.spell.graph.verification.rule;

import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.verification.VerificationResult;

import java.util.function.Consumer;

public interface VerificationRule {
    void check(SpellGraph graph, Consumer<VerificationResult> resultConsumer);
}
