package eu.aaxvv.node_spell.spell.graph;

import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;

import java.util.Map;

public class SpellGraph {
    private NodeInstance entrypoint;

    private Map<String, Datatype> arguments;

    public NodeInstance getEntrypoint() {
        return entrypoint;
    }
}
