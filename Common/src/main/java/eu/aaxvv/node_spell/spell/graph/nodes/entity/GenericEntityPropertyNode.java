package eu.aaxvv.node_spell.spell.graph.nodes.entity;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.world.entity.Entity;

import java.util.UUID;
import java.util.function.Function;

public class GenericEntityPropertyNode<T> extends Node {
    public final Socket sIn;
    public final Socket sOut;
    private final Function<Entity, T> conversionFunction;
    private final Datatype outputType;

    public GenericEntityPropertyNode(String resLoc, Datatype outputType, String outputName, Function<Entity, T> conversionFunction) {
        super(NodeCategories.ENTITY, ModConstants.resLoc(resLoc));
        this.sIn = addInputSocket(Datatype.ENTITY, "socket.node_spell.entity");
        this.sOut = addOutputSocket(outputType, "socket.node_spell." + outputName);
        this.outputType = outputType;
        this.conversionFunction = conversionFunction;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        UUID uuid = instance.getSocketValue(sIn, ctx).entityValue();
        Entity entity = ctx.getEntityOrThrow(uuid);
        T outputValue = conversionFunction.apply(entity);
        instance.setSocketValue(sOut, Value.create(outputType, outputValue));
    }

    @Override
    public int getWidth() {
        return (int) (super.getWidth() * 1.2f);
    }
}
