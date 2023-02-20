package eu.aaxvv.node_spell.platform;

import eu.aaxvv.node_spell.platform.services.IPlatformHelper;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import net.minecraft.core.Registry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.commons.lang3.NotImplementedException;

public class ForgePlatformHelper implements IPlatformHelper {

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
    public Registry<Node> createNodeRegistry() {
        //TODO: figure out how to do this on forge
        throw new NotImplementedException();
    }
}