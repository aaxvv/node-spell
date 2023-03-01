package eu.aaxvv.node_spell.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.*;
import eu.aaxvv.node_spell.network.packet.UpdateSpellBookC2SPacket;
import eu.aaxvv.node_spell.platform.services.ClientPlatformHelper;
import eu.aaxvv.node_spell.spell.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpellBookScreen extends BaseScreen {
    private static final ResourceLocation BACKGROUND_LOCATION = ModConstants.resLoc("textures/gui/spell_book_bg.png");
    private static final TextureRegion BACKGROUND = new TextureRegion(BACKGROUND_LOCATION, 20, 1, 164, 180);
    private static final int SAFE_AREA_X_OFFSET = 14;
    private static final int SAFE_AREA_Y_OFFSET = 11;
    private static final int SAFE_AREA_WIDTH = 135;
    private static final int SAFE_AREA_HEIGHT = 158;

    private final ItemStack bookStack;
    private final Player player;
    private final InteractionHand hand;

    private final GuiScrollContainer spellList;
    private final GuiTransientText errorText;
    private final MultiSelectionModel selectionModel;

    public SpellBookScreen(Player player, ItemStack bookStack, InteractionHand hand) {
        super(Component.literal("Spell Book"));
        this.player = player;
        this.bookStack = bookStack;
        this.hand = hand;
        this.selectionModel = new MultiSelectionModel();

        this.guiContext.getRootPane().setSize(SAFE_AREA_WIDTH, SAFE_AREA_HEIGHT);

        makeAndAddButton(0, Component.translatable("gui.node_spell.spell_list.edit"))
                .setClickCallback(this::onEditSpell);

        makeAndAddButton(1, Component.translatable("gui.node_spell.spell_list.create"))
                .setClickCallback(this::onAddSpell);

        makeAndAddButton(2, Component.translatable("gui.node_spell.spell_list.delete"))
                .setClickCallback(this::onRemoveSpell);

        makeAndAddButton(3, Component.translatable("gui.node_spell.spell_list.rename"))
                .setClickCallback(() -> System.out.println("Rename button clicked"));

        makeAndAddButton(4, Component.translatable("gui.node_spell.spell_list.duplicate"))
                .setClickCallback(() -> System.out.println("Duplicate button clicked"));

        makeAndAddButton(5, Component.translatable("gui.node_spell.spell_list.import"))
                .setClickCallback(() -> System.out.println("Import button clicked"));

        makeAndAddButton(6, Component.translatable("gui.node_spell.spell_list.export"))
                .setClickCallback(() -> System.out.println("Export button clicked"));

        this.spellList = new GuiScrollContainer(SAFE_AREA_WIDTH, SAFE_AREA_HEIGHT - 18);
        getGuiRoot().addChild(spellList);

        this.errorText = new GuiTransientText(getRootWidth());
        this.errorText.setLocalPosition(0, -32);
        this.errorText.setCentered(true);
        getGuiRoot().addChild(this.errorText);
        addSpellsFromBook();
    }

    private void onEditSpell() {
        if (this.selectionModel.getSelectionCount() != 1) {
            this.errorText.show(Component.translatable("gui.node_spell.spell_list.select_one_spell_error").withStyle(ChatFormatting.RED), 60);
            return;
        }

        GuiSpellListItem selected = (GuiSpellListItem) this.selectionModel.getSelectedItems().iterator().next();

        Spell spell;
        if (selected.getCachedSpell() != null) {
            spell = selected.getCachedSpell();
        } else {
            CompoundTag spellTag = this.bookStack.getOrCreateTagElement("Spells").getCompound(selected.getSpellName());
            spell = Spell.fromNbt(spellTag);
            selected.setCachedSpell(spell);
        }

        Minecraft.getInstance().setScreen(new SpellEditScreen(this, spell));
    }

    private void onRemoveSpell() {
        this.selectionModel.getSelectedItems().forEach(item -> {
            if (item instanceof GuiSpellListItem listItem) {
                this.bookStack.getOrCreateTagElement("Spells").remove(listItem.getSpellName());
                this.spellList.removeChild(listItem);
            }
        });
        this.selectionModel.deselectAll();
    }

    private void onAddSpell() {
        String name = "New Spell";
        // TODO: sort list, pick unique name on creation
        addSpellItem(name);
        this.bookStack.getOrCreateTagElement("Spells").put(name, new CompoundTag());
    }

    private void onFavoriteSpell(GuiSpellListItem item) {
        // TODO: only allow limited number of favorites, add a warning test above the UI?
        this.errorText.show(Component.translatable("gui.node_spell.spell_list.favorite_limit_error", 8).withStyle(ChatFormatting.RED), 60);
        item.setFavorited(!item.isFavorited());
    }

    @Override
    public void onClose() {
        // save all cached spells to nbt
        for (GuiElement child : this.spellList.getChildren()) {
            if (child instanceof GuiSpellListItem item && item.getCachedSpell() != null) {
                String spellName = item.getSpellName();
                Spell spell = item.getCachedSpell();

                CompoundTag spellTag = this.bookStack.getOrCreateTagElement("Spells").getCompound(spellName);
                spell.serialize(spellTag);
                this.bookStack.getOrCreateTagElement("Spells").put(spellName, spellTag);
            }
        }

        int slot = this.hand == InteractionHand.MAIN_HAND ? this.player.getInventory().selected : Inventory.SLOT_OFFHAND;
        ClientPlatformHelper.INSTANCE.sendToServer(new UpdateSpellBookC2SPacket(slot, this.bookStack.getTag()));

        super.onClose();
    }

    private GuiTextureButton makeAndAddButton(int screenXIndex, Component tooltip) {
        GuiTextureButton button = new GuiTextureButton(15, 15);
        button.setLocalPosition(3 + 19*screenXIndex, SAFE_AREA_HEIGHT - 16);
        button.setTexture(new TextureRegion(BACKGROUND_LOCATION, 2 + 19*screenXIndex, 188, 16, 16));
        button.setTooltip(tooltip);
        this.guiContext.getRootPane().addChild(button);
        return button;
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        this.fillGradient(pose, 0, 0, this.width, this.height, -1072689136, -804253680);

        BACKGROUND.blit(pose, this.getX() - SAFE_AREA_X_OFFSET, this.getY() - SAFE_AREA_Y_OFFSET);

        super.render(pose, mouseX, mouseY, tickDelta);
    }

    private void addSpellsFromBook() {
        CompoundTag spellsTag = this.bookStack.getOrCreateTagElement("Spells");

        for (String spellName : spellsTag.getAllKeys().stream().sorted().toList()) {
            addSpellItem(spellName);
        }
    }

    private void addSpellItem(String spellName) {
        GuiSpellListItem spellItem = new GuiSpellListItem(spellName);
        this.spellList.addChild(spellItem);
        spellItem.setClickCallback(() -> this.selectSpell(spellItem));
        spellItem.setFavoriteCallback(() -> this.onFavoriteSpell(spellItem));
    }

    private void selectSpell(GuiSpellListItem item) {
        this.selectionModel.clickItem(item);
        updateSelectionState();
    }

    private void updateSelectionState() {
        for (GuiElement child : this.spellList.getChildren()) {
            GuiSpellListItem item = ((GuiSpellListItem) child);
            item.setSelected(this.selectionModel.isSelected(item));
        }
    }
}
