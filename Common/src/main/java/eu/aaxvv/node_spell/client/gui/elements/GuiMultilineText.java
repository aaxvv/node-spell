package eu.aaxvv.node_spell.client.gui.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class GuiMultilineText extends GuiElement {
    private static final int LINE_OFFSET = 13;
    private List<Component> lines;
    private int textColor;


    public GuiMultilineText() {
        super(0, 0);
        this.lines = new ArrayList<>();
        this.textColor = ModConstants.Colors.RED;
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        Font font = Minecraft.getInstance().font;

        for (int i = 0; i < this.lines.size(); i++) {
            font.draw(pose, lines.get(i), this.getGlobalX(), this.getGlobalY() + (i * LINE_OFFSET), this.textColor);
        }

        super.render(pose, mouseX, mouseY, tickDelta);
    }

    public List<Component> getLines() {
        return lines;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public boolean containsPointGlobal(double x, double y) {
        return false;
    }

    @Override
    public boolean containsPointLocal(double x, double y) {
        return false;
    }
}
