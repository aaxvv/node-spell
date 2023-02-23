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
    public void draw(PoseStack pose, int x, int y) {
        GuiComponent.fill(pose, x, y, x + this.width, y + this.height, 0xFFFFFFFF);
        GuiComponent.fill(pose, x + 1, y + 1, x + this.width - 1, y + this.height - 1, 0xFF000000);

        String displayString = getStringWithCursor();
        if (!displayString.isEmpty()) {
            String text = Minecraft.getInstance().font.plainSubstrByWidth(displayString, this.width - 4 - 4, this.focused);
            Minecraft.getInstance().font.draw(pose, text, x + 2, y + 2, 0xFFFFFFFF);
        }

        GuiComponent.fill(pose, x + this.width - 5, y + 3, x + this.width - 4, y + 4, 0xFFFFFFFF);
        GuiComponent.fill(pose, x + this.width - 6, y + 4, x + this.width - 3, y + 5, 0xFFFFFFFF);

        GuiComponent.fill(pose, x + this.width - 6, y + this.height - 4, x + this.width - 3, y + this.height - 5, 0xFFFFFFFF);
        GuiComponent.fill(pose, x + this.width - 5, y + this.height - 3, x + this.width - 4, y + this.height - 4, 0xFFFFFFFF);
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
    public void receiveKeyPress(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            try {
                this.currentValue = Double.parseDouble(this.currentStringValue);
            } catch (NumberFormatException ex) {
                rollbackValue();
                return;
            }

            commitValue();
            return;
        } else if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            rollbackValue();
            this.cursorPos = this.currentStringValue.length();
            return;
        } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (this.cursorPos > 0) {
                this.currentStringValue = this.currentStringValue.substring(0, this.cursorPos - 1) + this.currentStringValue.substring(this.cursorPos);
                this.cursorPos -= 1;
            }
        } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
            this.cursorPos = Math.max(this.cursorPos - 1, 0);
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            this.cursorPos = Math.min(this.cursorPos + 1, this.currentStringValue.length());
        } else if (keyCode == GLFW.GLFW_KEY_HOME) {
            this.cursorPos = 0;
        } else if (keyCode == GLFW.GLFW_KEY_END) {
            this.cursorPos = this.currentStringValue.length();
        }
    }

    @Override
    public void receiveCharTyped(char character, int modifiers) {
        if (this.cursorPos == 0) {
            this.currentStringValue = character + this.currentStringValue;
        } else {
            this.currentStringValue =  this.currentStringValue.substring(0, this.cursorPos) + character + this.currentStringValue.substring(this.cursorPos);
        }
        this.cursorPos += 1;
    }

    @Override
    public void receiveMouseInput(int mouseX, int mouseY, int button) {
        super.receiveMouseInput(mouseX, mouseY, button);

        int x = getX();
        int y = getY();
        if (mouseX >= x + this.width - 7 && mouseX < x + this.width - 1) {
            // handle spinner buttons
            if (mouseY < y + (this.height / 2)) {
                this.currentValue += 1;
            } else if (mouseY > y + (this.height / 2)) {
                this.currentValue -= 1;
            }
            commitValue();
            updateStringRepresentation();
        } else {
            this.cursorPos = this.currentStringValue.length();
        }
    }

    @Override
    public void commitValue() {
        super.commitValue();
        updateStringRepresentation();
    }

    @Override
    public void rollbackValue() {
        super.rollbackValue();
        updateStringRepresentation();
    }

    private void updateStringRepresentation() {
        DecimalFormat format = new DecimalFormat("#0.##");
        this.currentStringValue = format.format(this.currentValue);
    }

    @Override
    protected Double getDefaultValue() {
        return 0.0;
    }
}