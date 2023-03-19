package eu.aaxvv.node_spell.spell.graph.nodes.entity;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;

public class TargetPosNode extends Node {
    public final Socket sEntity;
    public final Socket sPosOut;
    public final Socket sSideOut;
    public final Socket sHit;

    public TargetPosNode() {
        super(NodeCategories.ENTITY, ModConstants.resLoc("entity_target_pos"));
        this.sEntity = addInputSocket(Datatype.ENTITY, "socket.node_spell.entity");
        this.sPosOut = addOutputSocket(Datatype.VECTOR, "socket.node_spell.pos");
        this.sSideOut = addOutputSocket(Datatype.VECTOR, "socket.node_spell.side");
        this.sHit = addOutputSocket(Datatype.BOOL, "socket.node_spell.hit");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Entity entity = ctx.getEntityOrThrow(instance.getSocketValue(this.sEntity, ctx).entityValue());

        Vec3 origin = entity.getEyePosition();
        Vec3 target = entity.getLookAngle().scale(ModConstants.SPELL_INTERACTION_RANGE).add(origin);

        BlockHitResult result = ctx.getLevel().clip(new ClipContext(origin, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

        if (result.getType() == HitResult.Type.BLOCK) {
            instance.setSocketValue(sPosOut, Value.createVector(Vec3.atLowerCornerOf(result.getBlockPos())));
            Vec3i normal = result.getDirection().getNormal();
            instance.setSocketValue(sSideOut, Value.createVector(new Vec3(normal.getX(), normal.getY(), normal.getZ())));
            instance.setSocketValue(this.sHit, Value.createBool(true));
        } else {
            instance.setSocketValue(sPosOut, Value.createVector(target));
            instance.setSocketValue(sSideOut, Value.createVector(Vec3.ZERO));
            instance.setSocketValue(this.sHit, Value.createBool(false));
        }
    }
}
