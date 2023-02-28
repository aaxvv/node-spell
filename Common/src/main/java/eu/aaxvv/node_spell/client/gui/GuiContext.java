package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayDeque;
import java.util.Deque;

public class GuiContext {
    private GuiElement focused;
    private GuiElement root;
    private final Screen parentScreen;
    private final Deque<RenderCallback> postRenderTasks;

    public GuiContext(Screen parentScreen) {
        this.parentScreen = parentScreen;
        this.postRenderTasks = new ArrayDeque<>();
    }

    public GuiElement getFocused() {
        return focused;
    }

    public void setFocused(GuiElement focused) {
        this.focused = focused;
    }

    public GuiElement getRoot() {
        return root;
    }

    public void setRoot(GuiElement root) {
        this.root = root;
        this.root.setContext(this);
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

        if (this.root != null) {
            return this.root.onMouseDown(screenX, screenY, button);
        } else {
            return false;
        }
    }

    public boolean onMouseUp(double screenX, double screenY, int button) {
        if (this.focused != null && this.focused.onMouseUp(screenX, screenY, button)) {
            return true;
        }

        if (this.root != null) {
            return this.root.onMouseUp(screenX, screenY, button);
        } else {
            return false;
        }
    }

    public boolean onMouseMoved(double dX, double dY) {
        double screenX = Minecraft.getInstance().mouseHandler.xpos() * (double)Minecraft.getInstance().getWindow().getGuiScaledWidth() / (double)Minecraft.getInstance().getWindow().getScreenWidth();
        double screenY = Minecraft.getInstance().mouseHandler.ypos() * (double)Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double)Minecraft.getInstance().getWindow().getScreenHeight();

        if (this.focused != null && this.focused.onMouseMoved(screenX, screenY, dX, dY)) {
            return true;
        }

        if (this.root != null) {
            return this.root.onMouseMoved(screenX, screenY, dX, dY);
        } else {
            return false;
        }
    }

    public boolean onMouseDragged(double screenX, double screenY, int buttons, double dX, double dY) {
        if (this.focused != null && this.focused.onMouseDragged(screenX, screenY, buttons, dX, dY)) {
            return true;
        }

        if (this.root != null) {
            return this.root.onMouseDragged(screenX, screenY, buttons, dX, dY);
        } else {
            return false;
        }
    }

    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.focused != null && this.focused.onKeyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        if (this.root != null) {
            return this.root.onKeyPressed(keyCode, scanCode, modifiers);
        } else {
            return false;
        }
    }

    public boolean onCharTyped(char character, int modifiers) {
        if (this.focused != null && this.focused.onCharTyped(character, modifiers)) {
            return true;
        }

        if (this.root != null) {
            return this.root.onCharTyped(character, modifiers);
        } else {
            return false;
        }
    }

    @FunctionalInterface
    public interface RenderCallback {
        void render(PoseStack pose, int mouseX, int mouseY, float tickDelta);
    }
}
