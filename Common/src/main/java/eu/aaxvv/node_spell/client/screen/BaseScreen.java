package eu.aaxvv.node_spell.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.gui.GuiContext;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class BaseScreen extends Screen {
    private int x;
    private int y;

    protected final GuiContext guiContext;

    public BaseScreen(Component title) {
        super(title);
        this.guiContext = new GuiContext(this);
    }

    @Override
    protected void init() {
        this.x = (this.width / 2) - (this.getRootWidth() / 2);
        this.y = (this.height / 2) - (this.getRootHeight() / 2);

        this.getGuiRoot().setLocalPosition(this.x, this.y);
        this.guiContext.getPopupPane().setSize(this.width, this.height);
    }

    public GuiElement getGuiRoot() {
        return this.guiContext.getRootPane();
    }

    public int getRootWidth() {
        return this.guiContext.getRootPane() == null ? 0 : this.guiContext.getRootPane().getWidth();
    }

    public int getRootHeight() {
        return this.guiContext.getRootPane() == null ? 0 : this.guiContext.getRootPane().getHeight();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        GuiElement rootElement = this.guiContext.getRootPane();
        if (rootElement != null) {
            rootElement.render(pose, mouseX, mouseY, tickDelta);
        }
        this.guiContext.runRenderLast(pose, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (guiContext.onKeyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double screenX, double screenY, int buttons) {
        return guiContext.onMouseDown(screenX, screenY, buttons);
    }

    @Override
    public boolean mouseReleased(double screenX, double screenY, int buttons) {
        return guiContext.onMouseUp(screenX, screenY, buttons);
    }

    @Override
    public boolean mouseDragged(double screenX, double screenY, int buttons, double dX, double dY) {
        return guiContext.onMouseDragged(screenX, screenY, buttons, dX, dY);
    }

    @Override
    public boolean mouseScrolled(double screenX, double screenY, double amount) {
        return guiContext.onMouseScrolled(screenX, screenY, amount);
    }

    @Override
    public void mouseMoved(double dX, double dY) {
        guiContext.onMouseMoved(dX, dY);
    }

    @Override
    public boolean charTyped(char character, int modifiers) {
        return guiContext.onCharTyped(character, modifiers);
    }
}
