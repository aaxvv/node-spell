package eu.aaxvv.node_spell.spell;

import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import net.minecraft.nbt.CompoundTag;

public class Spell {
    private final String name;

    private final SpellGraph graph;

    public Spell(String name) {
        this.name = name;
        this.graph = new SpellGraph();
    }

    private Spell(String name, SpellGraph graph) {
        this.name = name;
        this.graph = graph;
    }

    public void run(SpellContext ctx) {

    }

    public void serialize(CompoundTag nbt) {

    }

    public void deserialize(CompoundTag nbt) {

    }

    public SpellGraph getGraph() {
        return graph;
    }

    public String getName() {
        return name;
    }
}
