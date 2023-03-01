package eu.aaxvv.node_spell.network.packet;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.NodeSpellCommon;
import eu.aaxvv.node_spell.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public record UpdateSpellBookC2SPacket(int slot, CompoundTag bookNbt) implements CustomPacket {
    public static final ResourceLocation ID = ModConstants.resLoc("book");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(slot);
        buf.writeNbt(bookNbt);
    }

    public static UpdateSpellBookC2SPacket decode(FriendlyByteBuf buf) {
        return new UpdateSpellBookC2SPacket(buf.readInt(), buf.readNbt());
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    public static void handle(UpdateSpellBookC2SPacket packet, MinecraftServer server, ServerPlayer player) {
        int slot = packet.slot();
        CompoundTag bookNbt = packet.bookNbt();

        server.execute(() -> {
            if (Inventory.isHotbarSlot(slot) || slot == Inventory.SLOT_OFFHAND) {
                ItemStack bookStack = player.getInventory().getItem(slot);
                if (bookStack.is(ModItems.SPELL_BOOK)) {
                    bookStack.setTag(bookNbt);
                    NodeSpellCommon.playerSpellCache.invalidate(player.getUUID());
                }
            }
        });
    }
}
