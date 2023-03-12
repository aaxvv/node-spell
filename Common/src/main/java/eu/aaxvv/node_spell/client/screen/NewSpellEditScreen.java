package eu.aaxvv.node_spell.client.screen;

import eu.aaxvv.node_spell.client.gui.elements.GuiMultilineText;
import eu.aaxvv.node_spell.client.gui.graph_editor.GuiNodeView;
import eu.aaxvv.node_spell.client.gui.helper.GuiHelper;
import eu.aaxvv.node_spell.client.gui.elements.GuiPanContainer;
import eu.aaxvv.node_spell.client.gui.graph_editor.GuiGraphEditor;
import eu.aaxvv.node_spell.client.gui.graph_editor.GuiNodePicker;
import eu.aaxvv.node_spell.spell.Spell;
import eu.aaxvv.node_spell.spell.graph.verification.GraphVerifier;
import eu.aaxvv.node_spell.spell.graph.verification.VerificationResult;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class NewSpellEditScreen extends BaseScreen {
    private final SpellBookScreen parentScreen;
    private final Spell spell;
    private final GraphVerifier verifier;

    private final GuiNodePicker nodePicker;
    private final GuiPanContainer canvasContainer;
    private final GuiGraphEditor nodeGraph;
    private final GuiMultilineText errorText;

    public NewSpellEditScreen(SpellBookScreen parentScreen, Spell spell) {
        super(Component.literal(spell.getName()));
        this.parentScreen = parentScreen;
        this.spell = spell;
        this.verifier = new GraphVerifier(this.spell.getGraph());

        this.nodePicker = new GuiNodePicker(this.getRootWidth(), 0);
        this.nodePicker.setLocalPosition(0, this.getRootHeight());
        this.getGuiRoot().addChild(this.nodePicker);

        this.nodeGraph = new GuiGraphEditor(this.spell.getGraph());
        this.nodeGraph.setGraphChangedCallback(this::verifyGraph);
        this.canvasContainer = new GuiPanContainer(this.getRootWidth(), this.getRootHeight(), this.nodeGraph);
        this.canvasContainer.setLocalPosition(0, 0);
        this.getGuiRoot().addChild(this.canvasContainer);

        this.nodePicker.setNodeCreatedCallback(node -> {
            double x = GuiHelper.getMouseScreenX();
            double y = GuiHelper.getMouseScreenY();
            this.nodeGraph.addNode(node.createInstance(), x, y);
        });

        this.errorText = new GuiMultilineText();
        this.errorText.setLocalPosition(2, 2);
        this.getGuiRoot().addChild(this.errorText);

        verifyGraph();
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

    private void verifyGraph() {
        boolean isOk = this.verifier.check();
        this.errorText.getLines().clear();

        if (!isOk) {
            this.errorText.getLines().add(Component.translatable("gui.node_spell.spell_verification.problems_heading"));
            this.verifier.getProblems().stream()
                    .filter(VerificationResult::isError)
                    .map(VerificationResult::getDescription)
                    .forEach(this.errorText.getLines()::add);
        }

        for (GuiNodeView nodeView : this.nodeGraph.getGraphView().getNodes()) {
            nodeView.setHighlightColor(this.verifier.getNodeErrorLevel(nodeView.getInstance()).highlightColor);
        }

    }
}
