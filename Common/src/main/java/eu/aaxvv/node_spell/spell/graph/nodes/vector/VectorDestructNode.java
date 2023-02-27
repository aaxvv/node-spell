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

public class VectorDestructNode  extends Node {
    public final Socket sX;
    public final Socket sY;
    public final Socket sZ;
    public final Socket sVec;

    public VectorDestructNode() {
        super(NodeCategories.VECTOR, ModConstants.resLoc("vec_destruct"));
        this.sVec = addInputSocket(Datatype.VECTOR, "socket.node_spell.vector");
        this.sX = addOutputSocket(Datatype.NUMBER, "socket.node_spell.x");
        this.sY = addOutputSocket(Datatype.NUMBER, "socket.node_spell.y");
        this.sZ = addOutputSocket(Datatype.NUMBER, "socket.node_spell.z");
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Vec3 vec = instance.getSocketValue(this.sVec, ctx).vectorValue();

        instance.setSocketValue(this.sX, Value.createNumber(vec.x));
        instance.setSocketValue(this.sY, Value.createNumber(vec.y));
        instance.setSocketValue(this.sZ, Value.createNumber(vec.z));
    }
}