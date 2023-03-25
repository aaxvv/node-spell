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
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class RaycastEntityNode extends Node {
    public final Socket sOriginIn;
    public final Socket sDirectionIn;

    public final Socket sEntityOut;
    public final Socket sHitOut;

    public RaycastEntityNode() {
        super(NodeCategories.WORLD, ModConstants.resLoc("raycast_entity"));
        this.sOriginIn = addInputSocket(Datatype.VECTOR, "socket.node_spell.origin");
        this.sDirectionIn = addInputSocket(Datatype.VECTOR, "socket.node_spell.direction");
        this.sEntityOut = addOutputSocket(Datatype.ENTITY, "socket.node_spell.entity");
        this.sHitOut = addOutputSocket(Datatype.BOOL, "socket.node_spell.hit");

    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Optional<ServerPlayer> player = ctx.getCaster().asPlayer();
        if (player.isEmpty()) {
            throw new SpellExecutionException(Component.translatable("error.node_spell.raycast_without_entity"));
        }

        Vec3 origin = instance.getSocketValue(this.sOriginIn, ctx).vectorValue();
        Vec3 direction = instance.getSocketValue(this.sDirectionIn, ctx).vectorValue();

        Vec3 target = direction.normalize().scale(ModConstants.SPELL_INTERACTION_RANGE).add(origin);
        AABB aabb = new AABB(origin, target);

        EntityHitResult result = ProjectileUtil.getEntityHitResult(player.get(), origin, target, aabb, ent -> true, 1_000_000.0);

        if (result == null) {
            instance.setSocketValue(this.sHitOut, Value.createBool(false));
            instance.setSocketInvalid(this.sEntityOut);
        } else {
            instance.setSocketValue(this.sHitOut, Value.createBool(true));
            instance.setSocketValue(this.sEntityOut, Value.createEntity(result.getEntity().getUUID()));
        }
    }

//    @Override
//    public int getMinWidth() {
//        return super.getMinWidth() + 4;
//    }
}
