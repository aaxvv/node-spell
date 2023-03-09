package eu.aaxvv.node_spell.client.node_widget;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;

public abstract class Widget<T> extends GuiElement {
    protected NodeInstance parent;
    protected T currentValue;
    protected int localX;
    protected int localY;

    protected int width;
    protected int height;
    protected boolean focused;

    @SuppressWarnings("unchecked")
    public Widget(NodeInstance parent, int width, int height) {
        super(width, height);
        this.parent = parent;
        this.width = width;
        this.height = height;
        this.currentValue = (T) this.parent.getInstanceData();
        if (!parent.getInstanceData().getClass().isAssignableFrom(this.currentValue.getClass())) {
            throw new IllegalArgumentException("Node instance data type does not match widget type.");
        }
    }

    public void commitValue() {
        this.parent.setInstanceData(this.currentValue);
        this.releaseFocus();
    }

    @SuppressWarnings("unchecked")
    public void rollbackValue() {
        this.currentValue = (T) this.parent.getInstanceData();
        this.releaseFocus();
    }

    @Override
    public void onLoseFocus() {
        commitValue();
    }
}
