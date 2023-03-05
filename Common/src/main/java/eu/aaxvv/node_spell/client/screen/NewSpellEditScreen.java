package eu.aaxvv.node_spell.client.screen;

import eu.aaxvv.node_spell.client.gui.base.GuiPanContainer;
import eu.aaxvv.node_spell.client.gui.graph_editor.GuiGraphEditor;
import eu.aaxvv.node_spell.client.gui.graph_editor.GuiNodePicker;
import eu.aaxvv.node_spell.spell.Spell;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class NewSpellEditScreen extends BaseScreen {
    private final SpellBookScreen parentScreen;
    private final Spell spell;

    private final GuiNodePicker nodePicker;
    private final GuiPanContainer canvasContainer;
    private final GuiGraphEditor nodeGraph;

    public NewSpellEditScreen(SpellBookScreen parentScreen, Spell spell) {
        super(Component.literal(spell.getName()));
        this.parentScreen = parentScreen;
        this.spell = spell;

        this.nodePicker = new GuiNodePicker(this.getRootWidth(), 0);
        this.nodePicker.setLocalPosition(0, this.getRootHeight());
        this.getGuiRoot().addChild(this.nodePicker);

        this.nodeGraph = new GuiGraphEditor(this.spell.getGraph());
        this.canvasContainer = new GuiPanContainer(this.getRootWidth(), this.getRootHeight(), this.nodeGraph);
        this.canvasContainer.setLocalPosition(0, 0);
        this.getGuiRoot().addChild(this.canvasContainer);
    }

    @Override
    protected void init() {
        this.getGuiRoot().setSize(this.width, this.height);
        super.init();
        this.nodePicker.setWidth(this.getRootWidth());
        this.nodePicker.setLocalPosition(0, this.getRootHeight() - this.nodePicker.getHeight());
        this.canvasContainer.setHeight(this.getRootHeight() - this.nodePicker.getHeight());
        this.canvasContainer.setWidth(this.getRootWidth());
    }

    @Override
    public void onClose() {
        this.spell.getGraph().findEntrypoint();
        Minecraft.getInstance().setScreen(this.parentScreen);
    }
}
