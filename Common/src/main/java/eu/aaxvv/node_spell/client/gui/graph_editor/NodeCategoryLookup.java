package eu.aaxvv.node_spell.client.gui.graph_editor;

import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.Nodes;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.*;

public class NodeCategoryLookup {
    private final Map<NodeCategory, List<Node>> nodesByCategory;
    private final Map<NodeCategory, Integer> maxNodeNameWidth;
    private final List<NodeCategory> categoriesOrdered;
    private final int maxCategoryNameWidth;

    public NodeCategoryLookup() {
        nodesByCategory = new HashMap<>();
        maxNodeNameWidth = new HashMap<>();
        categoriesOrdered = new ArrayList<>();
        setup();
        this.maxCategoryNameWidth = this.categoriesOrdered.stream().mapToInt(cat -> Minecraft.getInstance().font.width(Component.translatable(cat.translationKey))).max().orElse(0);
    }

    public int getMaxCategoryWidth() {
        return maxCategoryNameWidth + 2;
    }

    public int getMaxNodeNameWidth(NodeCategory category) {
        return this.maxNodeNameWidth.get(category);
    }

    public List<NodeCategory> getCategoriesOrdered() {
        return this.categoriesOrdered;
    }

    public List<Node> getNodesForCategory(NodeCategory category) {
        return this.nodesByCategory.get(category);
    }

    private void setup() {
        for (var entry : NodeCategories.REGISTRY_SUPPLIER.get().entrySet()) {
            nodesByCategory.put(entry.getValue(), new ArrayList<>());
        }

        for (var entry : Nodes.REGISTRY_SUPPLIER.get().entrySet()) {
            NodeCategory category = entry.getValue().getCategory();
            nodesByCategory.get(category).add(entry.getValue());
        }

        for (List<Node> list : nodesByCategory.values()) {
            list.sort(Comparator.comparing(n -> (Component.translatable(n.getTranslationKey()).toString())));
        }


        List<NodeCategory> categoriesOrdered = NodeCategories.REGISTRY_SUPPLIER.get().entrySet().stream()
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(NodeCategory::getPriority))
                .toList();
        this.categoriesOrdered.addAll(categoriesOrdered);


        for (var entry : nodesByCategory.entrySet()) {
            int maxWidth = entry.getValue().stream().mapToInt(node -> Minecraft.getInstance().font.width(Component.translatable(node.getTranslationKey()))).max().orElse(0);
            maxNodeNameWidth.put(entry.getKey(), maxWidth);
        }
    }
}
