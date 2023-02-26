package eu.aaxvv.node_spell.item;

import eu.aaxvv.node_spell.NodeSpellCommon;
import eu.aaxvv.node_spell.spell.execution.PlayerSpellCache;
import eu.aaxvv.node_spell.spell.Spell;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellRunner;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class WandItem extends Item {

    public WandItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, level, lines, flag);
        String spellName = stack.getOrCreateTag().getString("Spell");
        lines.add(Component.translatable("gui.node_spell.wand_spell_tooltip", spellName));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();

        if (!tag.contains("Spell")) {
            return InteractionResultHolder.fail(stack);
        }
        // player.startUsingItem(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        SpellBookResult spellBook = findPlayerSpellBook(player);

        if (spellBook.bookStack == null) {
            ((ServerPlayer)player).displayClientMessage(Component.translatable("gui.node_spell.spell_book_not_found").withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }

        if (spellBook.duplicate) {
            ((ServerPlayer)player).displayClientMessage(Component.translatable("gui.node_spell.multiple_spell_books_found").withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }

        String spellName = tag.getString("Spell");
        Optional<Spell> cachedSpell = NodeSpellCommon.playerSpellCache.get(player.getUUID(), spellName);

        Spell spellToCast;
        if (cachedSpell.isEmpty()) {
            spellToCast = this.getSpellFromBook(spellBook.bookStack, spellName);
            NodeSpellCommon.playerSpellCache.put(player.getUUID(), spellName, spellToCast);
        } else {
            spellToCast = cachedSpell.get();
        }

        if (spellToCast == null) {
            ((ServerPlayer)player).displayClientMessage(Component.translatable("gui.node_spell.spell_not_active").withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }

        NodeSpellCommon.spellTaskRunner.startSpell(player.getUUID(), spellToCast, new SpellContext(player, level));

//        if (!success) {
//            return InteractionResultHolder.fail(stack);
//        } else {
            return InteractionResultHolder.consume(stack);
//        }
    }

    private SpellBookResult findPlayerSpellBook(Player player) {
        ItemStack found = null;

        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == ModItems.SPELL_BOOK) {
                if (found != null) {
                    return new SpellBookResult(found, true);
                }

                found = stack;
            }
        }

        return new SpellBookResult(found, false);
    }

    private Spell getSpellFromBook(ItemStack spellBook, String spellName) {
        ListTag bookActiveSpellsTag = spellBook.getOrCreateTag().getList("ActiveSpells", Tag.TAG_STRING);
        if (!bookActiveSpellsTag.contains(StringTag.valueOf(spellName))) {
            return null;
        }

        CompoundTag bookSpellListTag = spellBook.getOrCreateTagElement("Spells");
        return Spell.fromNbt(bookSpellListTag.getCompound(spellName));
    }

    private record SpellBookResult(ItemStack bookStack, boolean duplicate){}

    @Override
    public boolean canAttackBlock(BlockState $$0, Level $$1, BlockPos $$2, Player $$3) {
        return false;
    }


}
