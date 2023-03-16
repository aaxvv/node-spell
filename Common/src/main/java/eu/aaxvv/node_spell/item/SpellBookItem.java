package eu.aaxvv.node_spell.item;

import eu.aaxvv.node_spell.client.screen.SpellBookScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellBookItem extends Item {
    public SpellBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack bookStack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            return InteractionResultHolder.consume(bookStack);
        }

        Minecraft.getInstance().setScreen(new SpellBookScreen(player, bookStack, hand));
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, level, components, flag);

        int spellCount = itemStack.getOrCreateTagElement("Spells").size();
        components.add(Component.translatable("gui.node_spell.book_spell_tooltip", spellCount).withStyle(ChatFormatting.GRAY));
    }
}
