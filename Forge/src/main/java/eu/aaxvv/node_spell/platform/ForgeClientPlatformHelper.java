package eu.aaxvv.node_spell.platform;

import eu.aaxvv.node_spell.network.ForgePacketHandler;
import eu.aaxvv.node_spell.network.packet.CustomPacket;
import eu.aaxvv.node_spell.platform.services.ClientPlatformHelper;

public class ForgeClientPlatformHelper implements ClientPlatformHelper {
    @Override
    public void sendToServer(CustomPacket packet) {
        ForgePacketHandler.CHANNEL.sendToServer(packet);
    }
}
