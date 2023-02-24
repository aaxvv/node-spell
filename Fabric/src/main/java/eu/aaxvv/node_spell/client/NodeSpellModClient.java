package eu.aaxvv.node_spell.client;

import eu.aaxvv.node_spell.network.FabricPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class NodeSpellModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NodeSpellClient.init();
        FabricPacketHandler.initClient();

        HudRenderCallback.EVENT.register(NodeSpellClient.getSpellSelectionOverlay()::render);
    }
}
