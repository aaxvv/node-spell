package eu.aaxvv.node_spell.spell.graph.nodes.math;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.function.TriFunction;

public class BasicNumberTriOpNode extends Node {
    public final Socket sA;
    public final Socket sB;
    public final Socket sC;
    public final Socket sResult;

    private final TriFunction<Double, Double, Double, Double> operation;

    public BasicNumberTriOpNode(String name, ResourceLocation resLoc, TriFunction<Double, Double, Double, Double> operation) {
        super(name, NodeCategories.MATH, resLoc);
        this.sA = addInputSocket(Datatype.NUMBER, "a");
        this.sB = addInputSocket(Datatype.NUMBER, "b");
        this.sC = addInputSocket(Datatype.NUMBER, "c");
        this.sResult = addOutputSocket(Datatype.NUMBER, "Result");
        this.operation = operation;
    }

    public BasicNumberTriOpNode(String name, ResourceLocation resLoc, String inputAName, String inputBName, String inputCName, TriFunction<Double, Double, Double, Double> operation) {
        super(name, NodeCategories.MATH, resLoc);
        this.sA = addInputSocket(Datatype.NUMBER, inputAName);
        this.sB = addInputSocket(Datatype.NUMBER, inputBName);
        this.sC = addInputSocket(Datatype.NUMBER, inputCName);
        this.sResult = addOutputSocket(Datatype.NUMBER, "Result");
        this.operation = operation;
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        double valueA = instance.getSocketValue(this.sA, ctx).numberValue();
        double valueB = instance.getSocketValue(this.sB, ctx).numberValue();
        double valueC = instance.getSocketValue(this.sC, ctx).numberValue();
        double result = operation.apply(valueA, valueB, valueC);

        instance.setSocketValue(this.sResult, Value.createNumber(result));
    }
}