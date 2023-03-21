package eu.aaxvv.node_spell.spell;

import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellDeserializationContext;
import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class Spell {
    private String name;

    private final SpellGraph graph;
    private boolean hasErrors;

    public Spell(String name) {
        this(name, new SpellGraph());
    }

    private Spell(String name, SpellGraph graph) {
        this.name = name;
        this.graph = graph;
        this.hasErrors = false;
    }

    public static Tag createEmptyNbt(String name) {
        CompoundTag spellTag = new CompoundTag();
        spellTag.putString("Name", name);
        return spellTag;
    }

    public void run(SpellContext ctx) {

    }

    public void serialize(CompoundTag nbt) {
        nbt.putString("Name", this.name);
        this.graph.serialize(nbt);
    }

    public void deserialize(CompoundTag nbt, SpellDeserializationContext context) {
        this.name = nbt.getString("Name");
        this.graph.deserialize(nbt, context);
    }

    public static Spell fromNbt(CompoundTag nbt, SpellDeserializationContext context) {
        Spell spell = new Spell(null);
        spell.deserialize(nbt, context);
        return spell;
    }

    public SpellGraph getGraph() {
        return graph;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public boolean hasErrors() {
        return hasErrors;
    }
}
