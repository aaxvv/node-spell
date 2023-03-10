package eu.aaxvv.node_spell.client.gui.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TextEditController {
    private String text;
    private String displayText;
    private int cursorPos;
    private int displayWidth;
    private int displayStartIdx;
    private boolean isFocused;
    private Predicate<String> doneCallback;
    private Supplier<String> rollbackValueProvider;
    private Consumer<String> displayTextChangedCallback;

    public TextEditController() {
        this.text = "";
        this.displayText = "";
        this.cursorPos = 0;
        this.displayWidth = 2048;
        this.displayStartIdx = 0;
    }

    public void onCharTyped(char character, int modifiers) {
        if (this.cursorPos == 0) {
            this.text = character + this.text;
        } else {
            this.text = this.text.substring(0, this.cursorPos) + character + this.text.substring(this.cursorPos);
        }
        moveCursorBy(1);
    }

    public void onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            commitValue();
        } else if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            rollbackValue();
            moveCursorToEnd();
        } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (this.cursorPos > 0) {
                this.text = this.text.substring(0, this.cursorPos - 1) + this.text.substring(this.cursorPos);
                moveCursorBy(-1);
            }
        } else if (keyCode == GLFW.GLFW_KEY_DELETE) {
            if (this.cursorPos < this.text.length()) {
                this.text = this.text.substring(0, this.cursorPos) + this.text.substring(this.cursorPos + 1);
            }
        } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
            moveCursorBy(-1);
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            moveCursorBy(1);
        } else if (keyCode == GLFW.GLFW_KEY_HOME) {
            moveCursorToStart();
        } else if (keyCode == GLFW.GLFW_KEY_END) {
            moveCursorToEnd();
        } else {
            return;
        }

        updateDisplayString();
    }

    public void commitValue() {
        if (this.doneCallback != null && !this.doneCallback.test(this.text)) {
            rollbackValue();
        }

        setFocused(false);
        updateDisplayString();
    }

    public void rollbackValue() {
        if (this.rollbackValueProvider != null) {
            this.text = this.rollbackValueProvider.get();
            this.moveCursorToEnd();
        }
        setFocused(false);
        updateDisplayString();
        if (this.doneCallback != null) {
            this.doneCallback.test(null);
        }
    }


    private void updateDisplayString() {
        String stringWithCursor;

        if (!this.isFocused) {
            stringWithCursor = this.text;
        } else if (this.cursorPos == 0) {
            stringWithCursor =  '_' + this.text;
        } else {
            stringWithCursor = this.text.substring(0, this.cursorPos) + "_" + this.text.substring(this.cursorPos);
        }

        String displayedWindow = stringWithCursor.substring(this.displayStartIdx);

        this.displayText = Minecraft.getInstance().font.plainSubstrByWidth(displayedWindow, this.displayWidth);
        if (displayTextChangedCallback != null) {
            displayTextChangedCallback.accept(this.displayText);
        }
    }

    private void moveCursorBy(int offset) {
        this.cursorPos = Mth.clamp(this.cursorPos + offset, 0, this.text.length());
        if (this.cursorPos < this.displayStartIdx) {
            // passed left edge
            this.displayStartIdx = Mth.clamp(this.displayStartIdx - (this.displayWidth / 12), 0, this.text.length());
        } else if (this.cursorPos >= this.displayStartIdx + this.displayText.length()) {
            // passed right edge
            String upToCursor = this.text.substring(0, this.cursorPos) + "_";
            String fitToWindow = Minecraft.getInstance().font.plainSubstrByWidth(upToCursor, this.displayWidth, true);
            this.displayStartIdx = Mth.clamp(this.cursorPos - fitToWindow.length() + 1, 0, this.text.length());
        }
        updateDisplayString();
    }

    private void moveCursorToEnd() {
        moveCursorBy(100000);
    }

    private void moveCursorToStart() {
        this.cursorPos = 0;
        this.displayStartIdx = 0;
    }

    public String getDisplayString() {
        return this.displayText;
    }

    public String getText() {
        return text;
    }

    public void startEditing(String text) {
        this.text = text;
        this.setFocused(true);
        updateDisplayString();
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
        if (!this.isFocused) {
            this.displayStartIdx = 0;
            this.cursorPos = 0;
        } else {
            moveCursorToEnd();
        }
    }

    public boolean isFocused() {
        return isFocused;
    }

    public void setDoneCallback(Predicate<String> commitHandler) {
        this.doneCallback = commitHandler;
    }

    public void setRollbackValueProvider(Supplier<String> rollbackValueProvider) {
        this.rollbackValueProvider = rollbackValueProvider;
    }

    public void setDisplayTextChangedCallback(Consumer<String> displayTextChangedCallback) {
        this.displayTextChangedCallback = displayTextChangedCallback;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public void setText(String text) {
        this.text = text;
    }
}
