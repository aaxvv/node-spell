package eu.aaxvv.node_spell.spell.graph.nodes.block;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class GenericBlockPropertyNode<T> extends Node {
    public final Socket sIn;
    public final Socket sOut;
    private final Function<BlockState, T> conversionFunction;
    private final Datatype outputType;

    public GenericBlockPropertyNode(String resLoc, Datatype outputType, String outputName, Function<BlockState, T> conversionFunction) {
        super(NodeCategories.BLOCK, ModConstants.resLoc(resLoc));
        this.sIn = addInputSocket(Datatype.BLOCK, "socket.node_spell.block");
        this.sOut = addOutputSocket(outputType, "socket.node_spell." + outputName);
        this.outputType = outputType;
        this.conversionFunction = conversionFunction;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        BlockState stack = instance.getSocketValue(sIn, ctx).blockValue();
        T outputValue = conversionFunction.apply(stack);
        instance.setSocketValue(sOut, Value.create(outputType, outputValue));
    }

//    @Override
//    public int getMinWidth() {
//        return (int) (super.getMinWidth() * 1.2f);
//    }
}
