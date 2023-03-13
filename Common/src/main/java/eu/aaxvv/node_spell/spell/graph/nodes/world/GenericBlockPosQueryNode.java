package eu.aaxvv.node_spell.spell.graph.nodes.world;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;

public class GenericBlockPosQueryNode<T> extends Node {
    public final Socket sPos;
    public final Socket sOut;
    private final BiFunction<ServerLevel, BlockPos, T> conversionFunction;
    private final Datatype outputType;

    public GenericBlockPosQueryNode(String resLoc, Datatype outputType, String outputName, BiFunction<ServerLevel, BlockPos, T> conversionFunction) {
        super(NodeCategories.WORLD, ModConstants.resLoc(resLoc));
        this.sPos = addInputSocket(Datatype.VECTOR, "socket.node_spell.position");
        this.sOut = addOutputSocket(outputType, "socket.node_spell." + outputName);
        this.outputType = outputType;
        this.conversionFunction = conversionFunction;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Vec3 vec = instance.getSocketValue(this.sPos, ctx).vectorValue();
        BlockPos pos = new BlockPos(vec.x, vec.y, vec.z);
        T outputValue = conversionFunction.apply(((ServerLevel) ctx.getLevel()), pos);
        instance.setSocketValue(sOut, Value.create(outputType, outputValue));
    }
}
