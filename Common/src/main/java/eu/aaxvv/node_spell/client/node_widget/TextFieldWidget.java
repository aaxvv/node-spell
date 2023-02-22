package eu.aaxvv.node_spell.client.node_widget;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import org.lwjgl.glfw.GLFW;

public class TextFieldWidget extends Widget<String> {
    private int cursorPos;
    public TextFieldWidget(NodeInstance parent, int width) {
        super(parent, width, 13);
        this.cursorPos = 0;
    }

    @Override
    public void draw(PoseStack pose, int x, int y) {
        GuiComponent.fill(pose, x, y, x + this.width, y + this.height, 0xFFFFFFFF);
        GuiComponent.fill(pose, x + 1, y + 1, x + this.width - 1, y + this.height - 1, 0xFF000000);

        String displayString = getStringWithCursor();
        if (!displayString.isEmpty()) {
            String text = Minecraft.getInstance().font.plainSubstrByWidth(displayString, this.width - 4, this.focused);
            Minecraft.getInstance().font.draw(pose, text, x + 2, y + 2, 0xFFFFFFFF);
        }
    }

    private String getStringWithCursor() {
        if (!this.focused) {
            return this.currentValue;
        }

        if (this.cursorPos == 0) {
            return '_' + this.currentValue;
        } else {
            return this.currentValue.substring(0, this.cursorPos) + "_" + this.currentValue.substring(this.cursorPos);
        }
    }

    @Override
    public void receiveKeyPress(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            commitValue();
            return;
        } else if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            rollbackValue();
            this.cursorPos = this.currentValue.length();
            return;
        } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (this.cursorPos > 0) {
                this.currentValue = this.currentValue.substring(0, this.cursorPos - 1) + this.currentValue.substring(this.cursorPos);
                this.cursorPos -= 1;
            }
        } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
            this.cursorPos = Math.max(this.cursorPos - 1, 0);
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            this.cursorPos = Math.min(this.cursorPos + 1, this.currentValue.length());
        } else if (keyCode == GLFW.GLFW_KEY_HOME) {
            this.cursorPos = 0;
        } else if (keyCode == GLFW.GLFW_KEY_END) {
            this.cursorPos = this.currentValue.length();
        }
    }

    @Override
    public void receiveCharTyped(char character, int modifiers) {
        if (this.cursorPos == 0) {
            this.currentValue = character + this.currentValue;
        } else {
            this.currentValue =  this.currentValue.substring(0, this.cursorPos) + character + this.currentValue.substring(this.cursorPos);
        }
        this.cursorPos += 1;
    }

    @Override
    public void receiveMouseInput(int mouseX, int mouseY, int button) {
        super.receiveMouseInput(mouseX, mouseY, button);
        this.cursorPos = this.currentValue.length();
    }

    @Override
    protected String getDefaultValue() {
        return "";
    }
}
