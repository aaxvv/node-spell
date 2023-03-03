package eu.aaxvv.node_spell.helper;

import eu.aaxvv.node_spell.NodeSpellCommon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public class EntityHelper {
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

    public static boolean isSneaking(Entity entity) {
        if (entity instanceof ServerPlayer player) {
            return player.isShiftKeyDown();
        } else {
            return false;
        }
    }

    public static Optional<Entity> getFromUuid(Level level, UUID uuid) {
        if (level instanceof ServerLevel serverLevel) {
            return Optional.ofNullable(serverLevel.getEntity(uuid));
        } else {
            return Optional.empty();
        }
    }
}
