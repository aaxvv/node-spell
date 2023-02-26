package eu.aaxvv.node_spell.helper;

import eu.aaxvv.node_spell.NodeSpellCommon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;

public class EntityVelocityHelper {
    public static Vec3 getEntityVelocity(Entity entity) {
        if (entity instanceof ServerPlayer player) {
            return NodeSpellCommon.playerMotionRecorder.getMotion(player);
        } else if (entity instanceof AbstractArrow arrow) {
            // TODO: technically need an accessor into AbstractArrow here to check inGround
            return arrow.getDeltaMovement();
        } else {
            return entity.getDeltaMovement();
        }
    }
}
