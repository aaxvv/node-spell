package eu.aaxvv.node_spell.network;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.network.packet.ExportSpellsC2SPacket;
import eu.aaxvv.node_spell.network.packet.UpdateSpellBookC2SPacket;
import eu.aaxvv.node_spell.network.packet.UpdateWandActiveSpellC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ForgePacketHandler {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ModConstants.resLoc("main"), () -> "0", "0"::equals, "0"::equals);

    public static void init() {
        CHANNEL.registerMessage(0, UpdateSpellBookC2SPacket.class, UpdateSpellBookC2SPacket::encode, UpdateSpellBookC2SPacket::decode, makeServerBoundHandler(UpdateSpellBookC2SPacket::handle));
        CHANNEL.registerMessage(1, UpdateWandActiveSpellC2SPacket.class, UpdateWandActiveSpellC2SPacket::encode, UpdateWandActiveSpellC2SPacket::decode, makeServerBoundHandler(UpdateWandActiveSpellC2SPacket::handle));
        CHANNEL.registerMessage(2, ExportSpellsC2SPacket.class, ExportSpellsC2SPacket::encode, ExportSpellsC2SPacket::decode, makeServerBoundHandler(ExportSpellsC2SPacket::handle));
    }

    private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeServerBoundHandler(TriConsumer<T, MinecraftServer, ServerPlayer> handler) {
        return (m, ctx) -> {
            handler.accept(m, ctx.get().getSender().getServer(), ctx.get().getSender());
            ctx.get().setPacketHandled(true);
        };
    }

    private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeClientBoundHandler(Consumer<T> consumer) {
        return (m, ctx) -> {
            consumer.accept(m);
            ctx.get().setPacketHandled(true);
        };
    }
}
