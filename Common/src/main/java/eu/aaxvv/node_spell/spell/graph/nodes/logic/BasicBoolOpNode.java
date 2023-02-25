package eu.aaxvv.node_spell.spell.graph.nodes.logic;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

public class BasicBoolOpNode extends Node {
    public final Socket sA;
    public final Socket sB;
    public final Socket sResult;

    private final BiFunction<Boolean, Boolean, Boolean> operation;

    public BasicBoolOpNode(String name, ResourceLocation resLoc, BiFunction<Boolean, Boolean, Boolean> operation) {
        super(name, NodeCategories.LOGIC, resLoc);
        this.sA = addInputSocket(Datatype.BOOL, "a");
        this.sB = addInputSocket(Datatype.BOOL, "b");
        this.sResult = addOutputSocket(Datatype.BOOL, "Result");
        this.operation = operation;
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        boolean valueA = instance.getSocketValue(this.sA, ctx).boolValue();
        boolean valueB = instance.getSocketValue(this.sB, ctx).boolValue();
        boolean result = operation.apply(valueA, valueB);

        instance.setSocketValue(this.sResult, Value.createBool(result));
    }
}