package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.spell.Spell;
import eu.aaxvv.node_spell.spell.graph.verification.GraphVerifier;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public class PlayerSpellCache {
    private final Map<UUID, Map<UUID, Spell>> cache;

    public PlayerSpellCache() {
        this.cache = new HashMap<>();
    }

    public Optional<Spell> get(UUID playerUuid, UUID spellId) {
        Map<UUID, Spell> map = cache.get(playerUuid);
        if (map == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(map.get(spellId));
    }

    public Optional<Spell> getOrCreate(UUID playerUuid, UUID spellId, CompoundTag spellNbt, SpellDeserializationContext context) {
        Map<UUID, Spell> map = cache.computeIfAbsent(playerUuid, key -> new HashMap<>());
        Spell spell = map.get(spellId);

        if (spell == null) {
            if (spellNbt == null) {
                return Optional.empty();
            }

            spell = Spell.fromNbt(spellNbt, context);
            GraphVerifier verifier = new GraphVerifier(spell.getGraph());
            spell.setHasErrors(!verifier.check());
            map.put(spellId, spell);
        }

        return Optional.of(spell);
    }

    public void put(UUID playerUuid, UUID spellId, Spell spell) {
        Map<UUID, Spell> map = cache.computeIfAbsent(playerUuid, k -> new HashMap<>());

        map.put(spellId, spell);
    }

    public void invalidate(UUID playerUuid) {
        Map<UUID, Spell> map = cache.get(playerUuid);
        if (map == null) {
            return;
        }

        map.clear();
    }
}
