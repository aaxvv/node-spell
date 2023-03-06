package eu.aaxvv.node_spell.client.gui;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MultiSelectionModel {
    private GuiElement lastSelectedItem;
    private final Set<GuiElement> selectedItems;
    private boolean contiguousSelectionPossible;

    public MultiSelectionModel() {
        this.selectedItems = new HashSet<>();
        this.lastSelectedItem = null;
        this.contiguousSelectionPossible = true;
    }

    public void clickItem(GuiElement item) {
        if (GuiHelper.isShiftDown() && this.contiguousSelectionPossible) {
            if (this.lastSelectedItem == null) {
                this.lastSelectedItem = item;
                this.selectedItems.add(item);
            } else {
                int startIdx = this.lastSelectedItem.getIndexInParent();
                int endIdx = item.getIndexInParent();

                if (endIdx < startIdx) {
                    int tmp = endIdx;
                    endIdx = startIdx;
                    startIdx = tmp;
                }

                GuiElement parentContainer = item.getParent();
                for (int i = startIdx; i <= endIdx; i++) {
                    if (i < 0 || i >= parentContainer.getChildren().size()) {
                        continue;
                    }
                    GuiElement addedItem = parentContainer.getChildren().get(i);
                    this.selectedItems.add(addedItem);
                }
            }
        } else if (GuiHelper.isControlDown()) {
            if (isSelected(item)) {
                this.selectedItems.remove(item);
                if (this.lastSelectedItem == item) {
                    this.lastSelectedItem = null;
                }
            } else {
                this.selectedItems.add(item);
                this.lastSelectedItem = item;
            }
        } else {
            this.lastSelectedItem = item;
            this.selectedItems.clear();
            this.selectedItems.add(item);
        }
    }

    public void selectItem(GuiElement item) {
        this.selectedItems.add(item);
    }
    public void deselectItem(GuiElement item) {
        this.selectedItems.remove(item);
    }

    public boolean isSelected(GuiElement item) {
        return this.selectedItems.contains(item);
    }

    public int getSelectionCount() {
        return this.selectedItems.size();
    }

    public Set<GuiElement> getSelectedItems() {
        return Collections.unmodifiableSet(this.selectedItems);
    }

    public void setSelectedItems(Collection<GuiElement> items) {
        this.selectedItems.clear();
        this.selectedItems.addAll(items);
        this.lastSelectedItem = null;
    }

    public void deselectAll() {
        this.selectedItems.clear();
        this.lastSelectedItem = null;
    }

    public void setContiguousSelectionPossible(boolean contiguousSelectionPossible) {
        this.contiguousSelectionPossible = contiguousSelectionPossible;
    }
}
