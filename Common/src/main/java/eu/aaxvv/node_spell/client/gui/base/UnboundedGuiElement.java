package eu.aaxvv.node_spell.client.gui.base;

import eu.aaxvv.node_spell.client.gui.GuiElement;

public class UnboundedGuiElement extends GuiElement {
    public UnboundedGuiElement() {
        super(0,0);
    }

    @Override
    public boolean containsPointGlobal(double x, double y) {
        return true;
    }

    @Override
    public boolean containsPointLocal(double x, double y) {
        return true;
    }
}
