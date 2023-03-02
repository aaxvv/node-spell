package eu.aaxvv.node_spell.item;

import eu.aaxvv.node_spell.client.screen.SpellBookScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

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
}
