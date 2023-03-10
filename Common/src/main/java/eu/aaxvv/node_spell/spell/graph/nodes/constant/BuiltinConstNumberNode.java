package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.resources.ResourceLocation;

public class BuiltinConstNumberNode extends Node {
    public final Socket sOut;
    private final double value;

    public BuiltinConstNumberNode(ResourceLocation resourceLocation, double value) {
        super(NodeCategories.INPUT, resourceLocation);
        this.value = value;
        this.sOut = addOutputSocket(Datatype.NUMBER, "socket.node_spell.value");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        instance.setSocketValue(this.sOut, Value.createNumber(this.value));
    }
}
