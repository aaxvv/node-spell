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
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class TargetEntityNode extends Node {
    public final Socket sEntity;
    public final Socket sTargetEntity;
    public final Socket sHit;

    public TargetEntityNode() {
        super(NodeCategories.ENTITY, ModConstants.resLoc("entity_target_entity"));
        this.sEntity = addInputSocket(Datatype.ENTITY, "socket.node_spell.entity");
        this.sTargetEntity = addOutputSocket(Datatype.ENTITY, "socket.node_spell.entity");
        this.sHit = addOutputSocket(Datatype.BOOL, "socket.node_spell.hit");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Entity entity = ctx.getEntityOrThrow(instance.getSocketValue(this.sEntity, ctx).entityValue());

        Vec3 origin = entity.getEyePosition();
        Vec3 target = entity.getLookAngle().scale(ModConstants.SPELL_INTERACTION_RANGE).add(origin);
        AABB aabb = new AABB(origin, target);

        EntityHitResult result = ProjectileUtil.getEntityHitResult(entity, origin, target, aabb, entity1 -> true, 1_000_000.0);

        if (result != null) {
            instance.setSocketValue(this.sTargetEntity, Value.createEntity(result.getEntity().getUUID()));
            instance.setSocketValue(this.sHit, Value.createBool(true));
        } else {
            instance.setSocketInvalid(this.sTargetEntity);
            instance.setSocketValue(this.sHit, Value.createBool(false));
        }
    }
}
