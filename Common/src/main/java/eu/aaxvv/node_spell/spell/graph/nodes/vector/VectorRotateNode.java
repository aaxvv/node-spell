package eu.aaxvv.node_spell.spell.graph.nodes.vector;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import eu.aaxvv.node_spell.util.VectorUtil;
import net.minecraft.world.phys.Vec3;

public class VectorRotateNode extends Node {
    public final Socket sAxisIn;
    public final Socket sAngleIn;
    public final Socket sVecIn;
    public final Socket sVecOut;

    public VectorRotateNode() {
        super(NodeCategories.VECTOR, ModConstants.resLoc("vector_rotate"));
        this.sAxisIn = addInputSocket(Datatype.VECTOR, "socket.node_spell.axis");
        this.sAngleIn = addInputSocket(Datatype.NUMBER, "socket.node_spell.angle");
        this.sVecIn = addInputSocket(Datatype.VECTOR, "socket.node_spell.vector");
        this.sVecOut = addOutputSocket(Datatype.VECTOR, "socket.node_spell.vector");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Vec3 vector = instance.getSocketValue(this.sVecIn, ctx).vectorValue();
        Vec3 axis = instance.getSocketValue(this.sAxisIn, ctx).vectorValue();
        double angle = instance.getSocketValue(this.sAngleIn, ctx).numberValue();

        Vec3 result = VectorUtil.rotateAxis(vector, angle, axis);
        instance.setSocketValue(this.sVecOut, Value.createVector(result));
    }
}
