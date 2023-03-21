package eu.aaxvv.node_spell.client.gui.spell_list;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.gui.elements.GuiTextureButton;
import eu.aaxvv.node_spell.client.gui.helper.TextureRegion;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import eu.aaxvv.node_spell.spell.Spell;
import net.minecraft.client.Minecraft;

import java.util.UUID;
import java.util.function.Supplier;

public class GuiSpellListItem extends GuiElement {
    private static final int ITEM_HEIGHT = 16;
    public static final int FAVORITE_SIZE = 12;
    private static final TextureRegion FAVORITE_BUTTON_TEXTURE = new TextureRegion(ModConstants.resLoc("textures/gui/spell_book_bg.png"), 136, 188, 12, 12);

    private Runnable clickCallback;
    private String spellName;
    private UUID spellId;
    private Spell cachedSpell;
    private final GuiTextureButton favoriteButton;
    private boolean selected;
    private boolean isFavorite;
    private Supplier<String> displayNameOverrideSupplier;

    public GuiSpellListItem(String spellName, UUID spellId) {
        super(0, ITEM_HEIGHT);
        this.spellName = spellName;
        this.spellId = spellId;
        this.favoriteButton = new GuiTextureButton(FAVORITE_SIZE, FAVORITE_SIZE);
        this.favoriteButton.setTexture(FAVORITE_BUTTON_TEXTURE);
        this.favoriteButton.setDrawHoverOverlay(false);
        this.addChild(this.favoriteButton);
    }

    public String getSpellName() {
        return this.spellName;
    }

    public UUID getSpellId() {
        return spellId;
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

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
        this.favoriteButton.setTextureOffset(this.isFavorite ? 16 : 0, 0);
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavoriteCallback(Runnable favoriteCallback) {
        this.favoriteButton.setClickCallback(favoriteCallback);
    }

    public void setDisplayNameOverrideSupplier(Supplier<String> displayNameOverrideSupplier) {
        this.displayNameOverrideSupplier = displayNameOverrideSupplier;
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        //TODO: better colors
        RenderUtil.drawRect(pose, this.getGlobalX(), this.getGlobalY(), this.getWidth(), this.getHeight(), this.selected ? 0xFF888888 : 0xFF000000);
        RenderUtil.drawRect(pose, this.getGlobalX() + 1, this.getGlobalY() + 1, this.getWidth() - 2, this.getHeight() - 2, 0xFFFFFFFF);
        String text = this.displayNameOverrideSupplier != null ? this.displayNameOverrideSupplier.get() : this.spellName;
        RenderUtil.drawTextTruncated(pose, Minecraft.getInstance().font, text, this.getGlobalX() + 2, this.getGlobalY() + 4, this.getWidth() - 6 - FAVORITE_SIZE, 0xFF000000);

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
