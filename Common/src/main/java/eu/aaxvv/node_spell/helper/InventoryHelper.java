package eu.aaxvv.node_spell.helper;

import net.minecraft.world.entity.player.Inventory;
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

    public static int findStackSlotInInventory(Player player, Predicate<ItemStack> predicate) {
        for(int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (predicate.test(stack)) {
                return i;
            }
        }

        return -1;
    }

    public static ItemStack firstStackAfterSlot(Player player, int startSlot) {
        int slotsChecked = 0;
        int currentSlot = (startSlot + 1) % 9;
        while (slotsChecked < 9) {
            ItemStack current = player.getInventory().getItem(currentSlot);
            if (!current.isEmpty()) {
                return current;
            }

            currentSlot = (currentSlot + 1) % 9;
            slotsChecked++;
        }

        return ItemStack.EMPTY;
    }
}
