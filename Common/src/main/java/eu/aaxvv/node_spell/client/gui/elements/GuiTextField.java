package eu.aaxvv.node_spell.client.gui.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.gui.helper.TextEditController;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class GuiTextField extends GuiElement {
    private final TextEditController textEditController;
    private int borderColor;
    private int backgroundColor;
    private Consumer<String> valueChangedCallback;
    private Runnable enterPressedCallback;

    private String lastCommittedValue;

    public GuiTextField(int width, int height) {
        super(width, height);
        this.lastCommittedValue = "";
        this.textEditController = new TextEditController();
        this.textEditController.setDisplayWidth(this.width - 4);
        this.textEditController.setRollbackValueProvider(() -> this.lastCommittedValue);
        this.textEditController.setDisplayTextChangedCallback(this::textChanged);
        this.textEditController.setDoneCallback(this::editDone);
        this.borderColor = ModConstants.Colors.PAPER_BORDER;
        this.backgroundColor = ModConstants.Colors.PAPER_BG;
    }

    public void focus() {
        this.textEditController.startEditing(this.textEditController.getText());
        requestFocus();
    }

    private boolean editDone(String text) {
        releaseFocus();
        if (text == null) {
            return false;
        }

        this.lastCommittedValue = text;
        return true;
    }

    private void textChanged(String text) {
        if (this.valueChangedCallback != null) {
            this.valueChangedCallback.accept(this.textEditController.getText());
        }
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        RenderUtil.drawRect(pose, this.getGlobalX(), this.getGlobalY(), this.getWidth(), this.getHeight(), this.borderColor);
        RenderUtil.drawRect(pose, this.getGlobalX() + 1, this.getGlobalY() + 1, this.getWidth() - 2, this.getHeight() - 2, this.backgroundColor);

        Minecraft.getInstance().font.draw(pose, this.textEditController.getDisplayString(), this.getGlobalX() + 2, this.getGlobalY() + 2, ModConstants.Colors.TEXT);

        super.render(pose, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean onMouseDown(double screenX, double screenY, int button) {
        if (super.onMouseDown(screenX, screenY, button)) {
            return true;
        }

        focus();
        return true;
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.isFocused()) {
            this.textEditController.onKeyPressed(keyCode, scanCode, modifiers);

            if (keyCode == GLFW.GLFW_KEY_ENTER && this.enterPressedCallback != null) {
                this.enterPressedCallback.run();
            }
            return true;
        }

        return super.onKeyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean onCharTyped(char character, int modifiers) {
        if (this.isFocused()) {
            this.textEditController.onCharTyped(character, modifiers);
            return true;
        }

        return super.onCharTyped(character, modifiers);
    }

    @Override
    public void onLoseFocus() {
        this.textEditController.commitValue();
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setValueChangedCallback(Consumer<String> valueChangedCallback) {
        this.valueChangedCallback = valueChangedCallback;
    }

    public void setEnterPressedCallback(Runnable enterPressedCallback) {
        this.enterPressedCallback = enterPressedCallback;
    }
}
