package eu.aaxvv.node_spell.spell.graph.nodes.generic;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

import java.util.function.BiFunction;

public class GenericIsInTagNode<T> extends Node {
    public final Socket sObjIn;
    public final Socket sTagIn;
    public final Socket sOut;

    private final BiFunction<T, String, Boolean> checkFunction;

    public GenericIsInTagNode(NodeCategory category, String resLoc, Datatype inType, String inputName, BiFunction<T, String, Boolean> checkFunction) {
        super(category, ModConstants.resLoc(resLoc));
        this.sObjIn = addInputSocket(inType, "socket.node_spell." + inputName);
        this.sTagIn = addInputSocket(Datatype.STRING, "socket.node_spell.tag");
        this.sOut = addOutputSocket(Datatype.BOOL, "socket.node_spell.bool");
        this.checkFunction = checkFunction;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run(SpellContext ctx, NodeInstance instance) {
        T obj = ((T) instance.getSocketValue(this.sObjIn, ctx).uncheckedValue());
        String tagName = instance.getSocketValue(this.sTagIn, ctx).stringValue();
        boolean isInTag = this.checkFunction.apply(obj, tagName);
        instance.setSocketValue(this.sOut, Value.createBool(isInTag));
    }
}
