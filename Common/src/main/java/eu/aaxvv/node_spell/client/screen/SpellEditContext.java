package eu.aaxvv.node_spell.client.screen;

import eu.aaxvv.node_spell.spell.graph.structure.Node;
import net.minecraft.client.Minecraft;

public class SpellEditContext {
    private static NewSpellEditScreen currentEditScreen;

    public static void openScreen(NewSpellEditScreen screen) {
        currentEditScreen = screen;
        Minecraft.getInstance().setScreen(screen);
    }

    public static void closeScreen() {
        Minecraft.getInstance().setScreen(currentEditScreen.getParentScreen());
        currentEditScreen = null;
    }

    public static NewSpellEditScreen getCurrentEditScreen() {
        return currentEditScreen;
    }

    public static void reVerifyGraph() {
        currentEditScreen.verifyGraph();
    }

    public static void addNodeAtCursor(Node node) {
        currentEditScreen.addNodeAtCursor(node);
    }
}
