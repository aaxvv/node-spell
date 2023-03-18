package eu.aaxvv.node_spell.client;

import eu.aaxvv.node_spell.item.ModItems;
import eu.aaxvv.node_spell.network.FabricPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;

public class NodeSpellModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NodeSpellClient.init();
        FabricPacketHandler.initClient();

        // render spell selection overlay
        HudRenderCallback.EVENT.register(NodeSpellClient.getSpellSelectionOverlay()::render);

        // prevent wand from right-click interaction with entities
        UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            if (player.getItemInHand(hand).is(ModItems.WAND)) {
                if (Minecraft.getInstance().gameMode != null) {
                    Minecraft.getInstance().gameMode.useItem(player, hand);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }
}
