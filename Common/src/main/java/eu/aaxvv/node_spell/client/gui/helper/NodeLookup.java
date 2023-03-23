package eu.aaxvv.node_spell.client.gui.helper;

import eu.aaxvv.node_spell.spell.Spell;
import eu.aaxvv.node_spell.spell.execution.SpellDeserializationContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.Nodes;
import eu.aaxvv.node_spell.spell.graph.nodes.custom.SubSpellNode;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.*;

public class NodeLookup {
    private final Map<NodeCategory, List<Node>> nodesByCategory;
    private final List<NodeCategory> categoriesOrdered;
    private List<Node> subSpellNodes;
    private final int maxCategoryNameWidth;

    private final SpellDeserializationContext spellContext;

    public NodeLookup(SpellDeserializationContext spellContext) {
        this.nodesByCategory = new HashMap<>();
        this.categoriesOrdered = new ArrayList<>();
        this.subSpellNodes = null;
        this.spellContext = spellContext;
        setup();
        this.maxCategoryNameWidth = this.categoriesOrdered.stream().mapToInt(cat -> Minecraft.getInstance().font.width(Component.translatable(cat.translationKey))).max().orElse(0);
    }

    public int getMaxCategoryWidth() {
        return maxCategoryNameWidth + 2;
    }

    public List<NodeCategory> getCategoriesOrdered() {
        return this.categoriesOrdered;
    }

    public List<Node> getNodesForCategory(NodeCategory category) {
        return this.nodesByCategory.get(category);
    }

    private void setup() {
        for (var entry : NodeCategories.REGISTRY.entrySet()) {
            nodesByCategory.put(entry.getValue(), new ArrayList<>());
        }

        for (var entry : Nodes.REGISTRY.entrySet()) {
            NodeCategory category = entry.getValue().getCategory();
            nodesByCategory.get(category).add(entry.getValue());
        }

        for (List<Node> list : nodesByCategory.values()) {
            list.sort(Comparator.comparing(n -> (n.getDisplayName().getString().toLowerCase())));
        }


        List<NodeCategory> categoriesOrdered = NodeCategories.REGISTRY.entrySet().stream()
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(NodeCategory::getPriority))
                .toList();
        this.categoriesOrdered.addAll(categoriesOrdered);
    }

    private void setupSubSpells() {
        this.subSpellNodes = this.spellContext
                .getAllSpells()
                .stream()
                .map(SubSpellNode::create)
                .toList();
    }

    public List<Node> getSubSpellNodes() {
        if (this.subSpellNodes == null) {
            setupSubSpells();
        }
        return this.subSpellNodes;
    }

}
