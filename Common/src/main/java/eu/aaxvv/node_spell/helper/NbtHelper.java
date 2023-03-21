package eu.aaxvv.node_spell.helper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;
import java.util.function.Predicate;

public class NbtHelper {
    public static CompoundTag findSpellInBookTag(ItemStack spellBook, UUID spellId) {
        return findListCompound(spellBook.getOrCreateTag().getList("Spells", Tag.TAG_COMPOUND), tag -> spellId.equals(getSpellId(tag)));
    }

    public static CompoundTag findListCompound(ListTag listTag, Predicate<CompoundTag> predicate) {
        for (Tag tag : listTag) {
            if (tag instanceof CompoundTag compound && predicate.test(compound)) {
                return compound;
            }
        }

        return null;
    }

    public static UUID getSpellId(Tag spellTag) {
        if (!(spellTag instanceof CompoundTag compoundTag)) {
            return null;
        }

        Tag idTag  = compoundTag.get("Id");
        if (idTag != null) {
            return NbtUtils.loadUUID(idTag);
        } else {
            return null;
        }
    }
}
