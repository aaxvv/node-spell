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
        if (spellName.isEmpty()) {
            spellName = "-";
        }
        lines.add(Component.translatable("gui.node_spell.wand_spell_tooltip", spellName));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();

        if (!tag.contains("Spell")) {
            player.displayClientMessage(Component.translatable("gui.node_spell.no_spell_selected").withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }
        // player.startUsingItem(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        SpellBookResult spellBook = findPlayerSpellBook(player);

        if (spellBook.bookStack == null) {
            player.displayClientMessage(Component.translatable("gui.node_spell.spell_book_not_found").withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }

        if (spellBook.duplicate) {
            player.displayClientMessage(Component.translatable("gui.node_spell.multiple_spell_books_found").withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }

        String spellName = tag.getString("Spell");
        CompoundTag spellNbt = this.getSpellFromBook(spellBook.bookStack, spellName);
        Optional<Spell> cachedSpell = NodeSpellCommon.playerSpellCache.getOrCreate(player.getUUID(), spellName, spellNbt);

        if (cachedSpell.isEmpty()) {
            player.displayClientMessage(Component.translatable("gui.node_spell.spell_not_active").withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }

        boolean success = NodeSpellCommon.spellTaskRunner.startSpell(player.getUUID(), cachedSpell.get(), new SpellContext((ServerPlayer) player, level, cachedSpell.get().getName()));

        return success ? InteractionResultHolder.consume(stack) : InteractionResultHolder.fail(stack);
    }

    private SpellBookResult findPlayerSpellBook(Player player) {
        ItemStack found = null;

        for(int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(ModItems.SPELL_BOOK)) {
                if (found != null) {
                    return new SpellBookResult(found, true);
                }

                found = stack;
            }
        }

        return new SpellBookResult(found, false);
    }

    private CompoundTag getSpellFromBook(ItemStack spellBook, String spellName) {
        ListTag bookActiveSpellsTag = spellBook.getOrCreateTag().getList("ActiveSpells", Tag.TAG_STRING);
        if (!bookActiveSpellsTag.contains(StringTag.valueOf(spellName))) {
            return null;
        }

        CompoundTag bookSpellListTag = spellBook.getOrCreateTagElement("Spells");
        if (bookSpellListTag.contains(spellName)) {
            return bookSpellListTag.getCompound(spellName);
        } else {
            return null;
        }
    }

    private record SpellBookResult(ItemStack bookStack, boolean duplicate){}

    @Override
    public boolean canAttackBlock(BlockState $$0, Level $$1, BlockPos $$2, Player $$3) {
        return false;
    }


}
