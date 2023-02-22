package eu.aaxvv.node_spell.client;

import com.mojang.blaze3d.platform.InputConstants;
import eu.aaxvv.node_spell.client.screen.SpellBookScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class NodeSpellModClient implements ClientModInitializer {
    private KeyMapping testMapping;

    @Override
    public void onInitializeClient() {
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
