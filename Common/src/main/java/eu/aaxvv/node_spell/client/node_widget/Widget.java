package eu.aaxvv.node_spell.client.node_widget;

import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;

public abstract class Widget<T> extends GuiElement {
    protected NodeInstance instance;
    protected T currentValue;

    @SuppressWarnings("unchecked")
    public Widget(NodeInstance instance, int width, int height) {
        super(width, height);
        this.instance = instance;
        this.currentValue = (T) this.instance.getInstanceData();
        if (!instance.getInstanceData().getClass().isAssignableFrom(this.currentValue.getClass())) {
            throw new IllegalArgumentException("Node instance data type does not match widget type.");
        }
    }

    public void commitValue() {
        this.instance.setInstanceData(this.currentValue);
        this.releaseFocus();
    }

    @SuppressWarnings("unchecked")
    public void rollbackValue() {
        this.currentValue = (T) this.instance.getInstanceData();
        this.releaseFocus();
    }

    @Override
    public void onLoseFocus() {
        commitValue();
    }
}
