package eu.aaxvv.node_spell.spell.graph.nodes.math;

import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;

public class BasicVectorOpNode extends Node {
    public final Socket sA;
    public final Socket sB;
    public final Socket sResult;

    private final BiFunction<Vec3, Vec3, Vec3> operation;

    public BasicVectorOpNode(ResourceLocation resLoc, BiFunction<Vec3, Vec3, Vec3> operation) {
        super(NodeCategories.MATH, resLoc);
        this.sA = addInputSocket(Datatype.VECTOR, "socket.node_spell.a");
        this.sB = addInputSocket(Datatype.VECTOR, "socket.node_spell.b");
        this.sResult = addOutputSocket(Datatype.VECTOR, "socket.node_spell.result");
        this.operation = operation;
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Vec3 valueA = instance.getSocketValue(this.sA, ctx).vectorValue();
        Vec3 valueB = instance.getSocketValue(this.sB, ctx).vectorValue();
        Vec3 result = operation.apply(valueA, valueB);

        instance.setSocketValue(this.sResult, Value.createVector(result));
    }
}