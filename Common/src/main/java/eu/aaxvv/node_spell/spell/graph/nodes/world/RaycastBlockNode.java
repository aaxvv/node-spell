package eu.aaxvv.node_spell.spell.graph.nodes.world;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellExecutionException;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class RaycastBlockNode extends Node {
    public final Socket sOriginIn;
    public final Socket sDirectionIn;

    public final Socket sPosOut;
    public final Socket sSideOut;
    public final Socket sHitOut;

    public RaycastBlockNode() {
        super(NodeCategories.WORLD, ModConstants.resLoc("ray_cast_block"));
        this.sOriginIn = addInputSocket(Datatype.VECTOR, "socket.node_spell.origin");
        this.sDirectionIn = addInputSocket(Datatype.VECTOR, "socket.node_spell.direction");
        this.sPosOut = addOutputSocket(Datatype.VECTOR, "socket.node_spell.pos");
        this.sSideOut = addOutputSocket(Datatype.VECTOR, "socket.node_spell.side");
        this.sHitOut = addOutputSocket(Datatype.BOOL, "socket.node_spell.hit");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Vec3 source = instance.getSocketValue(sOriginIn, ctx).vectorValue();
        Vec3 direction = instance.getSocketValue(sDirectionIn, ctx).vectorValue();

        Vec3 target = direction.normalize().scale(ModConstants.SPELL_INTERACTION_RANGE).add(source);

        //TODO: is there a way to do this without an entity?
        // make a fake player for auto casters
        Optional<ServerPlayer> player = ctx.getCaster().asPlayer();
        if (player.isEmpty()) {
            throw new SpellExecutionException("Cannot ray-cast without entity.");
        }

        BlockHitResult result = ctx.getLevel().clip(new ClipContext(source, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player.get()));
        if (result.getType() == HitResult.Type.MISS) {
            instance.setSocketValue(sPosOut, Value.createVector(target));
            instance.setSocketValue(sSideOut, Value.createVector(Vec3.ZERO));
            instance.setSocketValue(sHitOut, Value.createBool(false));
        } else {
            instance.setSocketValue(sPosOut, Value.createVector(Vec3.atLowerCornerOf(result.getBlockPos())));
            Vec3i normal = result.getDirection().getNormal();
            instance.setSocketValue(sSideOut, Value.createVector(new Vec3(normal.getX(), normal.getY(), normal.getZ())));
            instance.setSocketValue(sHitOut, Value.createBool(true));
        }
    }

    @Override
    public int getWidth() {
        return super.getWidth() + 4;
    }
}
