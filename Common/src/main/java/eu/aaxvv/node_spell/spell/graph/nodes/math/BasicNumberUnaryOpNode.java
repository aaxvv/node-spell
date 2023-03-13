package eu.aaxvv.node_spell.spell.graph.nodes.math;

import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class BasicNumberUnaryOpNode extends Node {
    public final Socket sA;
    public final Socket sResult;

    private final Function<Double, Double> operation;

    public BasicNumberUnaryOpNode(ResourceLocation resLoc, Function<Double, Double> operation) {
        super(NodeCategories.MATH, resLoc);
        this.sA = addInputSocket(Datatype.NUMBER, "socket.node_spell.val");
        this.sResult = addOutputSocket(Datatype.NUMBER, "socket.node_spell.result");
        this.operation = operation;
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        double valueA = instance.getSocketValue(this.sA, ctx).numberValue();
        double result = operation.apply(valueA);

        instance.setSocketValue(this.sResult, Value.createNumber(result));
    }
}