package eu.aaxvv.node_spell.spell.graph.nodes.world;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.server.level.ServerLevel;

import java.util.function.Function;

public class GenericWorldPropertyNode<T> extends Node {

    public final Socket sOut;
    private final Function<ServerLevel, T> conversionFunction;
    private final Datatype outputType;

    public GenericWorldPropertyNode(String resLoc, Datatype outputType, String outputName, Function<ServerLevel, T> conversionFunction) {
        super(NodeCategories.WORLD, ModConstants.resLoc(resLoc));
        this.sOut = addOutputSocket(outputType, "socket.node_spell." + outputName);
        this.outputType = outputType;
        this.conversionFunction = conversionFunction;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        ServerLevel level = ((ServerLevel) ctx.getLevel());
        T outputValue = conversionFunction.apply(level);
        instance.setSocketValue(sOut, Value.create(outputType, outputValue));
    }
}