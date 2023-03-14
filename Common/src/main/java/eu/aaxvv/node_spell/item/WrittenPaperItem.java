package eu.aaxvv.node_spell.item;

import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WrittenPaperItem extends Item {
    public WrittenPaperItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);

        CompoundTag spellTag = stack.getOrCreateTagElement("Spells");
        if (!spellTag.isEmpty()) {
            components.add(Component.translatable("node_spell.written_paper.spell_header").withStyle(ChatFormatting.GRAY));
            for (String key : spellTag.getAllKeys()) {
                components.add(Component.literal(" - " + key).withStyle(ChatFormatting.GRAY));
            }
        }

        CompoundTag valueTag = stack.getOrCreateTagElement("Value");
        if (!valueTag.isEmpty()) {
            components.add(Component.translatable("node_spell.written_paper.value_header").withStyle(ChatFormatting.GRAY));
            Value value = Value.fromNbt(valueTag);
            if (value != null) {
                components.add(Component.literal("  " + value).withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
