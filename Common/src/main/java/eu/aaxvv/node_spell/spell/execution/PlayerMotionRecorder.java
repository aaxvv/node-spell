package eu.aaxvv.node_spell.spell.execution;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.WeakHashMap;

/*+
 * Minecraft doesn't actually store proper velocity on player objects, so we need to record it ourselves.
 *
 * Taken from hexcasting, since they have exactly the same problem:
 * https://github.com/gamma-delta/HexMod/blob/main/Common/src/main/java/at/petrak/hexcasting/common/misc/PlayerPositionRecorder.java
 */
public class PlayerMotionRecorder {
    private final Map<Player, Vec3> lastSecondPositionMap;
    private final Map<Player, Vec3> lastPositionMap;

    public PlayerMotionRecorder() {
        lastSecondPositionMap = new WeakHashMap<>();
        lastPositionMap = new WeakHashMap<>();
    }

    public void updateAllPlayers(ServerLevel world) {
        for (ServerPlayer player : world.players()) {
            var prev = lastPositionMap.get(player);
            if (prev != null)
                lastSecondPositionMap.put(player, prev);
            lastPositionMap.put(player, player.position());
        }
    }

    public Vec3 getMotion(ServerPlayer player) {
        Vec3 vec = lastPositionMap.get(player);
        Vec3 prev = lastSecondPositionMap.get(player);

        if (vec == null)
            return Vec3.ZERO;

        if (prev == null)
            return player.position().subtract(vec);

        return vec.subtract(prev);
    }
}
