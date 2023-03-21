package eu.aaxvv.node_spell.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.*;
import eu.aaxvv.node_spell.client.gui.elements.*;
import eu.aaxvv.node_spell.client.gui.helper.MultiSelectionModel;
import eu.aaxvv.node_spell.client.gui.helper.TextEditController;
import eu.aaxvv.node_spell.client.gui.helper.TextureRegion;
import eu.aaxvv.node_spell.client.gui.spell_list.GuiSpellListItem;
import eu.aaxvv.node_spell.helper.InventoryHelper;
import eu.aaxvv.node_spell.item.ModItems;
import eu.aaxvv.node_spell.network.packet.ExportSpellsC2SPacket;
import eu.aaxvv.node_spell.network.packet.UpdateSpellBookC2SPacket;
import eu.aaxvv.node_spell.platform.services.ClientPlatformHelper;
import eu.aaxvv.node_spell.spell.Spell;
import eu.aaxvv.node_spell.spell.execution.SpellDeserializationContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class SpellBookScreen extends BaseScreen {
    private static final ResourceLocation BACKGROUND_LOCATION = ModConstants.resLoc("textures/gui/spell_book_bg.png");
    private static final TextureRegion BACKGROUND = new TextureRegion(BACKGROUND_LOCATION, 20, 1, 164, 180);
    private static final int SAFE_AREA_X_OFFSET = 14;
    private static final int SAFE_AREA_Y_OFFSET = 11;
    private static final int SAFE_AREA_WIDTH = 135;
    private static final int SAFE_AREA_HEIGHT = 158;
    private static final int MAX_FAVORITE_SPELLS = 8;

    private final ItemStack bookStack;
    private final Player player;
    private final InteractionHand hand;
    private final List<ItemStack> writtenSpellPaperItems;
    private int selectedImportItemIdx;

    private final GuiScrollContainer spellList;
    private final GuiTransientText errorText;
    private final MultiSelectionModel selectionModel;
    private final GuiTextureButton importButton;

    private GuiSpellListItem renaming;
    private final TextEditController textEditController;

    public SpellBookScreen(Player player, ItemStack bookStack, InteractionHand hand) {
        super(Component.literal("Spell Book"));
        this.player = player;
        this.bookStack = bookStack;
        this.hand = hand;
        this.selectionModel = new MultiSelectionModel();
        this.textEditController = new TextEditController();
        this.writtenSpellPaperItems = this.findSpellPaperItems(this.player);
        this.selectedImportItemIdx = 0;


        this.guiContext.getRootPane().setSize(SAFE_AREA_WIDTH, SAFE_AREA_HEIGHT);

        makeAndAddButton(0, Component.translatable("gui.node_spell.spell_list.edit"))
                .setClickCallback(this::onEditSpell);

        makeAndAddButton(1, Component.translatable("gui.node_spell.spell_list.create"))
                .setClickCallback(this::onAddSpell);

        makeAndAddButton(2, Component.translatable("gui.node_spell.spell_list.delete"))
                .setClickCallback(this::onRemoveSpell);

        makeAndAddButton(3, Component.translatable("gui.node_spell.spell_list.rename"))
                .setClickCallback(this::onRenameSpell);

        makeAndAddButton(4, Component.translatable("gui.node_spell.spell_list.duplicate"))
                .setClickCallback(this::onDuplicateSpell);

        // need to save this one because we need to change its tooltip
        this.importButton = makeAndAddButton(5, Component.empty());
        this.importButton.setClickCallback(this::onImportSpells);
        this.importButton.setScrollCallback(this::onScrollImportButton);
        this.importButton.setTooltip(this.getImportButtonTooltip());

        makeAndAddButton(6, Component.translatable("gui.node_spell.spell_list.export"))
                .setClickCallback(this::onExportSpells);

        this.spellList = new GuiScrollContainer(SAFE_AREA_WIDTH, SAFE_AREA_HEIGHT - 18);
        getGuiRoot().addChild(spellList);

        this.errorText = new GuiTransientText(getRootWidth());
        this.errorText.setLocalPosition(0, -32);
        this.errorText.setCentered(true);
        getGuiRoot().addChild(this.errorText);
        addSpellsFromBook();
    }

    private List<ItemStack> findSpellPaperItems(Player player) {
        return InventoryHelper.findAllStacksInInventory(player, stack -> {
            if (!stack.is(ModItems.WRITTEN_PAPER)) {
                return false;
            }

            return stack.getOrCreateTag().contains("Spells", Tag.TAG_COMPOUND);
        });
    }

    private void onEditSpell() {
        if (!ensureOneSelected()) {
            return;
        }

        GuiSpellListItem selected = (GuiSpellListItem) this.selectionModel.getSelectedItems().iterator().next();

        Spell spell;
        if (selected.getCachedSpell() != null) {
            spell = selected.getCachedSpell();
        } else {
            CompoundTag spellTag = this.bookStack.getOrCreateTagElement("Spells").getCompound(selected.getSpellName());
            spell = Spell.fromNbt(spellTag, new SpellDeserializationContext.ClientSide(this));
            selected.setCachedSpell(spell);
        }

        Minecraft.getInstance().setScreen(new NewSpellEditScreen(this, spell));
    }

    private void onRemoveSpell() {
        this.selectionModel.getSelectedItems().forEach(item -> {
            if (item instanceof GuiSpellListItem listItem) {
                this.bookStack.getOrCreateTagElement("Spells").remove(listItem.getSpellName());
                this.spellList.removeChild(listItem);

                ListTag activeSpellsList = this.bookStack.getOrCreateTag().getList("ActiveSpells", Tag.TAG_STRING);
                activeSpellsList.removeIf(tag -> tag.getAsString().equals(listItem.getSpellName()));
            }
        });
        this.selectionModel.deselectAll();
    }

    private void onAddSpell() {
        String name = getNewSpellName("New Spell");
        addSpellItem(name);
        this.bookStack.getOrCreateTagElement("Spells").put(name, Spell.createEmptyNbt(name));
    }

    private void onRenameSpell() {
        if (!ensureOneSelected()) {
            return;
        }

        if (renaming != null) {
            return;
        }

        GuiSpellListItem selected = (GuiSpellListItem) this.selectionModel.getSelectedItems().iterator().next();

        this.renaming = selected;
        selected.setDisplayNameOverrideSupplier(this.textEditController::getDisplayString);
        textEditController.setDisplayWidth(selected.getWidth() - 6 - GuiSpellListItem.FAVORITE_SIZE);
        textEditController.setRollbackValueProvider(selected::getSpellName);
        textEditController.startEditing(selected.getSpellName());
        textEditController.setDoneCallback(newName -> {
            selected.setDisplayNameOverrideSupplier(null);
            if (newName == null || newName.isEmpty()) {
                this.renaming = null;
                return false;
            }

            Set<String> currentNames = this.spellList.getChildren().stream().map(child -> ((GuiSpellListItem) child).getSpellName()).collect(Collectors.toSet());
            if (currentNames.contains(newName)) {
                return false;
            }

            CompoundTag oldSpellTag = this.bookStack.getOrCreateTagElement("Spells").getCompound(selected.getSpellName());
            this.bookStack.getOrCreateTagElement("Spells").remove(selected.getSpellName());

            ListTag activeSpellsList = this.bookStack.getOrCreateTag().getList("ActiveSpells", Tag.TAG_STRING);
            if (activeSpellsList.removeIf(tag -> tag.getAsString().equals(selected.getSpellName()))) {
                activeSpellsList.add(StringTag.valueOf(newName));
            }

            selected.setSpellName(newName);
            this.bookStack.getOrCreateTagElement("Spells").put(newName, oldSpellTag);
            oldSpellTag.putString("Name", newName);
            this.renaming = null;
            this.spellList.getChildren().sort(Comparator.comparing(child -> ((GuiSpellListItem)child).getSpellName()));
            return true;
        });
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.renaming != null) {
            this.textEditController.onKeyPressed(keyCode, scanCode, modifiers);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char character, int modifiers) {
        if (this.renaming != null) {
            this.textEditController.onCharTyped(character, modifiers);
            return true;
        }
        return super.charTyped(character, modifiers);
    }

    private void onDuplicateSpell() {
        if (!ensureOneSelected()) {
            return;
        }

        GuiSpellListItem selected = (GuiSpellListItem) this.selectionModel.getSelectedItems().iterator().next();

        CompoundTag newSpellTag;
        if (selected.getCachedSpell() != null) {
            newSpellTag = new CompoundTag();
            selected.getCachedSpell().serialize(newSpellTag);
        } else {
            newSpellTag = this.bookStack.getOrCreateTagElement("Spells").getCompound(selected.getSpellName()).copy();
        }

        String name = getNewSpellName(selected.getSpellName());
        addSpellItem(name);
        this.bookStack.getOrCreateTagElement("Spells").put(name, newSpellTag);
    }

    private void onFavoriteSpell(GuiSpellListItem item) {
        ListTag activeSpellsList = this.bookStack.getOrCreateTag().getList("ActiveSpells", Tag.TAG_STRING);
        if (item.isFavorite()) {
            item.setFavorite(false);
            activeSpellsList.removeIf(tag -> tag.getAsString().equals(item.getSpellName()));
        } else {
            if (activeSpellsList.size() >= MAX_FAVORITE_SPELLS) {
                this.errorText.show(Component.translatable("gui.node_spell.spell_list.favorite_limit_error", 8).withStyle(ChatFormatting.RED), 60);
                return;
            }

            item.setFavorite(true);
            activeSpellsList.add(StringTag.valueOf(item.getSpellName()));
        }

        this.bookStack.getOrCreateTag().put("ActiveSpells", activeSpellsList);
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

    private void onExportSpells() {
        if (InventoryHelper.findStackInInventory(player, stack -> stack.is(Items.PAPER)) == null) {
            this.errorText.show(Component.translatable("gui.node_spell.spell_list.no_paper_error", 8).withStyle(ChatFormatting.RED), 60);
            return;
        }

        CompoundTag spellsToExport = new CompoundTag();

        this.selectionModel.getSelectedItems().forEach(item -> {
            if (item instanceof GuiSpellListItem listItem) {
                CompoundTag spellTag;
                if (listItem.getCachedSpell() != null) {
                    spellTag = new CompoundTag();
                    listItem.getCachedSpell().serialize(spellTag);
                } else {
                    spellTag = this.bookStack.getOrCreateTagElement("Spells").getCompound(listItem.getSpellName());
                }

                spellsToExport.put(listItem.getSpellName(), spellTag);
            }
        });

        ClientPlatformHelper.INSTANCE.sendToServer(new ExportSpellsC2SPacket(spellsToExport));
        this.errorText.show(Component.translatable("gui.node_spell.spell_list.exported", 8).withStyle(ChatFormatting.WHITE), 60);
    }

    private void onImportSpells() {
        if (this.writtenSpellPaperItems.isEmpty()) {
            this.errorText.show(Component.translatable("gui.node_spell.spell_list.import.no_paper", 8).withStyle(ChatFormatting.RED), 60);
            return;
        }

        ItemStack stackToLoad = this.writtenSpellPaperItems.get(this.selectedImportItemIdx);
        CompoundTag spellsTag = stackToLoad.getOrCreateTagElement("Spells");

        List<String> duplicateSpells = new ArrayList<>();
        for (String spellName : spellsTag.getAllKeys()) {
            if (this.spellList.getChildren().stream().map(elem -> ((GuiSpellListItem) elem)).anyMatch(item -> item.getSpellName().equals(spellName))) {
                // already exists
                duplicateSpells.add(spellName);
                continue;
            }
            addSpellItem(spellName);
            this.bookStack.getOrCreateTagElement("Spells").put(spellName, spellsTag.getCompound(spellName));
        }

        if (!duplicateSpells.isEmpty()) {
            String names = String.join(", ", duplicateSpells);
            this.errorText.show(Component.translatable("gui.node_spell.spell_list.import.duplicate_spells", names).withStyle(ChatFormatting.RED), 60);
        }
    }

    private void onScrollImportButton(double mouseX, double mouseY, double amount) {
        if (amount < 0) {
            this.selectedImportItemIdx += 1;
        } else {
            this.selectedImportItemIdx -= 1;
        }
        this.selectedImportItemIdx = Mth.clamp(this.selectedImportItemIdx, 0, this.writtenSpellPaperItems.size() - 1);
        this.importButton.setTooltip(this.getImportButtonTooltip());
    }

    private List<Component> getImportButtonTooltip() {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("gui.node_spell.spell_list.import"));

        if (this.writtenSpellPaperItems.isEmpty()) {
            tooltip.add(Component.translatable("gui.node_spell.spell_list.import.no_paper").withStyle(ChatFormatting.RED));
        } else {
            tooltip.add(Component.translatable("gui.node_spell.spell_list.import.scroll_hint").withStyle(ChatFormatting.GRAY));
            ItemStack current = this.writtenSpellPaperItems.get(this.selectedImportItemIdx);
            tooltip.add(Component.translatable("gui.node_spell.spell_list.import.spell_header"));
            for (String spellName : current.getOrCreateTagElement("Spells").getAllKeys()) {
                tooltip.add(Component.literal(" - " + spellName).withStyle(ChatFormatting.GRAY));
            }
        }

        return tooltip;
    }

    private GuiTextureButton makeAndAddButton(int screenXIndex, Component tooltip) {
        GuiTextureButton button = new GuiTextureButton(15, 15);
        button.setLocalPosition(3 + 19*screenXIndex, SAFE_AREA_HEIGHT - 16);
        button.setTexture(new TextureRegion(BACKGROUND_LOCATION, 2 + 19*screenXIndex, 188, 16, 16));
        button.setTooltip(List.of(tooltip));
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
        Set<String> activeSpells = this.bookStack.getOrCreateTag().getList("ActiveSpells", Tag.TAG_STRING).stream().map(Tag::getAsString).collect(Collectors.toSet());

        for (String spellName : spellsTag.getAllKeys().stream().sorted().toList()) {
            GuiSpellListItem item = addSpellItem(spellName);
            if (activeSpells.contains(item.getSpellName())) {
                item.setFavorite(true);
            }
        }
    }

    private GuiSpellListItem addSpellItem(String spellName) {
        GuiSpellListItem spellItem = new GuiSpellListItem(spellName);
        this.spellList.addChild(spellItem);
        this.spellList.getChildren().sort(Comparator.comparing(child -> ((GuiSpellListItem)child).getSpellName()));
        this.spellList.invalidateLayout();
        spellItem.setClickCallback(() -> this.selectSpell(spellItem));
        spellItem.setFavoriteCallback(() -> this.onFavoriteSpell(spellItem));
        return spellItem;
    }

    private String getNewSpellName(String baseName) {
        Set<String> currentNames = this.spellList.getChildren().stream().map(child -> ((GuiSpellListItem) child).getSpellName()).collect(Collectors.toSet());
        int number = 0;
        String newName = baseName;

        while (currentNames.contains(newName)) {
            number++;
            newName = baseName + " " + number;
        }

        return newName;
    }

    private void selectSpell(GuiSpellListItem item) {
        if (item != renaming) {
            renaming = null;
        }
        this.selectionModel.clickItem(item);
        updateSelectionState();
    }

    private void updateSelectionState() {
        for (GuiElement child : this.spellList.getChildren()) {
            GuiSpellListItem item = ((GuiSpellListItem) child);
            item.setSelected(this.selectionModel.isSelected(item));
        }
    }

    private boolean ensureOneSelected() {
        if (this.selectionModel.getSelectionCount() == 0) {
            this.errorText.show(Component.translatable("gui.node_spell.spell_list.no_selection_error").withStyle(ChatFormatting.RED), 60);
            return false;
        } else if (this.selectionModel.getSelectionCount() > 1) {
            this.errorText.show(Component.translatable("gui.node_spell.spell_list.multiple_selection_error").withStyle(ChatFormatting.RED), 60);
            return false;
        }

        return true;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getBookStack() {
        return bookStack;
    }

    public GuiScrollContainer getSpellList() {
        return spellList;
    }
}
