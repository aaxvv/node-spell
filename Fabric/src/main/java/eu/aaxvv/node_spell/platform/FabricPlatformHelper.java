package eu.aaxvv.node_spell.platform;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.platform.services.IPlatformHelper;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;

public class FabricPlatformHelper implements IPlatformHelper {

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
    public Registry<Node> createNodeRegistry() {
        return FabricRegistryBuilder.createSimple(Node.class, ModConstants.resLoc("nodes")).buildAndRegister();
    }
}
