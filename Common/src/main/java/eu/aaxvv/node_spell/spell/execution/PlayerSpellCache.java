package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.spell.Spell;
import eu.aaxvv.node_spell.spell.graph.verification.GraphVerifier;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public class PlayerSpellCache {
    private final Map<UUID, Map<String, Spell>> cache;

    public PlayerSpellCache() {
        this.cache = new HashMap<>();
    }

    public Optional<Spell> get(UUID playerUuid, String spellName) {
        Map<String, Spell> map = cache.get(playerUuid);
        if (map == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(map.get(spellName));
    }

    public Optional<Spell> getOrCreate(UUID playerUuid, String spellName, CompoundTag spellNbt) {
        Map<String, Spell> map = cache.computeIfAbsent(playerUuid, key -> new HashMap<>());
        Spell spell = map.get(spellName);

        if (spell == null) {
            if (spellNbt == null) {
                return Optional.empty();
            }

            spell = Spell.fromNbt(spellNbt);
            GraphVerifier verifier = new GraphVerifier(spell.getGraph());
            spell.setHasErrors(!verifier.check());
            map.put(spellName, spell);
        }

        return Optional.of(spell);
    }

    public void put(UUID playerUuid, String spellName, Spell spell) {
        Map<String, Spell> map = cache.computeIfAbsent(playerUuid, k -> new HashMap<>());

        map.put(spellName, spell);
    }

    public void invalidate(UUID playerUuid) {
        Map<String, Spell> map = cache.get(playerUuid);
        if (map == null) {
            return;
        }

        map.clear();
    }
}
