package eu.aaxvv.node_spell.client.node_widget;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;

public class NumberFieldWidget extends Widget<Double> {
    private int cursorPos;
    private String currentStringValue;
    DecimalFormat format;
    public NumberFieldWidget(NodeInstance parent, int width) {
        super(parent, width, 13);
        this.cursorPos = 0;
        this.format = new DecimalFormat("#0.##", DecimalFormatSymbols.getInstance(Locale.US));
        updateStringRepresentation();
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        GuiComponent.fill(pose, this.getGlobalX(), this.getGlobalY(), this.getGlobalX() + this.getWidth(), this.getGlobalY() + this.getHeight(), 0xFFFFFFFF);
        GuiComponent.fill(pose, this.getGlobalX() + 1, this.getGlobalY() + 1, this.getGlobalX() + this.getWidth() - 1, this.getGlobalY() + this.getHeight() - 1, 0xFF000000);

        String displayString = getStringWithCursor();
        if (!displayString.isEmpty()) {
            String text = Minecraft.getInstance().font.plainSubstrByWidth(displayString, this.getWidth() - 4 - 4, this.isFocused());
            Minecraft.getInstance().font.draw(pose, text, this.getGlobalX() + 2, this.getGlobalY() + 2, 0xFFFFFFFF);
        }

        GuiComponent.fill(pose, this.getGlobalX() + this.getWidth() - 5, this.getGlobalY() + 3, this.getGlobalX() + this.getWidth() - 4, this.getGlobalY() + 4, 0xFFFFFFFF);
        GuiComponent.fill(pose, this.getGlobalX() + this.getWidth() - 6, this.getGlobalY() + 4, this.getGlobalX() + this.getWidth() - 3, this.getGlobalY() + 5, 0xFFFFFFFF);

        GuiComponent.fill(pose, this.getGlobalX() + this.getWidth() - 6, this.getGlobalY() + this.getHeight() - 4, this.getGlobalX() + this.getWidth() - 3, this.getGlobalY() + this.getHeight() - 5, 0xFFFFFFFF);
        GuiComponent.fill(pose, this.getGlobalX() + this.getWidth() - 5, this.getGlobalY() + this.getHeight() - 3, this.getGlobalX() + this.getWidth() - 4, this.getGlobalY() + this.getHeight() - 4, 0xFFFFFFFF);
    }

    private String getStringWithCursor() {
        if (!this.focused) {
            return this.currentStringValue;
        }

        if (this.cursorPos == 0) {
            return '_' + this.currentStringValue;
        } else {
            return this.currentStringValue.substring(0, this.cursorPos) + "_" + this.currentStringValue.substring(this.cursorPos);
        }
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isFocused()) {
            return false;
        }

        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            commitFromText();
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            rollbackValue();
            this.cursorPos = this.currentStringValue.length();
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (this.cursorPos > 0) {
                this.currentStringValue = this.currentStringValue.substring(0, this.cursorPos - 1) + this.currentStringValue.substring(this.cursorPos);
                this.cursorPos -= 1;
            }
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
            this.cursorPos = Math.max(this.cursorPos - 1, 0);
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            this.cursorPos = Math.min(this.cursorPos + 1, this.currentStringValue.length());
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_HOME) {
            this.cursorPos = 0;
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_END) {
            this.cursorPos = this.currentStringValue.length();
            return true;
        }

        return false;
    }

    @Override
    public boolean onCharTyped(char character, int modifiers) {
        if (!this.isFocused()) {
            return false;
        }

        if (this.cursorPos == 0) {
            this.currentStringValue = character + this.currentStringValue;
        } else {
            this.currentStringValue =  this.currentStringValue.substring(0, this.cursorPos) + character + this.currentStringValue.substring(this.cursorPos);
        }
        this.cursorPos += 1;
        return true;
    }

    @Override
    public boolean onMouseDown(double screenX, double screenY, int button) {
        this.requestFocus();
        if (screenX >= this.getGlobalX() + this.getWidth() - 7 && screenX < this.getGlobalX() + this.getWidth() - 1) {
            // handle spinner buttons
            if (screenY < this.getGlobalY() + (this.getHeight() / 2f)) {
                this.currentValue += 1;
            } else if (screenY > this.getGlobalY() + (this.getHeight() / 2f)) {
                this.currentValue -= 1;
            }
            commitValue();
            updateStringRepresentation();
        } else {
            this.cursorPos = this.currentStringValue.length();
        }
        return true;
    }

    private void commitFromText() {
        try {
            this.currentValue = Double.parseDouble(this.currentStringValue);
        } catch (NumberFormatException ex) {
            rollbackValue();
        }

        commitValue();
    }

    @Override
    public void commitValue() {
        this.parent.setInstanceData(this.currentValue);
        updateStringRepresentation();
        releaseFocus();
    }

    @Override
    public void rollbackValue() {
        this.currentValue = (Double) this.parent.getInstanceData();
        updateStringRepresentation();
        releaseFocus();
    }

    @Override
    public void onLoseFocus() {
        commitFromText();
    }

    private void updateStringRepresentation() {
        DecimalFormat format = new DecimalFormat("#0.##");
        this.currentStringValue = format.format(this.currentValue);
    }
}