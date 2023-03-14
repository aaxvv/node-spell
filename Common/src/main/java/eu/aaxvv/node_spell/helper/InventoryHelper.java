package eu.aaxvv.node_spell.helper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class InventoryHelper {
    public static ItemStack findStackInInventory(Player player, Predicate<ItemStack> predicate) {
        for(int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (predicate.test(stack)) {
                return stack;
            }
        }

        return null;
    }

    public static List<ItemStack> findAllStacksInInventory(Player player, Predicate<ItemStack> predicate) {
        List<ItemStack> matches = new ArrayList<>();

        for(int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (predicate.test(stack)) {
                matches.add(stack);
            }
        }

        return matches;
    }
}
