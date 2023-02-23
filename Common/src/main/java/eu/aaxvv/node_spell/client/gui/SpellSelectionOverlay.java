package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class SpellSelectionOverlay extends GuiComponent {
    private boolean active;
    private List<String> activeSpellNames;

    public SpellSelectionOverlay() {
        this.active = false;
        this.activeSpellNames = new ArrayList<>();
    }

    public void activate() {
        this.active = true;
    }

    public void render(PoseStack pose, float tickDelta) {
        if (!this.active) {
            return;
        }

        if (shouldClose()) {
            this.active = false;
            // released
            return;
        }

        //TODO: draw radial menu
        Window window = Minecraft.getInstance().getWindow();
        this.fillGradient(pose, 0, 0, window.getGuiScaledWidth(), window.getGuiScaledHeight(), 0xC0101010, 0xD0101010);
    }

    private boolean shouldClose() {
        if (!Minecraft.getInstance().options.keyAttack.isDown()) {
            return true;
        }

        Player player = Minecraft.getInstance().player;
        if (player == null || player.getMainHandItem().getItem() != ModItems.WAND) {
            return true;
        }

        return false;
    }
}
