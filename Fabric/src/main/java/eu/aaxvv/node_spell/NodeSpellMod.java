package eu.aaxvv.node_spell;

import com.mojang.blaze3d.platform.InputConstants;
import eu.aaxvv.node_spell.client.screen.SpellBookScreen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class NodeSpellMod implements ModInitializer {
    private KeyMapping testMapping;

    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        NodeSpellCommon.init();

        testMapping = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.node_spell.test", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, "key.node_spell.test.desc"));

        ClientTickEvents.START_CLIENT_TICK.register(this::startTick);
    }

    private void startTick(Minecraft client) {
        while (testMapping.consumeClick()) {
            System.out.println("KeyBind triggered");

            client.setScreen(new SpellBookScreen(Component.literal("Title")));
        }
    }
}
