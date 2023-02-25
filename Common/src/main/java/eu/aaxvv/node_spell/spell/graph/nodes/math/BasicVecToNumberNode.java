package eu.aaxvv.node_spell.spell.graph.nodes.math;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class BasicVecToNumberNode extends Node {
    public final Socket sA;
    public final Socket sResult;

    private final Function<Vec3, Double> operation;

    public BasicVecToNumberNode(String name, ResourceLocation resLoc, Function<Vec3, Double> operation) {
        super(name, NodeCategories.MATH, resLoc);
        this.sA = addInputSocket(Datatype.VECTOR, "");
        this.sResult = addOutputSocket(Datatype.NUMBER, "Result");
        this.operation = operation;
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Vec3 valueA = instance.getSocketValue(this.sA, ctx).vectorValue();
        double result = operation.apply(valueA);

        instance.setSocketValue(this.sResult, Value.createNumber(result));
    }
}