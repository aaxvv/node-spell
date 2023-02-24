package eu.aaxvv.node_spell.item;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.screen.SpellBookScreen;
import eu.aaxvv.node_spell.spell.Spell;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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

        CompoundTag spellListTag = bookStack.getOrCreateTagElement("Spells");

        // TODO: for testing
        if (spellListTag.isEmpty()) {
            spellListTag.put("Test Spell", new CompoundTag());
        }

        Tag spellTag = spellListTag.get(spellListTag.getAllKeys().iterator().next());
        if (!(spellTag instanceof CompoundTag)) {
            ModConstants.LOG.error("Expected spell compound but found list.");
            return InteractionResultHolder.fail(bookStack);
        }

        Spell editedSpell = Spell.fromNbt((CompoundTag) spellTag);
        editedSpell.setName("Test Spell");

        Minecraft.getInstance().setScreen(new SpellBookScreen(editedSpell, player, bookStack, hand));
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
