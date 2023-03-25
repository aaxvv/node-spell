package eu.aaxvv.node_spell.spell.graph.nodes.item;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class GenericItemPropertyNode<T> extends Node {
    public final Socket sIn;
    public final Socket sOut;
    private final Function<ItemStack, T> conversionFunction;
    private final Datatype outputType;

    public GenericItemPropertyNode(String resLoc, Datatype outputType, String outputName, Function<ItemStack, T> conversionFunction) {
        super(NodeCategories.ITEM, ModConstants.resLoc(resLoc));
        this.sIn = addInputSocket(Datatype.ITEM, "socket.node_spell.item");
        this.sOut = addOutputSocket(outputType, "socket.node_spell." + outputName);
        this.outputType = outputType;
        this.conversionFunction = conversionFunction;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        ItemStack stack = instance.getSocketValue(sIn, ctx).itemValue();
        T outputValue = conversionFunction.apply(stack);
        instance.setSocketValue(sOut, Value.create(outputType, outputValue));
    }

//    @Override
//    public int getMinWidth() {
//        return (int) (super.getMinWidth() * 1.2f);
//    }
}