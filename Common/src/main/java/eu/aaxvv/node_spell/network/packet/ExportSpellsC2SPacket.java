package eu.aaxvv.node_spell.network.packet;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.helper.InventoryHelper;
import eu.aaxvv.node_spell.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record ExportSpellsC2SPacket(ListTag spells) implements CustomPacket {
    public static final ResourceLocation ID = ModConstants.resLoc("export");

    @Override
    public void encode(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        tag.put("Spells", this.spells);
        buf.writeNbt(tag);
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    public static ExportSpellsC2SPacket decode(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();
        if (tag == null) {
            return new ExportSpellsC2SPacket(new ListTag());
        }
        ListTag spells = tag.getList("Spells", Tag.TAG_COMPOUND);
        return new ExportSpellsC2SPacket(spells);
    }

    public static void handle(ExportSpellsC2SPacket packet, MinecraftServer server, ServerPlayer player) {
        ListTag spells = packet.spells();

        server.execute(() -> {
            ItemStack paperStack = InventoryHelper.findStackInInventory(player, s -> s.is(Items.PAPER));
            if (paperStack == null) {
                return;
            }

            paperStack.shrink(1);
            ItemStack writtenStack = new ItemStack(ModItems.WRITTEN_PAPER);
            writtenStack.getOrCreateTag().put("Spells", spells);

            player.getInventory().placeItemBackInInventory(writtenStack);
        });
    }
}
