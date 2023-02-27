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

public class VectorConstructNode extends Node {
    public final Socket sX;
    public final Socket sY;
    public final Socket sZ;
    public final Socket sVec;

    public VectorConstructNode() {
        super(NodeCategories.VECTOR, ModConstants.resLoc("vec_construct"));
        this.sX = addInputSocket(Datatype.NUMBER, "socket.node_spell.x");
        this.sY = addInputSocket(Datatype.NUMBER, "socket.node_spell.y");
        this.sZ = addInputSocket(Datatype.NUMBER, "socket.node_spell.z");
        this.sVec = addOutputSocket(Datatype.VECTOR, "socket.node_spell.vector");
    }

    @Override
    public Void createInstanceData() {
        return null;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        double valueX = instance.getSocketValue(this.sX, ctx).numberValue();
        double valueY = instance.getSocketValue(this.sY, ctx).numberValue();
        double valueZ = instance.getSocketValue(this.sZ, ctx).numberValue();
        Vec3 result = new Vec3(valueX, valueY, valueZ);

        instance.setSocketValue(this.sVec, Value.createVector(result));
    }
}