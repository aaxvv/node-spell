package eu.aaxvv.node_spell.network;

import eu.aaxvv.node_spell.network.packet.ExportSpellsC2SPacket;
import eu.aaxvv.node_spell.network.packet.OpenSpellBookGuiS2CPacket;
import eu.aaxvv.node_spell.network.packet.UpdateSpellBookC2SPacket;
import eu.aaxvv.node_spell.network.packet.UpdateWandActiveSpellC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This is also from Botania.
 * <p>
 * <a href="https://github.com/VazkiiMods/Botania/blob/1.19.x/Fabric/src/main/java/vazkii/botania/fabric/network/FabricPacketHandler.java">Botania Source</a>
 */
public class FabricPacketHandler {
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(UpdateSpellBookC2SPacket.ID, makeServerBoundHandler(UpdateSpellBookC2SPacket::decode, UpdateSpellBookC2SPacket::handle));
        ServerPlayNetworking.registerGlobalReceiver(UpdateWandActiveSpellC2SPacket.ID, makeServerBoundHandler(UpdateWandActiveSpellC2SPacket::decode, UpdateWandActiveSpellC2SPacket::handle));
        ServerPlayNetworking.registerGlobalReceiver(ExportSpellsC2SPacket.ID, makeServerBoundHandler(ExportSpellsC2SPacket::decode, ExportSpellsC2SPacket::handle));
    }

    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(OpenSpellBookGuiS2CPacket.ID, makeClientBoundHandler(OpenSpellBookGuiS2CPacket::decode, OpenSpellBookGuiS2CPacket::handle));
    }

    private static <T> ServerPlayNetworking.PlayChannelHandler makeServerBoundHandler(
            Function<FriendlyByteBuf, T> decoder, TriConsumer<T, MinecraftServer, ServerPlayer> handle) {
        return (server, player, _handler, buf, _responseSender) -> handle.accept(decoder.apply(buf), server, player);
    }

    private static <T> ClientPlayNetworking.PlayChannelHandler makeClientBoundHandler(
            Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        return (_client, _handler, buf, _responseSender) -> handler.accept(decoder.apply(buf));
    }
}
