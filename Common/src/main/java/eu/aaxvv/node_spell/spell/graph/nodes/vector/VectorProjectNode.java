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

public class VectorProjectNode extends Node {
    public final Socket sVecIn;
    public final Socket sOnVecIn;
    public final Socket sVecOut;

    public VectorProjectNode() {
        super(NodeCategories.VECTOR, ModConstants.resLoc("vector_project"));
        this.sVecIn = addInputSocket(Datatype.VECTOR, "socket.node_spell.vector");
        this.sOnVecIn = addInputSocket(Datatype.VECTOR, "socket.node_spell.onto");
        this.sVecOut = addOutputSocket(Datatype.VECTOR, "socket.node_spell.vector");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Vec3 vector = instance.getSocketValue(this.sVecIn, ctx).vectorValue();
        Vec3 on = instance.getSocketValue(this.sOnVecIn, ctx).vectorValue();
        Vec3 onNorm = on.normalize();

        Vec3 result = onNorm.scale(onNorm.dot(vector));
        instance.setSocketValue(this.sVecOut, Value.createVector(result));
    }
}
