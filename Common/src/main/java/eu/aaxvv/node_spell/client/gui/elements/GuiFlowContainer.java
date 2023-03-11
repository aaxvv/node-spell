package eu.aaxvv.node_spell.client.gui.elements;

import eu.aaxvv.node_spell.client.gui.GuiElement;

import java.util.List;

public class GuiFlowContainer extends GuiElement {
    private int innerPadding;
    private int itemGap;
    private int minElementWidth;
    private int elementHeight;

    public GuiFlowContainer(int width, int minElementWidth, int elementHeight) {
        super(width, 0);
        this.minElementWidth = minElementWidth;
        this.elementHeight = elementHeight;
        this.innerPadding = 2;
        this.itemGap = 2;
    }

    public void setInnerPadding(int innerPadding) {
        this.innerPadding = innerPadding;
    }

    public void setItemGap(int itemGap) {
        this.itemGap = itemGap;
    }

    public void setMinElementWidth(int minElementWidth) {
        this.minElementWidth = minElementWidth;
    }

    public void setElementHeight(int elementHeight) {
        this.elementHeight = elementHeight;
    }

    public void addAll(List<? extends GuiElement> children) {
        children.forEach(super::addChild);
        invalidateLayout();
    }

    @Override
    public GuiElement addChild(GuiElement child) {
        GuiElement newChild = super.addChild(child);
        invalidateLayout();
        return newChild;
    }

    @Override
    public void removeChild(GuiElement child) {
        super.removeChild(child);
        invalidateLayout();
    }

    @Override
    public void removeAllChildren() {
        super.removeAllChildren();
        invalidateLayout();
    }

    @Override
    public void invalidate() {
        invalidateLayout();
        super.invalidate();
    }

    private void invalidateLayout() {
        int innerWidth = this.getWidth() - 2*this.innerPadding;
        int itemsPerLine = Math.max((innerWidth + this.itemGap) / (this.minElementWidth + this.itemGap), 1);
        int itemWidth = ((innerWidth + this.itemGap) / itemsPerLine) - this.itemGap;
        int paddingLeft = (innerWidth - ((itemWidth + this.itemGap)*itemsPerLine) + this.itemGap) / 2;

        int x = this.innerPadding + paddingLeft;
        int y = this.innerPadding;

        for (GuiElement child : this.getChildren()) {
            child.setLocalPosition(x, y);
            child.setSize(itemWidth, this.elementHeight);

            x += this.itemGap + itemWidth;
            if (x + itemWidth > innerWidth + this.innerPadding) {
                x = this.innerPadding + paddingLeft;
                y += this.itemGap + this.elementHeight;
            }
        }

        // if the items exactly fit on the last line y will be incremented even though it doesn't need to be
        if (x == this.innerPadding + paddingLeft) {
            y -= this.itemGap + this.elementHeight;
        }

        this.height = y + this.elementHeight + this.innerPadding;
    }
}
