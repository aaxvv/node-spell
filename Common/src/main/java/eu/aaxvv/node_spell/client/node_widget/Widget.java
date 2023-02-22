package eu.aaxvv.node_spell.client.node_widget;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;

public abstract class Widget<T> {
    protected NodeInstance parent;
    protected T currentValue;
    protected int localX;
    protected int localY;

    protected int width;
    protected int height;
    protected boolean focused;

    public Widget(NodeInstance parent, int width, int height) {
        this.parent = parent;
        this.width = width;
        this.height = height;
        this.currentValue = getDefaultValue();
        if (!parent.getInstanceData().getClass().isAssignableFrom(this.currentValue.getClass())) {
            throw new IllegalArgumentException("Node instance data type does not match widget type.");
        }
    }

    public void setLocalPosition(int x, int y) {
        this.localX = x;
        this.localY = y;
    }

    public int getX() {
        return this.parent.getX() + this.localX;
    }

    public int getY() {
        return this.parent.getY() + this.localY;
    }

    public int getLocalX() {
        return localX;
    }

    public int getLocalY() {
        return localY;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return focused;
    }

    public abstract void draw(PoseStack pose, int x, int y);

    public boolean isHit(int screenX, int screenY) {
        return screenX >= getX() && screenX < getX() + this.width && screenY >= getY() && screenY < getY() + this.height;
    }

    /**
     * Called when the element is focused and a key is pressed.
     */
    public void receiveKeyPress(int keyCode, int scanCode, int modifiers) {
    }

    /**
     * Called when the element is focused and a character is typed.
     */
    public void receiveCharTyped(char character, int modifiers) {
    }

    /**
     * Called when the element is focused and a mouse click occurs over it.
     */
    public void receiveMouseInput(int mouseX, int mouseY, int button) {
    }

    protected abstract T getDefaultValue();

    public void commitValue() {
        this.parent.setInstanceData(this.currentValue);
        this.setFocused(false);
    }

    @SuppressWarnings("unchecked")
    public void rollbackValue() {
        this.currentValue = (T) this.parent.getInstanceData();
        this.setFocused(false);
    }
}
