package eu.aaxvv.node_spell.spell.graph.verification.rule;

import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.nodes.sub_spell.SubSpellSocketNode;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.verification.VerificationResult;
import net.minecraft.network.chat.Component;

import java.util.*;
import java.util.function.Consumer;

public class SubSpellSocketRule implements VerificationRule {
    @Override
    public void check(SpellGraph graph, Consumer<VerificationResult> resultConsumer) {
        Map<Integer, NodeInstance> socketsByHash = new HashMap<>();
        Set<NodeInstance> conflictingSockets = new HashSet<>();
        boolean isSubSpell = false;

        for (NodeInstance instance : graph.getNodeInstances()) {
            if (instance.getBaseNode() instanceof SubSpellSocketNode socketNode) {
                isSubSpell = true;
                int hash = socketNode.getSocketHash(instance);

                if (socketsByHash.containsKey(hash)) {
                    conflictingSockets.add(instance);
                    conflictingSockets.add(socketsByHash.get(hash));
                } else {
                    socketsByHash.put(hash, instance);
                }
            }
        }

        graph.setIsSubSpell(isSubSpell);
        if (!conflictingSockets.isEmpty()) {
            resultConsumer.accept(VerificationResult.error(Component.translatable("gui.node_spell.spell_verification.conflicting_sub_spell_sockets"), new ArrayList<>(conflictingSockets)));
        }
    }

}
