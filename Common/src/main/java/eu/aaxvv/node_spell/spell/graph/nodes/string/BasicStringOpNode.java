package eu.aaxvv.node_spell.spell.graph.nodes.string;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Function;

public class BasicStringOpNode<T> extends Node {
    public final Socket sA;
    public final Socket sB;
    public final Socket sResult;

    private final BiFunction<String, String, T> operation;
    private final Function<T, Value> valueFunc;

    public BasicStringOpNode(String name, ResourceLocation resLoc, Datatype outputType, Function<T, Value> valueFunc, BiFunction<String, String, T> operation) {
        super(name, NodeCategories.STRING, resLoc);
        this.sA = addInputSocket(Datatype.STRING, "a");
        this.sB = addInputSocket(Datatype.STRING, "b");
        this.sResult = addOutputSocket(outputType, "Result");
        this.operation = operation;
        this.valueFunc = valueFunc;
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        String valueA = instance.getSocketValue(this.sA, ctx).stringValue();
        String valueB = instance.getSocketValue(this.sB, ctx).stringValue();
        T result = operation.apply(valueA, valueB);

        instance.setSocketValue(this.sResult, this.valueFunc.apply(result));
    }
}