package eu.aaxvv.node_spell.spell.graph.nodes.vector;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.world.phys.Vec3;

public class VectorDotNode extends Node {
    public final Socket sA;
    public final Socket sB;
    public final Socket sResult;

    public VectorDotNode() {
        super(NodeCategories.VECTOR, ModConstants.resLoc("vec_dot"));
        this.sA = addInputSocket(Datatype.VECTOR, "socket.node_spell.a");
        this.sB = addInputSocket(Datatype.VECTOR, "socket.node_spell.b");
        this.sResult = addOutputSocket(Datatype.NUMBER, "socket.node_spell.result");
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Vec3 valueA = instance.getSocketValue(this.sA, ctx).vectorValue();
        Vec3 valueB = instance.getSocketValue(this.sB, ctx).vectorValue();
        double result = valueA.dot(valueB);

        instance.setSocketValue(this.sResult, Value.createNumber(result));
    }
}