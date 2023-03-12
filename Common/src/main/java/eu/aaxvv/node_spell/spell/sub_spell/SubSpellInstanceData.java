package eu.aaxvv.node_spell.spell.sub_spell;

import eu.aaxvv.node_spell.spell.Spell;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;

import java.util.Map;

public class SubSpellInstanceData {
    private Map<NodeInstance, SocketInstance> sockets;
    private final Spell referencedSpell;

    private final NodeInstance parentInstance;
    private final boolean hasSideEffects;   // check if external flow input pin exists

    public SubSpellInstanceData(NodeInstance parentInstance, Spell spell) {
        this.parentInstance = parentInstance;
        this.referencedSpell = spell;

        for (var externalSocket : this.referencedSpell.getGraph().getExternalSockets().entrySet()) {
            SocketInstance instance = new SocketInstance(externalSocket.getValue(), this.parentInstance);
            this.sockets.put(externalSocket.getKey(), instance);
        }

        this.hasSideEffects = false;
    }

    public SocketInstance getSocketInstance(NodeInstance nodeInstance) {
        return this.sockets.get(nodeInstance);
    }

    public Spell getReferencedSpell() {
        return referencedSpell;
    }
}