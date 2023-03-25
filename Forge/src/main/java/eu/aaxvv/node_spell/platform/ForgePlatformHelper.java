package eu.aaxvv.node_spell.platform;

import eu.aaxvv.node_spell.network.ForgePacketHandler;
import eu.aaxvv.node_spell.network.packet.CustomPacket;
import eu.aaxvv.node_spell.platform.services.PlatformHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.PacketDistributor;

public class ForgePlatformHelper implements PlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CustomPacket packet) {
        ForgePacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}