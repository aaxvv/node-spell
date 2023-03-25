package eu.aaxvv.node_spell.platform;

import eu.aaxvv.node_spell.network.packet.CustomPacket;
import eu.aaxvv.node_spell.platform.services.PlatformHelper;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;

public class FabricPlatformHelper implements PlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CustomPacket packet) {
        ServerPlayNetworking.send(player, packet.getFabricId(), packet.toBuf());
    }
}
