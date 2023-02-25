package eu.aaxvv.node_spell.spell.graph.nodes.comparison;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;

public class BasicNumberCompNode extends Node {
    public final Socket sA;
    public final Socket sB;
    public final Socket sResult;

    private final BiFunction<Double, Double, Boolean> operation;

    public BasicNumberCompNode(String name, ResourceLocation resLoc, BiFunction<Double, Double, Boolean> operation) {
        super(name, NodeCategories.COMPARISON, resLoc);
        this.sA = addInputSocket(Datatype.NUMBER, "a");
        this.sB = addInputSocket(Datatype.NUMBER, "b");
        this.sResult = addOutputSocket(Datatype.BOOL, "Result");
        this.operation = operation;
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Double valueA = instance.getSocketValue(this.sA, ctx).numberValue();
        Double valueB = instance.getSocketValue(this.sB, ctx).numberValue();
        Boolean result = operation.apply(valueA, valueB);

        instance.setSocketValue(this.sResult, Value.createBool(result));
    }
}