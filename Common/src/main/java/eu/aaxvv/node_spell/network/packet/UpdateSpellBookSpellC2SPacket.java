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

public record UpdateSpellBookSpellC2SPacket(int slot, String spellName, CompoundTag spellNbt) implements CustomPacket {
    public static final ResourceLocation ID = ModConstants.resLoc("book");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(slot);
        buf.writeUtf(spellName);
        buf.writeNbt(spellNbt);
    }

    public static UpdateSpellBookSpellC2SPacket decode(FriendlyByteBuf buf) {
        return new UpdateSpellBookSpellC2SPacket(buf.readInt(), buf.readUtf(), buf.readNbt());
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    public static void handle(UpdateSpellBookSpellC2SPacket packet, MinecraftServer server, ServerPlayer player) {
        int slot = packet.slot();
        String spellName = packet.spellName();
        CompoundTag spellNbt = packet.spellNbt();

        server.execute(() -> {
            if (Inventory.isHotbarSlot(slot) || slot == Inventory.SLOT_OFFHAND) {
                ItemStack bookStack = player.getInventory().getItem(slot);
                if (bookStack.is(ModItems.SPELL_BOOK)) {
                    // TODO: testing
                    ListTag activeSpellList = new ListTag();
                    activeSpellList.add(StringTag.valueOf("Test Spell"));
                    bookStack.getOrCreateTag().put("ActiveSpells", activeSpellList);

                    bookStack.getOrCreateTagElement("Spells").put(spellName, spellNbt);
                    NodeSpellCommon.playerSpellCache.invalidate(player.getUUID(), spellName);
                }
            }
        });
    }
}
