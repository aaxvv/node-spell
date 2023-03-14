package eu.aaxvv.node_spell.network.packet;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.helper.InventoryHelper;
import eu.aaxvv.node_spell.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record ExportSpellsC2SPacket(CompoundTag spells) implements CustomPacket {
    public static final ResourceLocation ID = ModConstants.resLoc("export");

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.spells);
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    public static ExportSpellsC2SPacket decode(FriendlyByteBuf buf) {
        CompoundTag spells = buf.readNbt();
        return new ExportSpellsC2SPacket(spells);
    }

    public static void handle(ExportSpellsC2SPacket packet, MinecraftServer server, ServerPlayer player) {
        CompoundTag spells = packet.spells();

        server.execute(() -> {
            ItemStack paperStack = InventoryHelper.findStackInInventory(player, s -> s.is(Items.PAPER));
            if (paperStack == null) {
                return;
            }

            paperStack.shrink(1);
            ItemStack writtenStack = new ItemStack(ModItems.WRITTEN_PAPER);
            writtenStack.getOrCreateTag().put("Spells", spells);

//            boolean success = player.getInventory().add(writtenStack);
            player.getInventory().placeItemBackInInventory(writtenStack);
//            if (!success || !writtenStack.isEmpty()) {
//                ItemEntity itemEntity = player.drop(writtenStack, false);
//                if (itemEntity != null) {
//                    itemEntity.setNoPickUpDelay();
//                    itemEntity.setOwner(player.getUUID());
//                }
//            }
//            else {
//                writtenStack.setCount(1);
//                ItemEntity $$12 = player.drop(writtenStack, false);
//                if ($$12 != null) {
//                    $$12.makeFakeItem();
//                }
//
//                player.level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
//                player.containerMenu.broadcastChanges();
//            }
        });
    }
}
