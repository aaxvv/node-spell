package eu.aaxvv.node_spell.client;

import eu.aaxvv.node_spell.client.gui.SpellSelectionOverlay;

public class NodeSpellClient {
    private static SpellSelectionOverlay spellSelectionOverlay;

    public static void init() {
        spellSelectionOverlay = new SpellSelectionOverlay();

    }

    public static SpellSelectionOverlay getSpellSelectionOverlay() {
        return spellSelectionOverlay;
    }
}
