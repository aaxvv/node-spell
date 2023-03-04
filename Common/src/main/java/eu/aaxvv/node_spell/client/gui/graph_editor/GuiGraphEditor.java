package eu.aaxvv.node_spell.client.gui.graph_editor;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.gui.base.UnboundedGuiElement;
import eu.aaxvv.node_spell.spell.graph.SpellGraph;

import java.util.ArrayList;
import java.util.List;

public class GuiGraphEditor extends UnboundedGuiElement {
    private final NodeGraphView graphView;
    private final List<GuiNodeView> selectedNodes;
    private final GuiElement edgeLayer;
    private final GuiElement nodeLayer;

    public GuiGraphEditor(SpellGraph graph) {
        super();
        this.selectedNodes = new ArrayList<>();
        this.edgeLayer = new UnboundedGuiElement();
        this.addChild(edgeLayer);
        this.nodeLayer = new UnboundedGuiElement();
        this.addChild(nodeLayer);
        this.graphView = new NodeGraphView(graph, this.edgeLayer, this.nodeLayer);

    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        super.render(pose, mouseX, mouseY, tickDelta);
    }
}
