package eu.aaxvv.node_spell.client.gui.elements;

import eu.aaxvv.node_spell.client.gui.GuiElement;
import net.minecraft.util.Mth;

public class GuiPopupPane extends UnboundedGuiElement {
    public GuiPopupPane() {
        super();
    }

    public void openPopup(GuiElement popup, int x, int y) {
        this.addChild(popup);
        int popupX = Mth.clamp(x, 2, this.getWidth() - 2 - popup.getWidth());
        int popupY = Mth.clamp(y, 2, this.getHeight() - 2 - popup.getHeight());
        popup.setLocalPosition(popupX, popupY);
    }

    public void closePopup(GuiElement popup) {
        int index = getChildren().indexOf(popup);

        if (index == -1) {
            return;
        }

        for (int i = this.getChildren().size() - 1; i >= index; i--) {
            GuiElement child = this.getChildren().get(i);
            if (this.getContext().getFocused() != null && this.getContext().getFocused().isChildOf(child)) {
                this.getContext().setFocused(null);
            }
            this.removeChild(child);
        }
    }

    public void closeAllPopups() {
        if (this.getContext().getFocused() != null) {
            GuiElement focused = this.getContext().getFocused();
            if (this.getChildren().stream().anyMatch(focused::isChildOf)) {
                this.getContext().setFocused(null);
            }
        }

        this.removeAllChildren();
    }

    @Override
    public boolean onMouseDown(double screenX, double screenY, int button) {
        if (this.getChildren().isEmpty()) {
            return false;
        }

        GuiElement topMostChild = this.getChildren().get(this.getChildren().size() - 1);

        // if there is an active popup it always absorbs the event, either being hit or closing
        if (topMostChild.containsPointGlobal(screenX, screenY)) {
            topMostChild.onMouseDown(screenX, screenY, button);
        } else {
            closePopup(topMostChild);
        }
        return true;
    }

    @Override
    public boolean onMouseUp(double screenX, double screenY, int button) {
        if (this.getChildren().isEmpty()) {
            return false;
        }

        GuiElement topMostChild = this.getChildren().get(this.getChildren().size() - 1);

        if (topMostChild.containsPointGlobal(screenX, screenY)) {
            topMostChild.onMouseUp(screenX, screenY, button);
        }
        return true;
    }

    @Override
    public boolean onMouseScrolled(double screenX, double screenY, double amount) {
        if (this.getChildren().isEmpty()) {
            return false;
        }

        super.onMouseScrolled(screenX, screenY, amount);
        return true;
    }

    @Override
    public boolean onMouseMoved(double screenX, double screenY, double dX, double dY) {
        if (this.getChildren().isEmpty()) {
            return false;
        }

        super.onMouseMoved(screenX, screenY, dX, dY);
        return true;
    }

    @Override
    public boolean onMouseDragged(double screenX, double screenY, int button, double dX, double dY) {
        if (this.getChildren().isEmpty()) {
            return false;
        }

        super.onMouseDragged(screenX, screenY, button, dX, dY);
        return true;
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.getChildren().isEmpty()) {
            return false;
        }

        super.onKeyPressed(keyCode, scanCode, modifiers);
        return true;
    }

    @Override
    public boolean onCharTyped(char character, int modifiers) {
        if (this.getChildren().isEmpty()) {
            return false;
        }

        super.onCharTyped(character, modifiers);
        return true;
    }
}
