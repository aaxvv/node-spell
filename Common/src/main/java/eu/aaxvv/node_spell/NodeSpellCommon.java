package eu.aaxvv.node_spell;

import eu.aaxvv.node_spell.spell.execution.PlayerMotionRecorder;
import eu.aaxvv.node_spell.spell.execution.PlayerSpellCache;
import eu.aaxvv.node_spell.spell.execution.SpellPersistentStorage;
import eu.aaxvv.node_spell.spell.execution.SpellTaskRunner;
import net.minecraft.server.level.ServerLevel;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class NodeSpellCommon {
    public static PlayerSpellCache playerSpellCache;
    public static SpellTaskRunner spellTaskRunner;
    public static PlayerMotionRecorder playerMotionRecorder;
    public static SpellPersistentStorage spellPersistentStorage;

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        playerSpellCache = new PlayerSpellCache();
        spellTaskRunner = new SpellTaskRunner();
        playerMotionRecorder = new PlayerMotionRecorder();
        spellPersistentStorage = new SpellPersistentStorage();
//        Nodes.registerNodes();
//        NodeCategories.registerCategories();

//        ModConstants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
//        ModConstants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));
    }

    public static void onLevelTick(ServerLevel level) {
        playerMotionRecorder.updateAllPlayers(level);
        spellTaskRunner.onTick(level);
    }
}