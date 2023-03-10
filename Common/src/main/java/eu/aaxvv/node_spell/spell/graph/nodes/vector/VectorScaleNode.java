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

public class VectorScaleNode extends Node {
    public final Socket sVecIn;
    public final Socket sScaleIn;
    public final Socket sVecOut;

    public VectorScaleNode() {
        super(NodeCategories.VECTOR, ModConstants.resLoc("vec_scale"));
        this.sVecIn = addInputSocket(Datatype.VECTOR, "socket.node_spell.vector");
        this.sScaleIn = addInputSocket(Datatype.NUMBER, "socket.node_spell.val");
        this.sVecOut = addOutputSocket(Datatype.VECTOR, "socket.node_spell.vector");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Vec3 vec = instance.getSocketValue(this.sVecIn, ctx).vectorValue();
        double scale = instance.getSocketValue(this.sScaleIn, ctx).numberValue();

        Vec3 result = vec.scale(scale);
        instance.setSocketValue(this.sVecOut, Value.createVector(result));
    }
}
