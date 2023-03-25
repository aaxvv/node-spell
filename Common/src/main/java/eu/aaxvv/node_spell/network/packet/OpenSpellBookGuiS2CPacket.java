package eu.aaxvv.node_spell.network.packet;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.screen.SpellBookScreen;
import eu.aaxvv.node_spell.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record OpenSpellBookGuiS2CPacket(int slot) implements CustomPacket {
    public static final ResourceLocation ID = ModConstants.resLoc("open_gui");
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.slot);
    }

    public static OpenSpellBookGuiS2CPacket decode(FriendlyByteBuf buf) {
        return new OpenSpellBookGuiS2CPacket(buf.readInt());
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    public static void handle(OpenSpellBookGuiS2CPacket packet) {
        int slot = packet.slot();
        Minecraft client = Minecraft.getInstance();

        client.execute(new Runnable() {
            @Override
            public void run() {
                if (client.player == null) {
                    return;
                }

                ItemStack bookStack = client.player.getInventory().getItem(slot);
                if (bookStack.is(ModItems.SPELL_BOOK)) {
                    client.setScreen(new SpellBookScreen(client.player, bookStack, slot));
                }
            }
        });
    }
}
