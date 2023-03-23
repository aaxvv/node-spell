package eu.aaxvv.node_spell.spell;

import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellDeserializationContext;
import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Spell {
    private UUID id;
    private String name;

    private final SpellGraph graph;
    private boolean hasErrors;

    private final List<WeakReference<Spell>> dependentSpells;
    private boolean refreshing;

    private Spell() {
        this(null, new SpellGraph(), null);
    }

    public Spell(String name) {
        this(name, new SpellGraph(), UUID.randomUUID());
    }

    private Spell(String name, SpellGraph graph, UUID id) {
        this.id = id;
        this.name = name;
        this.graph = graph;
        this.hasErrors = false;
        this.dependentSpells = new ArrayList<>();
        this.refreshing = false;
    }

    public static Tag createEmptyNbt(String name, UUID spellId) {
        CompoundTag spellTag = new CompoundTag();
        spellTag.put("Id", NbtUtils.createUUID(spellId));
        spellTag.putString("Name", name);
        return spellTag;
    }

    public void run(SpellContext ctx) {

    }

    public void serialize(CompoundTag nbt) {
        nbt.putString("Name", this.name);
        nbt.put("Id", NbtUtils.createUUID(this.id));
        this.graph.serialize(nbt);
    }

    public void deserialize(CompoundTag nbt, SpellDeserializationContext context) {
        this.name = nbt.getString("Name");
        Tag idTag = nbt.get("Id");
        if (idTag != null) {
            this.id = NbtUtils.loadUUID(idTag);
        } else {
            this.id = UUID.randomUUID();
        }
        this.graph.deserialize(nbt, context);
    }

    public static Spell fromNbt(CompoundTag nbt, SpellDeserializationContext context) {
        Spell spell = new Spell();
        context.pushCurrentSpell(spell);
        spell.deserialize(nbt, context);
        context.popCurrentSpell();
        return spell;
    }

    public SpellGraph getGraph() {
        return graph;
    }

    public UUID getId() {
        return id;
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

    public void addDependent(Spell dependentSpell) {
        this.dependentSpells.add(new WeakReference<>(dependentSpell));
    }

    public void refreshDependents() {
        this.refreshing = true;
        var iterator = this.dependentSpells.iterator();

        while (iterator.hasNext()) {
            var dependent = iterator.next().get();
            if (dependent == null) {
                iterator.remove();
            } else {
                dependent.refreshOnDependencyChange();
            }
        }

        this.refreshing = false;
    }

    public void refreshOnDependencyChange() {
        if (this.refreshing) {
            // actually cant have a dependency loop right now because we don't update our dependents on this. should we be doing that?

            return;
        }

        this.graph.refreshSubSpellNodes();
    }
}
