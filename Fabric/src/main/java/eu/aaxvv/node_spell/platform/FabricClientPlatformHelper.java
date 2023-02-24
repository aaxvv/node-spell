package eu.aaxvv.node_spell.platform;

import eu.aaxvv.node_spell.network.packet.CustomPacket;
import eu.aaxvv.node_spell.platform.services.ClientPlatformHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class FabricClientPlatformHelper implements ClientPlatformHelper {
    @Override
    public void sendToServer(CustomPacket packet) {
        ClientPlayNetworking.send(packet.getFabricId(), packet.toBuf());
    }
}
