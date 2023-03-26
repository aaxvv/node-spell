package eu.aaxvv.node_spell.client.gui.node_widget;

import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.screen.SpellEditContext;
import eu.aaxvv.node_spell.spell.graph.runtime.InstanceDataContainer;

public abstract class Widget<T> extends GuiElement {
    protected InstanceDataContainer instance;
    protected T currentValue;

    @SuppressWarnings("unchecked")
    public Widget(InstanceDataContainer instance, int width, int height) {
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
        SpellEditContext.reVerifyGraph();
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
