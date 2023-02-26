package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.spell.Spell;

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

    public void put(UUID playerUuid, String spellName, Spell spell) {
        Map<String, Spell> map = cache.computeIfAbsent(playerUuid, k -> new HashMap<>());

        map.put(spellName, spell);
    }

    public void invalidate(UUID playerUuid, String spellName) {
        Map<String, Spell> map = cache.get(playerUuid);
        if (map == null) {
            return;
        }

        map.remove(spellName);
    }
}
