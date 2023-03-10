package eu.aaxvv.node_spell.client.gui.node_widget;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.gui.helper.TextEditController;
import eu.aaxvv.node_spell.spell.graph.runtime.InstanceDataContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;

import java.util.Locale;

public class NumberFieldWidget extends Widget<Double> {
    private final DecimalFormat format;
    protected final TextEditController textEditController;

    public NumberFieldWidget(InstanceDataContainer parent, int width) {
        super(parent, width, 12);
        this.format = new DecimalFormat("#0.##", DecimalFormatSymbols.getInstance(Locale.US));
        this.textEditController = new TextEditController();
        this.textEditController.setDisplayWidth(this.width - 4);
        this.textEditController.setRollbackValueProvider(() -> this.format.format(this.instance.getInstanceData()));
        this.textEditController.rollbackValue();
        this.textEditController.setDoneCallback(this::editDone);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        GuiComponent.fill(pose, this.getGlobalX(), this.getGlobalY(), this.getGlobalX() + this.getWidth(), this.getGlobalY() + this.getHeight(), 0xFFFFFFFF);
        GuiComponent.fill(pose, this.getGlobalX() + 1, this.getGlobalY() + 1, this.getGlobalX() + this.getWidth() - 1, this.getGlobalY() + this.getHeight() - 1, 0xFF000000);

        String displayString = this.textEditController.getDisplayString();
        Minecraft.getInstance().font.draw(pose, displayString, this.getGlobalX() + 2, this.getGlobalY() + 2, 0xFFFFFFFF);

        GuiComponent.fill(pose, this.getGlobalX() + this.getWidth() - 5, this.getGlobalY() + 3, this.getGlobalX() + this.getWidth() - 4, this.getGlobalY() + 4, 0xFFFFFFFF);
        GuiComponent.fill(pose, this.getGlobalX() + this.getWidth() - 6, this.getGlobalY() + 4, this.getGlobalX() + this.getWidth() - 3, this.getGlobalY() + 5, 0xFFFFFFFF);

        GuiComponent.fill(pose, this.getGlobalX() + this.getWidth() - 6, this.getGlobalY() + this.getHeight() - 4, this.getGlobalX() + this.getWidth() - 3, this.getGlobalY() + this.getHeight() - 5, 0xFFFFFFFF);
        GuiComponent.fill(pose, this.getGlobalX() + this.getWidth() - 5, this.getGlobalY() + this.getHeight() - 3, this.getGlobalX() + this.getWidth() - 4, this.getGlobalY() + this.getHeight() - 4, 0xFFFFFFFF);
    }

    private boolean editDone(String text) {
        if (text == null) {
            return false;
        }

        try {
            this.currentValue = Double.parseDouble(this.textEditController.getText());
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
    public boolean onMouseDown(double screenX, double screenY, int button) {
        this.requestFocus();
        if (screenX >= this.getGlobalX() + this.getWidth() - 7 && screenX < this.getGlobalX() + this.getWidth() - 1) {
            // handle spinner buttons
            if (screenY < this.getGlobalY() + (this.getHeight() / 2f)) {
                this.currentValue += 1;
            } else if (screenY > this.getGlobalY() + (this.getHeight() / 2f)) {
                this.currentValue -= 1;
            }
            commitValue();
            this.textEditController.rollbackValue();
        } else {
            this.textEditController.startEditing(valueAsString());
        }
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
        this.currentValue = (Double) this.instance.getInstanceData();
        this.textEditController.rollbackValue();
        releaseFocus();
    }

    @Override
    public void onLoseFocus() {
        this.textEditController.commitValue();
    }

    private String valueAsString() {
        return this.format.format(this.currentValue);
    }
}