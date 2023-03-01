package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class GuiHelper {
    public static boolean isKeyDown(int glfwKeyCode) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), glfwKeyCode);
    }

    public static boolean isShiftDown() {
        long windowHandle = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(windowHandle, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    public static boolean isControlDown() {
        long windowHandle = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(windowHandle, GLFW.GLFW_KEY_LEFT_CONTROL) || InputConstants.isKeyDown(windowHandle, GLFW.GLFW_KEY_RIGHT_CONTROL);
    }
}
