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
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        GuiComponent.fill(pose, this.getGlobalX(), this.getGlobalY(), this.getGlobalX() + this.getWidth(), this.getGlobalY() + this.getHeight(), 0xFFFFFFFF);
        GuiComponent.fill(pose, this.getGlobalX() + 1, this.getGlobalY() + 1, this.getGlobalX() + this.getWidth() - 1, this.getGlobalY() + this.getHeight() - 1, 0xFF000000);

        String displayString = getStringWithCursor();
        if (!displayString.isEmpty()) {
            //TODO: string doesn't scroll back when cursor exists visible window
            String text = Minecraft.getInstance().font.plainSubstrByWidth(displayString, this.getWidth() - 4, this.focused);
            Minecraft.getInstance().font.draw(pose, text, this.getGlobalX() + 2, this.getGlobalY() + 2, 0xFFFFFFFF);
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
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isFocused()) {
            return false;
        }

        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            commitValue();
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            rollbackValue();
            this.cursorPos = this.currentValue.length();
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (this.cursorPos > 0) {
                this.currentValue = this.currentValue.substring(0, this.cursorPos - 1) + this.currentValue.substring(this.cursorPos);
                this.cursorPos -= 1;
            }
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
            this.cursorPos = Math.max(this.cursorPos - 1, 0);
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            this.cursorPos = Math.min(this.cursorPos + 1, this.currentValue.length());
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_HOME) {
            this.cursorPos = 0;
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_END) {
            this.cursorPos = this.currentValue.length();
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
            this.currentValue = character + this.currentValue;
        } else {
            this.currentValue =  this.currentValue.substring(0, this.cursorPos) + character + this.currentValue.substring(this.cursorPos);
        }
        this.cursorPos += 1;
        return true;
    }

    @Override
    public boolean onMouseDown(double mouseX, double mouseY, int button) {
        this.requestFocus();
        this.cursorPos = this.currentValue.length();
        return true;
    }
}
