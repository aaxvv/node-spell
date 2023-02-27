package eu.aaxvv.node_spell.network.packet;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.item.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public record UpdateWandActiveSpellC2SPacket(int slot, String spellName) implements CustomPacket {
    public static final ResourceLocation ID = ModConstants.resLoc("wand");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(slot);
        buf.writeUtf(spellName);
    }

    public static UpdateWandActiveSpellC2SPacket decode(FriendlyByteBuf buf) {
        return new UpdateWandActiveSpellC2SPacket(buf.readInt(), buf.readUtf());
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    public static void handle(UpdateWandActiveSpellC2SPacket packet, MinecraftServer server, ServerPlayer player) {
        int slot = packet.slot();
        String spellName = packet.spellName();

        server.execute(() -> {
            if (Inventory.isHotbarSlot(slot) || slot == Inventory.SLOT_OFFHAND) {
                ItemStack wandStack = player.getInventory().getItem(slot);
                if (wandStack.is(ModItems.WAND)) {
                    wandStack.getOrCreateTag().putString("Spell", spellName);
                }
            }
        });
    }
}