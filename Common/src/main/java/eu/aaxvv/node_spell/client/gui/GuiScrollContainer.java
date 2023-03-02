package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import net.minecraft.client.Minecraft;

import java.util.List;

public class GuiScrollContainer extends GuiElement {
    // Appearance
    private int innerPadding;
    private int itemSeparation;
    private int scrollBarWidth;
    private int scrollBarColor;
    private int scrollTrackColor;

    // Functionality
    private int scrollPosition;
    private int scrollBarHeight;
    private int scrollBarPos;

    private final GuiElement contentPane;

    public GuiScrollContainer(int width, int height) {
        super(width, height);
        this.innerPadding = 2;
        this.itemSeparation = this.innerPadding;
        this.scrollBarWidth = 4;
        this.scrollPosition = 0;
        this.scrollBarColor = 0x55000000;
        this.scrollTrackColor = 0x22000000;
        this.contentPane = new GuiElement(getContentPaneWidth(), this.getHeight());
        super.addChild(this.contentPane);
        this.contentPane.setLocalPosition(0, 0);
        invalidateScrollbar();
    }

    public void setInnerPadding(int innerPadding) {
        this.innerPadding = innerPadding;
    }

    public void setScrollBarWidth(int scrollBarWidth) {
        this.scrollBarWidth = scrollBarWidth;
    }

    public void setScrollBarColor(int scrollBarColor) {
        this.scrollBarColor = scrollBarColor;
    }

    public void setScrollTrackColor(int scrollTrackColor) {
        this.scrollTrackColor = scrollTrackColor;
    }

    public void setItemSeparation(int itemSeparation) {
        this.itemSeparation = itemSeparation;
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        double scale = Minecraft.getInstance().getWindow().getGuiScale();
        int framebufferHeight = Minecraft.getInstance().getWindow().getHeight();
        RenderSystem.enableScissor((int)(this.getGlobalX()*scale), framebufferHeight - ((int)(this.getGlobalY()*scale) + (int)(this.getHeight()*scale)), (int)(this.getWidth()*scale), (int)(this.getHeight()*scale));

        for (GuiElement child : this.contentPane.getChildren()) {
            if (!this.intersects(child)) {
                // outside viewport
                continue;
            }

            child.render(pose, mouseX, mouseY, tickDelta);
        }

        // scrollbar
        RenderUtil.drawRect(pose, this.getGlobalX() + this.getWidth() - this.scrollBarWidth, this.getGlobalY(), scrollBarWidth, this.getHeight(), scrollTrackColor);
        RenderUtil.drawRect(pose, this.getGlobalX() + this.getWidth() - this.scrollBarWidth, this.getGlobalY() + scrollBarPos, scrollBarWidth, scrollBarHeight, scrollBarColor);
        RenderSystem.disableScissor();
    }

    @Override
    public GuiElement addChild(GuiElement child) {
        GuiElement result = this.contentPane.addChild(child);
        invalidateLayout();
        return result;
    }

    @Override
    public void removeChild(GuiElement child) {
        this.contentPane.removeChild(child);
        invalidateLayout();
    }

    @Override
    public List<GuiElement> getChildren() {
        return this.contentPane.getChildren();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.contentPane.invalidate();
        invalidateScrollbar();
    }

    @Override
    public boolean onMouseScrolled(double screenX, double screenY, double amount) {
        this.scrollPosition -= amount * 4;

        scrollTo(this.scrollPosition);
        return true;
    }

    private void scrollTo(int scrollPosition) {
        if (scrollPosition < 0 || this.contentPane.getHeight() <= this.scrollBarHeight) {
            this.scrollPosition = 0;
        } else if (scrollPosition > this.contentPane.getHeight() - this.getHeight()) {
            this.scrollPosition = this.contentPane.getHeight() - this.getHeight();
        } else {
            this.scrollPosition = scrollPosition;
        }

        this.contentPane.setLocalPosition(0, -this.scrollPosition);
        invalidateScrollbar();
    }

    @Override
    public boolean onMouseDown(double screenX, double screenY, int button) {
        if (screenX < this.getGlobalX() + this.getWidth() - scrollBarWidth) {
            return super.onMouseDown(screenX, screenY, button);
        }

        if (!(screenY >= this.getGlobalY() + scrollBarPos && screenY < this.getGlobalY() + scrollBarPos + scrollBarHeight)) {
            // clicked above/below scrollbar
            double percentage = (screenY - this.getGlobalY()) / this.getHeight();
            int requestedY = (int)(percentage * (this.contentPane.getHeight() - this.getHeight()));
            scrollTo(requestedY);
        }

        // technically not correct when grabbing the scrollbar.
        // should store the offset the bar was grabbed at to not jump slightly once dragging starts
        // not a huge issue though
        this.requestFocus();

        return true;
    }

    @Override
    public boolean onMouseDragged(double screenX, double screenY, int buttons, double dX, double dY) {
        if (!this.isFocused()) {
            return super.onMouseDragged(screenX, screenY, buttons, dX, dY);
        }

        double percentage = (screenY - this.getGlobalY()) / this.getHeight();
        int requestedY = (int)(percentage * (this.contentPane.getHeight() - this.getHeight()));
        scrollTo(requestedY);

        return true;
    }

    @Override
    public boolean onMouseUp(double screenX, double screenY, int button) {
        if (!this.isFocused()) {
            return super.onMouseUp(screenX, screenY, button);
        }

        this.releaseFocus();
        return true;
    }

    public void invalidateLayout() {
        this.contentPane.setWidth(getContentPaneWidth());
        int contentPaneHeight = this.innerPadding;

        for (GuiElement child : this.getChildren()) {
            child.setLocalPosition(this.innerPadding, contentPaneHeight);
            child.setWidth(this.contentPane.getWidth() - (2 * this.innerPadding));
            contentPaneHeight += child.getHeight() + this.itemSeparation;
        }

        contentPaneHeight = contentPaneHeight - this.itemSeparation + this.innerPadding;

        this.contentPane.setHeight(contentPaneHeight);
        this.scrollPosition = Math.max(0, Math.min(this.scrollPosition, contentPaneHeight - this.getHeight()));
        invalidateScrollbar();
    }

    private void invalidateScrollbar() {
        if (this.contentPane.getHeight() <= this.getHeight()) {
            this.scrollBarHeight = this.getHeight();
        } else {
            this.scrollBarHeight = (int)Math.ceil(this.getHeight() * (this.getHeight() / (float)this.contentPane.getHeight()));
        }

        if (this.contentPane.getHeight() == this.getHeight()) {
            this.scrollBarPos = 0;
        } else {
            this.scrollBarPos = (int)Math.floor((this.getHeight() - scrollBarHeight) * (this.scrollPosition / (float)(this.contentPane.getHeight() - this.getHeight())));
        }
    }

    private int getContentPaneWidth() {
        return this.getWidth() - this.scrollBarWidth;
    }
}
