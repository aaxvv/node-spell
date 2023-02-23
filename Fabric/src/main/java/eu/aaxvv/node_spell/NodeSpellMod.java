package eu.aaxvv.node_spell;

import eu.aaxvv.node_spell.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class NodeSpellMod implements ModInitializer {
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
//        ModConstants.LOG.info("Hello Fabric world!");
        NodeSpellCommon.init();
        ModItems.register((resLoc, item) -> Registry.register(BuiltInRegistries.ITEM, resLoc, item));
    }
}
