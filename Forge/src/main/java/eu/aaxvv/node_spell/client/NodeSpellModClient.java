package eu.aaxvv.node_spell.client;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.item.ModItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NodeSpellModClient {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        NodeSpellClient.init();

        MinecraftForge.EVENT_BUS.addListener((PlayerInteractEvent.LeftClickEmpty clickEvent) -> {
            if (clickEvent.getItemStack().is(ModItems.WAND)) {
                NodeSpellClient.getSpellSelectionOverlay().activate();
            }
        });

        MinecraftForge.EVENT_BUS.addListener((PlayerInteractEvent.LeftClickBlock clickEvent) -> {
            if (clickEvent.getItemStack().is(ModItems.WAND)) {
                NodeSpellClient.getSpellSelectionOverlay().activate();
                clickEvent.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("node_spell_spell_select", (ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) -> {
            NodeSpellClient.getSpellSelectionOverlay().render(poseStack, partialTick);
        });
    }
}
