package eu.aaxvv.node_spell.platform.services;

import eu.aaxvv.node_spell.network.packet.CustomPacket;
import eu.aaxvv.node_spell.platform.Services;

public interface ClientPlatformHelper {
    ClientPlatformHelper INSTANCE = Services.load(ClientPlatformHelper.class);

    void sendToServer(CustomPacket packet);
}
