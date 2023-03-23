package eu.aaxvv.node_spell.client.gui.graph_editor;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.GuiContext;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.gui.elements.GuiScrollContainer;
import eu.aaxvv.node_spell.client.gui.elements.GuiTextButton;
import eu.aaxvv.node_spell.client.gui.elements.GuiTextField;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import eu.aaxvv.node_spell.spell.graph.Nodes;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GuiQuickNodePopup extends GuiElement {
    private static final int HEIGHT = 160;
    private static final int WIDTH = 100;
    private static final int SEARCH_FIELD_HEIGHT = 13;

    private final GuiTextField searchField;
    private final GuiScrollContainer nodeList;
    private Consumer<Node> nodeClickedCallback;
    private final Predicate<Node> nodeFilter;

    public GuiQuickNodePopup(Predicate<Node> nodeFilter) {
        super(WIDTH, HEIGHT);
        this.nodeFilter = nodeFilter;
        this.searchField = new GuiTextField(this.getWidth(), SEARCH_FIELD_HEIGHT);
        this.searchField.setValueChangedCallback(this::updateResults);
        this.searchField.setEnterPressedCallback(this::enterPressed);
        addChild(this.searchField);
        this.nodeList = new GuiScrollContainer(this.getWidth() - 2, this.getHeight() - SEARCH_FIELD_HEIGHT);
        this.nodeList.setLocalPosition(1, SEARCH_FIELD_HEIGHT);
        addChild(this.nodeList);
//        updateResults("");
    }

    public GuiQuickNodePopup() {
        this(node -> true);
    }

    private void enterPressed() {
        if (this.nodeList.getChildren().isEmpty()) {
            return;
        }

        this.nodeList.getChildren().get(0).onMouseDown(0, 0, GLFW.GLFW_MOUSE_BUTTON_LEFT);
    }

    @Override
    public void setContext(GuiContext context) {
        super.setContext(context);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        RenderUtil.drawRect(pose, this.getGlobalX(), this.getGlobalY(), this.getWidth(), this.getHeight(), ModConstants.Colors.PAPER_BORDER);
        RenderUtil.drawRect(pose, this.getGlobalX() + 1, this.getGlobalY() + 1, this.getWidth() - 2, this.getHeight() - 2, ModConstants.Colors.PAPER_BG);

        super.render(pose, mouseX, mouseY, tickDelta);
    }

    private void updateResults(String searchText) {
        List<SearchResult> toDisplay = new ArrayList<>();
        for (var entry : Nodes.REGISTRY.entrySet()) {
            Node node = entry.getValue();
            if (!this.nodeFilter.test(node)) {
                continue;
            }

            String displayName = node.getDisplayName().getString();
            float matchScore = matchScore(displayName, searchText);
            if (matchScore != 0) {
                toDisplay.add(new SearchResult(node, displayName, matchScore));
            }
        }

        this.nodeList.getChildren().clear();
        Comparator<SearchResult> comp = Comparator.comparing(SearchResult::matchScore).reversed().thenComparing(SearchResult::displayName);
        toDisplay.sort(comp);

        for (SearchResult result : toDisplay) {
            GuiTextButton button = new GuiTextButton(0, 11, result.node.getDisplayName());
            button.setClickCallback(() -> this.nodeClicked(result.node));
            this.nodeList.addChild(button);
        }
    }

    private record SearchResult(Node node, String displayName, float matchScore) {};

    private float matchScore(String nodeName, String searchText) {
        if (searchText.isEmpty() || nodeName.isEmpty()) {
            return 1;
        }

        int searchIdx = 0;
        int score = 0;
        boolean continuous = true;
        for (int i = 0; i < nodeName.length(); i++) {
            char searchChar = Character.toLowerCase(searchText.charAt(searchIdx));
            char nameChar = Character.toLowerCase(nodeName.charAt(i));
            if (searchChar == nameChar) {
                score += continuous ? 2 : 1;
                searchIdx++;
                if (searchIdx >= searchText.length()) {
                    score += continuous ? 20 : 10;
                    break;
                }
            } else {
                if (searchIdx > 0) {
                    continuous = false;
                }
            }
        }

        return (float)score / nodeName.length();
    }

    private void nodeClicked(Node node) {
        if (this.nodeClickedCallback != null) {
            this.nodeClickedCallback.accept(node);
        }
        this.getContext().getPopupPane().closePopup(this);
    }

    public void setNodeClickedCallback(Consumer<Node> nodeClickedCallback) {
        this.nodeClickedCallback = nodeClickedCallback;
    }

    @Override
    public boolean onCharTyped(char character, int modifiers) {
        if (!this.searchField.isFocused()) {
            this.searchField.focus();
            if (character == ' ') {
                return true;
            }
        }

        return super.onCharTyped(character, modifiers);
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            if (this.nodeClickedCallback != null) {
                this.nodeClickedCallback.accept(null);
            }

            this.getContext().getPopupPane().closePopup(this);
            return true;
        }

        return super.onKeyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onRemove() {
        if (this.nodeClickedCallback != null) {
            this.nodeClickedCallback.accept(null);
        }
    }
}
