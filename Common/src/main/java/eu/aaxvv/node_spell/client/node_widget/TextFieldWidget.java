package eu.aaxvv.node_spell.client.node_widget;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.gui.TextEditController;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;

public class TextFieldWidget extends Widget<String> {
    private final TextEditController textEditController;
    public TextFieldWidget(NodeInstance parent, int width) {
        super(parent, width, 12);
        this.textEditController = new TextEditController();
        this.textEditController.setDisplayWidth(this.getWidth() - 4);
        this.textEditController.setRollbackValueProvider(() -> (String) this.instance.getInstanceData());
        this.textEditController.rollbackValue();
        this.textEditController.setDoneCallback(this::editDone);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        GuiComponent.fill(pose, this.getGlobalX(), this.getGlobalY(), this.getGlobalX() + this.getWidth(), this.getGlobalY() + this.getHeight(), 0xFFFFFFFF);
        GuiComponent.fill(pose, this.getGlobalX() + 1, this.getGlobalY() + 1, this.getGlobalX() + this.getWidth() - 1, this.getGlobalY() + this.getHeight() - 1, 0xFF000000);

        String displayString = this.textEditController.getDisplayString();
        Minecraft.getInstance().font.draw(pose, displayString, this.getGlobalX() + 2, this.getGlobalY() + 2, 0xFFFFFFFF);
    }

    private boolean editDone(String text) {
        if (text == null) {
            return false;
        }

        try {
            this.currentValue = text;
            commitValue();
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isFocused()) {
            return false;
        }

        this.textEditController.onKeyPressed(keyCode, scanCode, modifiers);
        return true;
    }

    @Override
    public boolean onCharTyped(char character, int modifiers) {
        if (!this.isFocused()) {
            return false;
        }

        this.textEditController.onCharTyped(character, modifiers);
        return true;
    }

    @Override
    public boolean onMouseDown(double mouseX, double mouseY, int button) {
        this.requestFocus();
        this.textEditController.startEditing(this.currentValue);
        return true;
    }

    // need to override these, because calling releaseFocus() before textEditController.rollbackValue()
    // would cause the textEditController to commit its old value due to losing focus
    @Override
    public void commitValue() {
        this.instance.setInstanceData(this.currentValue);
        this.textEditController.rollbackValue();
        releaseFocus();
    }

    @Override
    public void rollbackValue() {
        this.currentValue = (String) this.instance.getInstanceData();
        this.textEditController.rollbackValue();
        releaseFocus();
    }

    @Override
    public void onLoseFocus() {
        this.textEditController.commitValue();
    }
}
