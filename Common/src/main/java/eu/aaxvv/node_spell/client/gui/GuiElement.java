package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Vector2i;

import java.util.*;

/**
 * Custom Gui abstraction because working with the minecraft "drawableChild" stuff
 * is just not viable for a complex node GUI.
 */
public class GuiElement {
    private int x;
    private int y;
    private int height;
    private int width;

    private int cachedGlobalX;
    private int cachedGlobalY;

    private GuiElement parent;
    private List<GuiElement> children;
    private GuiContext context;

    public GuiElement(int width, int height) {
        this.x = 0;
        this.y = 0;
        this.width = width;
        this.height = height;
        this.children = new ArrayList<>();
    }

    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        for (GuiElement child : this.children) {
            child.render(pose, mouseX, mouseY, tickDelta);
        }
    }

    public void setLocalPosition(int x, int y) {
        this.x = x;
        this.y = y;
        invalidate();
    }

    public int getLocalX() {
        return this.x;
    }
    public int getLocalY() {
        return this.y;
    }

    public int getGlobalX() {
        return cachedGlobalX;
    }
    public int getGlobalY() {
        return cachedGlobalY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public GuiContext getContext() {
        return context;
    }

    public void setContext(GuiContext context) {
        this.context = context;
    }

    public Vector2i toLocal(int globalX, int globalY) {
        return new Vector2i(globalX - this.getGlobalX(), globalY - this.getGlobalY());
    }

    public void invalidate() {
        if (getParent() != null) {
            this.cachedGlobalX = getParent().getGlobalX() + getLocalX();
            this.cachedGlobalY = getParent().getGlobalY() + getLocalY();
        } else {
            this.cachedGlobalX = getLocalX();
            this.cachedGlobalY = getLocalY();
        }

        this.children.forEach(GuiElement::invalidate);
    }

    public boolean containsPointGlobal(double x, double y) {
        return x >= this.cachedGlobalX && x < this.cachedGlobalX + this.width && y >= this.cachedGlobalY && y < this.cachedGlobalY + this.height;
    }

    public boolean containsPointLocal(double x, double y) {
        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }

    public Collection<GuiElement> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    public GuiElement addChild(GuiElement child) {
        this.children.add(child);
        child.context = this.context;
        child.setParent(this);
        return child;
    }

    public void removeChild(GuiElement child) {
        this.children.remove(child);
    }

    public GuiElement getParent() {
        return this.parent;
    }

    public void setParent(GuiElement parent) {
        this.parent = parent;
        invalidate();
    }

    public boolean isFocused() {
        return this.context.getFocused() == this;
    }

    public void requestFocus() {
        this.context.setFocused(this);;
    }

    public void releaseFocus() {
        if (this.context.getFocused() == this) {
            this.context.setFocused(null);
        }
    }

    public boolean intersects(GuiElement other) {
        return other.cachedGlobalX < this.cachedGlobalX + this.width
                && other.cachedGlobalX + other.width >= this.cachedGlobalX
                && other.cachedGlobalY < this.cachedGlobalY + this.height
                && other.cachedGlobalY + other.height >= this.cachedGlobalY;
    }

    // event handlers

    public boolean onMouseDown(double screenX, double screenY, int button) {
        for (GuiElement child : getChildren()) {
            if (child.containsPointGlobal(screenX, screenY) && child.onMouseDown(screenX, screenY, button)) {
                return true;
            }
        }

        return false;
    }

    public boolean onMouseUp(double screenX, double screenY, int button) {
        for (GuiElement child : getChildren()) {
            if (child.containsPointGlobal(screenX, screenY) && child.onMouseUp(screenX, screenY, button)) {
                return true;
            }
        }

        return false;
    }

    public boolean onMouseScrolled(double screenX, double screenY, double amount) {
        for (GuiElement child : getChildren()) {
            if (child.containsPointGlobal(screenX, screenY) && child.onMouseScrolled(screenX, screenY, amount)) {
                return true;
            }
        }

        return false;
    }

    public boolean onMouseMoved(double screenX, double screenY, double dX, double dY) {
        for (GuiElement child : getChildren()) {
            if (child.containsPointGlobal(screenX, screenY) && child.onMouseMoved(screenX, screenY, dX, dY)) {
                return true;
            }
        }

        return false;
    }

    public boolean onMouseDragged(double screenX, double screenY, int buttons, double dX, double dY) {
        return false;
    }

    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean onCharTyped(char character, int modifiers) {
        return false;
    }
}
