package eu.aaxvv.node_spell.item;

import eu.aaxvv.node_spell.ModConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.BiConsumer;

public class ModItems {
    public static final WandItem WAND = new WandItem(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    public static final SpellBookItem SPELL_BOOK = new SpellBookItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1));

    public static void register(BiConsumer<ResourceLocation, Item> reg) {
        reg.accept(ModConstants.resLoc("wand"), WAND);
        reg.accept(ModConstants.resLoc("spell_book"), SPELL_BOOK);
    }
}
