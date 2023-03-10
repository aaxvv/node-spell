package eu.aaxvv.node_spell.client.gui.elements;

import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.gui.elements.UnboundedGuiElement;

public class GuiPopupPane extends UnboundedGuiElement {
    public GuiPopupPane() {
        super();
    }

    public void openPopup(GuiElement popup) {
        this.addChild(popup);
    }

    public void closePopup(GuiElement popup) {
        int index = getChildren().indexOf(popup);

        if (index == -1) {
            return;
        }

        for (int i = this.getChildren().size() - 1; i >= index; i--) {
            GuiElement child = this.getChildren().get(i);
            this.removeChild(child);
        }
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
            this.removeChild(topMostChild);
        }
        return true;
    }
}
