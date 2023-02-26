package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.spell.Spell;

import java.util.*;

public class PlayerSpellCache {
    private static final Map<UUID, Map<String, Spell>> cache = new HashMap<>();

    public static Optional<Spell> get(UUID playerUuid, String spellName) {
        Map<String, Spell> map = cache.get(playerUuid);
        if (map == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(map.get(spellName));
    }

    public static void put(UUID playerUuid, String spellName, Spell spell) {
        Map<String, Spell> map = cache.computeIfAbsent(playerUuid, k -> new HashMap<>());

        map.put(spellName, spell);
    }

    public static void invalidate(UUID playerUuid, String spellName) {
        Map<String, Spell> map = cache.get(playerUuid);
        if (map == null) {
            return;
        }

        map.remove(spellName);
    }
}
