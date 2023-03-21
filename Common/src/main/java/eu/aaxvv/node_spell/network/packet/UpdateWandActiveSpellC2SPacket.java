package eu.aaxvv.node_spell.network.packet;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.helper.InventoryHelper;
import eu.aaxvv.node_spell.helper.NbtHelper;
import eu.aaxvv.node_spell.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.UUID;

public record UpdateWandActiveSpellC2SPacket(int slot, UUID spellId) implements CustomPacket {
    public static final ResourceLocation ID = ModConstants.resLoc("wand");

    public UpdateWandActiveSpellC2SPacket(int slot, UUID spellId) {
        this.slot = slot;
        this.spellId = Objects.requireNonNullElseGet(spellId, () -> new UUID(0, 0));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(slot);
        buf.writeUUID(spellId);
    }

    public static UpdateWandActiveSpellC2SPacket decode(FriendlyByteBuf buf) {
        return new UpdateWandActiveSpellC2SPacket(buf.readInt(), buf.readUUID());
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    public static void handle(UpdateWandActiveSpellC2SPacket packet, MinecraftServer server, ServerPlayer player) {
        int slot = packet.slot();
        UUID spellId = packet.spellId();

        server.execute(() -> {
            if (Inventory.isHotbarSlot(slot) || slot == Inventory.SLOT_OFFHAND) {
                ItemStack wandStack = player.getInventory().getItem(slot);
                if (wandStack.is(ModItems.WAND)) {
                    wandStack.getOrCreateTag().put("SpellId", NbtUtils.createUUID(spellId));
                    if (spellId.equals(new UUID(0, 0))) {
                        wandStack.getOrCreateTag().putString("SpellName", "");
                    } else {
                        ItemStack bookStack = InventoryHelper.findStackInInventory(player, stack -> stack.is(ModItems.SPELL_BOOK));
                        if (bookStack == null) {
                            return;
                        }
                        CompoundTag spellTag = NbtHelper.findSpellInBookTag(bookStack, spellId);
                        if (spellTag == null) {
                            return;
                        }
                        wandStack.getOrCreateTag().putString("SpellName", spellTag.getString("Name"));
                    }
                }
            }
        });
    }
}