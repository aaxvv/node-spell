package eu.aaxvv.node_spell.item;

import eu.aaxvv.node_spell.network.packet.OpenSpellBookGuiS2CPacket;
import eu.aaxvv.node_spell.platform.services.PlatformHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
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
        if (level.isClientSide) {
            return InteractionResultHolder.success(bookStack);
        }

        int slot = hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : Inventory.SLOT_OFFHAND;
        PlatformHelper.INSTANCE.sendToPlayer((ServerPlayer) player, new OpenSpellBookGuiS2CPacket(slot));

        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, level, components, flag);

        int spellCount = itemStack.getOrCreateTag().getList("Spells", Tag.TAG_COMPOUND).size();
        components.add(Component.translatable("gui.node_spell.book_spell_tooltip", spellCount).withStyle(ChatFormatting.GRAY));
    }
}
