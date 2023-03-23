package eu.aaxvv.node_spell.client.gui.graph_editor;

import eu.aaxvv.node_spell.client.gui.helper.NodeLookup;

public class GuiSubSpellListPopup extends GuiNodeListPopup {
    public GuiSubSpellListPopup(NodeLookup lookup) {
        super(lookup.getSubSpellNodes());
    }
}
