package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import eu.aaxvv.node_spell.spell.Spell;
import net.minecraft.client.Minecraft;

public class GuiSpellListItem extends GuiElement {
    private static final int ITEM_HEIGHT = 16;
    private static final int FAVORITE_SIZE = 12;

    private Runnable clickCallback;
    private String spellName;
    private Spell cachedSpell;
    private final GuiTextureButton favoriteButton;
    private boolean selected;
    private boolean favorited;

    public GuiSpellListItem(String spellName) {
        super(0, ITEM_HEIGHT);
        this.spellName = spellName;
        this.favoriteButton = new GuiTextureButton(FAVORITE_SIZE, FAVORITE_SIZE);
        this.addChild(this.favoriteButton);
    }

    public String getSpellName() {
        return this.spellName;
    }

    public void setSpellName(String spellName) {
        this.spellName = spellName;
    }

    public void setCachedSpell(Spell cachedSpell) {
        this.cachedSpell = cachedSpell;
    }

    public Spell getCachedSpell() {
        return cachedSpell;
    }

    public void setClickCallback(Runnable clickCallback) {
        this.clickCallback = clickCallback;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavoriteCallback(Runnable favoriteCallback) {
        this.favoriteButton.setClickCallback(favoriteCallback);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        //TODO: better colors
        RenderUtil.drawRect(pose, this.getGlobalX(), this.getGlobalY(), this.getWidth(), this.getHeight(), this.selected ? 0xFF888888 : 0xFF000000);
        RenderUtil.drawRect(pose, this.getGlobalX() + 1, this.getGlobalY() + 1, this.getWidth() - 2, this.getHeight() - 2, 0xFFFFFFFF);
        RenderUtil.drawTextTruncated(pose, Minecraft.getInstance().font, this.spellName, this.getGlobalX() + 2, this.getGlobalY() + 4, this.getWidth() - 6 - FAVORITE_SIZE, 0xFF000000);

        super.render(pose, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean onMouseDown(double screenX, double screenY, int button) {
        if (super.onMouseDown(screenX, screenY, button)) {
            return true;
        }

        if (this.clickCallback != null) {
            this.clickCallback.run();
            return true;
        }

        return false;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.favoriteButton.setLocalPosition(this.getWidth() - 2 - FAVORITE_SIZE, 2);
    }
}
