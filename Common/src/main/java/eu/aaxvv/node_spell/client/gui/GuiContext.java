package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.gui.elements.GuiPopupPane;
import eu.aaxvv.node_spell.client.gui.elements.UnboundedGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.Deque;

public class GuiContext {
    private GuiElement focused;
    private final GuiElement rootPane;
    private final GuiPopupPane popupPane;
    private final Screen parentScreen;
    private final Deque<RenderCallback> postRenderTasks;

    private boolean mouseLeftDown;
    private boolean mouseRightDown;

    public GuiContext(Screen parentScreen) {
        this.parentScreen = parentScreen;
        this.rootPane = new UnboundedGuiElement();
        this.rootPane.setContext(this);
        this.popupPane = new GuiPopupPane();
        this.popupPane.setContext(this);
        this.postRenderTasks = new ArrayDeque<>();

        this.mouseLeftDown = false;
        this.mouseRightDown = false;
    }

    public GuiElement getFocused() {
        return focused;
    }

    public void setFocused(GuiElement focused) {
        if (this.focused != null && this.focused != focused) {
            GuiElement prevFocused = this.focused;
            this.focused = null;
            prevFocused.onLoseFocus();
        }

        this.focused = focused;
    }

    public GuiElement getRootPane() {
        return rootPane;
    }

    public GuiPopupPane getPopupPane() {
        return popupPane;
    }

    public Screen getParentScreen() {
        return parentScreen;
    }

    public void scheduleRenderLast(RenderCallback callback) {
        postRenderTasks.addFirst(callback);
    }

    public void runRenderLast(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        while (!this.postRenderTasks.isEmpty()) {
            this.postRenderTasks.removeLast().render(pose, mouseX, mouseY, tickDelta);
        }
    }

    public boolean onMouseDown(double screenX, double screenY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            this.mouseLeftDown = true;
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            this.mouseRightDown = true;
        }

//        if (this.focused != null && this.focused.onMouseDown(screenX, screenY, button)) {
//            return true;
//        }

        if (this.popupPane.onMouseDown(screenX, screenY, button)) {
            return true;
        }

        return this.rootPane.onMouseDown(screenX, screenY, button);
    }

    public boolean onMouseUp(double screenX, double screenY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            this.mouseLeftDown = false;
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            this.mouseRightDown = false;
        }

        if (this.focused != null && this.focused.onMouseUp(screenX, screenY, button)) {
            return true;
        }

        if (this.popupPane.onMouseUp(screenX, screenY, button)) {
            return true;
        }

        return this.rootPane.onMouseUp(screenX, screenY, button);
    }

    public boolean onMouseScrolled(double screenX, double screenY, double delta) {
        if (this.focused != null && this.focused.onMouseScrolled(screenX, screenY, delta)) {
            return true;
        }

        if (this.popupPane.onMouseScrolled(screenX, screenY, delta)) {
            return true;
        }

        return this.rootPane.onMouseScrolled(screenX, screenY, delta);
    }

    public void onMouseMoved(double dX, double dY) {
        double screenX = Minecraft.getInstance().mouseHandler.xpos() * (double)Minecraft.getInstance().getWindow().getGuiScaledWidth() / (double)Minecraft.getInstance().getWindow().getScreenWidth();
        double screenY = Minecraft.getInstance().mouseHandler.ypos() * (double)Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double)Minecraft.getInstance().getWindow().getScreenHeight();

        // since the vanilla input system doesn't support dragging with two buttons at once, we need to handle it ourselves
        if (this.mouseLeftDown) {
            onMouseDraggedInternal(screenX, screenY, GLFW.GLFW_MOUSE_BUTTON_LEFT, dX, dY);
        }

        if (this.mouseRightDown) {
            onMouseDraggedInternal(screenX, screenY, GLFW.GLFW_MOUSE_BUTTON_RIGHT, dX, dY);
        }

        if (this.focused != null && this.focused.onMouseMoved(screenX, screenY, dX, dY)) {
            return;
        }

        if (this.popupPane.onMouseMoved(screenX, screenY, dX, dY)) {
            return;
        }

        this.rootPane.onMouseMoved(screenX, screenY, dX, dY);
    }

    public boolean onMouseDragged(double screenX, double screenY, int button, double dX, double dY) {
        return false;
    }

    private boolean onMouseDraggedInternal(double screenX, double screenY, int button, double dX, double dY) {
        if (this.focused != null && this.focused.onMouseDragged(screenX, screenY, button, dX, dY)) {
            return true;
        }

        if (this.popupPane.onMouseDragged(screenX, screenY, button, dX, dY)) {
            return true;
        }

        return this.rootPane.onMouseDragged(screenX, screenY, button, dX, dY);
    }

    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.focused != null && this.focused.onKeyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        if (this.popupPane.onKeyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        return this.rootPane.onKeyPressed(keyCode, scanCode, modifiers);
    }

    public boolean onCharTyped(char character, int modifiers) {
        if (this.focused != null && this.focused.onCharTyped(character, modifiers)) {
            return true;
        }

        if (this.popupPane.onCharTyped(character, modifiers)) {
            return true;
        }

        return this.rootPane.onCharTyped(character, modifiers);
    }

    @FunctionalInterface
    public interface RenderCallback {
        void render(PoseStack pose, int mouseX, int mouseY, float tickDelta);
    }
}
