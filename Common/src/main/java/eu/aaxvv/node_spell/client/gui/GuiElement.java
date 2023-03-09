package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom Gui abstraction because working with the minecraft "drawableChild" stuff
 * is just not viable for a complex node GUI.
 */
public class GuiElement {
    protected int x;
    protected int y;
    protected int height;
    protected int width;

    private int cachedGlobalX;
    private int cachedGlobalY;

    private GuiElement parent;
    private final List<GuiElement> children;
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
        int oldValue = this.width;
        this.width = width;
        if (oldValue != width) {
            invalidate();
        }
    }

    public void setHeight(int height) {
        int oldValue = this.height;
        this.height = height;
        if (oldValue != height) {
            invalidate();
        }
    }

    public void setSize(int width, int height) {
        int oldWidth = this.width;
        int oldHeight = this.height;
        this.width = width;
        this.height = height;
        if (oldWidth != width || oldHeight != height) {
            invalidate();
        }
    }

    public GuiContext getContext() {
        return context;
    }

    public void setContext(GuiContext context) {
        this.context = context;
        this.children.forEach(child -> child.setContext(this.context));
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

    public List<GuiElement> getChildren() {
        return this.children;
    }

    public GuiElement addChild(GuiElement child) {
        this.children.add(child);
        child.setContext(this.context);
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

    public boolean intersectsRect(int x, int y, int w, int h) {
        return x < this.cachedGlobalX + this.width
                && x + w >= this.cachedGlobalX
                && y < this.cachedGlobalY + this.height
                && y + h >= this.cachedGlobalY;
    }

    public boolean intersectsRectLocal(int x, int y, int w, int h) {
        return x < this.getLocalX() + this.getWidth()
                && x + w >= this.getLocalX()
                && y < this.getLocalY() + this.getHeight()
                && y + h >= this.getLocalY();
    }

    public int getIndexInParent() {
        return this.parent.getChildren().indexOf(this);
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

    public boolean onMouseDragged(double screenX, double screenY, int button, double dX, double dY) {
        for (GuiElement child : getChildren()) {
            if (child.containsPointGlobal(screenX, screenY) && child.onMouseDragged(screenX, screenY, button, dX, dY)) {
                return true;
            }
        }

        return false;
    }

    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        for (GuiElement child : getChildren()) {
            if (child.onKeyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }

        return false;
    }

    public boolean onCharTyped(char character, int modifiers) {
        for (GuiElement child : getChildren()) {
            if (child.onCharTyped(character, modifiers)) {
                return true;
            }
        }

        return false;
    }


    public void onLoseFocus() {

    }
}
