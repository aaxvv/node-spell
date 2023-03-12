package eu.aaxvv.node_spell.spell.graph.nodes.generic;

import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.resources.ResourceLocation;

public class GenericSelectNode extends Node {
    public final Socket sInA;
    public final Socket sInB;
    public final Socket sInSelectA;
    public final Socket sOut;

    public GenericSelectNode(NodeCategory category, ResourceLocation resourceLocation, Datatype datatype) {
        super(category, resourceLocation);
        this.sInA = addInputSocket(datatype, "socket.node_spell.a");
        this.sInB = addInputSocket(datatype, "socket.node_spell.b");
        this.sInSelectA = addInputSocket(Datatype.BOOL, "socket.node_spell.select_a");
        this.sOut = addOutputSocket(datatype, "socket.node_spell.result");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        boolean selectA = instance.getSocketValue(this.sInSelectA, ctx).boolValue();
        Value result;
        if (selectA) {
            result = instance.getSocketValue(this.sInA, ctx);
        } else {
            result = instance.getSocketValue(this.sInB, ctx);
        }

        instance.setSocketValue(this.sOut, result);
    }
}
