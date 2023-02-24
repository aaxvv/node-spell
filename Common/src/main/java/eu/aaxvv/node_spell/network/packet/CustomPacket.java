package eu.aaxvv.node_spell.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;


import io.netty.buffer.Unpooled;

/**
 * Like seemingly most people using MultiLoaderTemplate I am stealing implementation ideas from Botania.
 * <p>
 * <a href="https://github.com/VazkiiMods/Botania/blob/1.19.x/Xplat/src/main/java/vazkii/botania/network/BotaniaPacket.java">Botania Source</a>
 */
public interface CustomPacket {
    default FriendlyByteBuf toBuf() {
        var ret = new FriendlyByteBuf(Unpooled.buffer());
        encode(ret);
        return ret;
    }

    void encode(FriendlyByteBuf buf);

    /**
     * Forge auto-assigns incrementing integers, Fabric requires us to declare an ID
     * These are sent using vanilla's custom plugin channel system and thus are written to every single packet.
     * So this ID tends to be more terse.
     */
    ResourceLocation getFabricId();
}