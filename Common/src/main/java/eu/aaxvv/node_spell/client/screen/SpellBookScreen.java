package eu.aaxvv.node_spell.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpellBookScreen extends BaseScreen {
    private static final ResourceLocation BACKGROUND_LOCATION = ModConstants.resLoc("textures/gui/spell_book_bg.png");
    private static final TextureRegion BACKGROUND = new TextureRegion(BACKGROUND_LOCATION, 20, 1, 146, 180);
    private static final int SAFE_AREA_X_OFFSET = 14;
    private static final int SAFE_AREA_Y_OFFSET = 11;
    private static final int SAFE_AREA_WIDTH = 117;
    private static final int SAFE_AREA_HEIGHT = 158;

    private final ItemStack bookStack;
    private final Player player;

    public SpellBookScreen(Player player, ItemStack bookStack) {
        super(Component.literal("Spell Book"));
        this.player = player;
        this.bookStack = bookStack;
        GuiElement root = new GuiElement(SAFE_AREA_WIDTH, SAFE_AREA_HEIGHT);
        this.guiContext.setRoot(root);

        makeAndAddButton(0, Component.translatable("gui.node_spell.spell_list.edit"))
                .setClickCallback(() -> System.out.println("Edit button clicked"));

        makeAndAddButton(1, Component.translatable("gui.node_spell.spell_list.create"))
                .setClickCallback(() -> System.out.println("Create button clicked"));

        makeAndAddButton(2, Component.translatable("gui.node_spell.spell_list.delete"))
                .setClickCallback(() -> System.out.println("Delete button clicked"));

        makeAndAddButton(3, Component.translatable("gui.node_spell.spell_list.rename"))
                .setClickCallback(() -> System.out.println("Rename button clicked"));

        makeAndAddButton(4, Component.translatable("gui.node_spell.spell_list.import"))
                .setClickCallback(() -> System.out.println("Import button clicked"));

        makeAndAddButton(5, Component.translatable("gui.node_spell.spell_list.export"))
                .setClickCallback(() -> System.out.println("Export button clicked"));

        GuiScrollContainer scrollContainer = new GuiScrollContainer(SAFE_AREA_WIDTH, SAFE_AREA_HEIGHT - 18);
        root.addChild(scrollContainer);
    }

    private GuiTextureButton makeAndAddButton(int screenXIndex, Component tooltip) {
        GuiTextureButton button = new GuiTextureButton(15, 15);
        button.setLocalPosition(3 + 19*screenXIndex, SAFE_AREA_HEIGHT - 16);
        button.setTexture(new TextureRegion(BACKGROUND_LOCATION, 2 + 19*screenXIndex, 188, 16, 16));
        button.setTooltip(tooltip);
        this.guiContext.getRoot().addChild(button);
        return button;
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        this.fillGradient(pose, 0, 0, this.width, this.height, -1072689136, -804253680);

        BACKGROUND.blit(pose, this.getX() - SAFE_AREA_X_OFFSET, this.getY() - SAFE_AREA_Y_OFFSET);

        super.render(pose, mouseX, mouseY, tickDelta);
    }
}
