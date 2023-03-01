package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayDeque;
import java.util.Deque;

public class GuiContext {
    private GuiElement focused;
    private final GuiElement rootPane;
    private final GuiPopupPane popupPane;
    private final Screen parentScreen;
    private final Deque<RenderCallback> postRenderTasks;

    public GuiContext(Screen parentScreen) {
        this.parentScreen = parentScreen;
        this.rootPane = new GuiElement(0, 0);
        this.rootPane.setContext(this);
        this.popupPane = new GuiPopupPane(0, 0);
        this.popupPane.setContext(this);
        this.postRenderTasks = new ArrayDeque<>();
    }

    public GuiElement getFocused() {
        return focused;
    }

    public void setFocused(GuiElement focused) {
        this.focused = focused;
    }

    public GuiElement getRootPane() {
        return rootPane;
    }

    public GuiElement getPopupPane() {
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
        if (this.focused != null && this.focused.onMouseDown(screenX, screenY, button)) {
            return true;
        }

        return this.rootPane.onMouseDown(screenX, screenY, button);
    }

    public boolean onMouseUp(double screenX, double screenY, int button) {
        if (this.focused != null && this.focused.onMouseUp(screenX, screenY, button)) {
            return true;
        }

        return this.rootPane.onMouseUp(screenX, screenY, button);
    }

    public boolean onMouseScrolled(double screenX, double screenY, double delta) {
        if (this.focused != null && this.focused.onMouseScrolled(screenX, screenY, delta)) {
            return true;
        }

        return this.rootPane.onMouseScrolled(screenX, screenY, delta);
    }

    public void onMouseMoved(double dX, double dY) {
        double screenX = Minecraft.getInstance().mouseHandler.xpos() * (double)Minecraft.getInstance().getWindow().getGuiScaledWidth() / (double)Minecraft.getInstance().getWindow().getScreenWidth();
        double screenY = Minecraft.getInstance().mouseHandler.ypos() * (double)Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double)Minecraft.getInstance().getWindow().getScreenHeight();

        if (this.focused != null && this.focused.onMouseMoved(screenX, screenY, dX, dY)) {
            return;
        }

        this.rootPane.onMouseMoved(screenX, screenY, dX, dY);
    }

    public boolean onMouseDragged(double screenX, double screenY, int buttons, double dX, double dY) {
        if (this.focused != null && this.focused.onMouseDragged(screenX, screenY, buttons, dX, dY)) {
            return true;
        }

        return this.rootPane.onMouseDragged(screenX, screenY, buttons, dX, dY);
    }

    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.focused != null && this.focused.onKeyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        return this.rootPane.onKeyPressed(keyCode, scanCode, modifiers);
    }

    public boolean onCharTyped(char character, int modifiers) {
        if (this.focused != null && this.focused.onCharTyped(character, modifiers)) {
            return true;
        }

        return this.rootPane.onCharTyped(character, modifiers);
    }

    @FunctionalInterface
    public interface RenderCallback {
        void render(PoseStack pose, int mouseX, int mouseY, float tickDelta);
    }
}
