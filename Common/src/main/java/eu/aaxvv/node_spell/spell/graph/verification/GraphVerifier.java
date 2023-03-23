package eu.aaxvv.node_spell.spell.graph.verification;

import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.verification.rule.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphVerifier {
    private final List<VerificationRule> rules = List.of(
            new SingleEntrypointRule(),
            new NoCyclesRule(),
            new DataUsedBeforeFlowReachedRule(),
            new SubSpellSocketRule()
    );
    private final SpellGraph graph;
    private final List<VerificationResult> problems;
    private final Map<NodeInstance, ErrorLevel> errorLevelCache;

    public GraphVerifier(SpellGraph graph) {
        this.graph = graph;
        this.problems = new ArrayList<>();
        this.errorLevelCache = new HashMap<>();
    }

    public boolean check() {
        this.problems.clear();

        for (VerificationRule rule : this.rules) {
            rule.check(this.graph, this.problems::add);
        }

        this.errorLevelCache.clear();
        this.problems.forEach(result -> {
            result.getProblematicInstances().forEach(instance -> {
                this.errorLevelCache.put(instance, result.getErrorLevel());
            });
        });
        return this.problems.stream().noneMatch(VerificationResult::isError);
    }

    public List<VerificationResult> getProblems() {
        return this.problems;
    }

    public ErrorLevel getNodeErrorLevel(NodeInstance nodeInstance) {
        return this.errorLevelCache.getOrDefault(nodeInstance, ErrorLevel.OK);
    }
}
