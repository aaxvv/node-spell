package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.helper.EntityHelper;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * The context for the execution of a single spell.
 * <p>
 * This is also used when invoking sub-spells or SubRunners within this spell execution.
 */
public class SpellContext {
    private final String spellName;
    private final Map<String, Value> locals;
    private final CasterWrapper caster;
    private final Level level;

    public SpellContext(ServerPlayer player, Level level, String spellName) {
        this.caster = new CasterWrapper(player);
        this.level = level;
        this.spellName = spellName;
        this.locals = new HashMap<>();
    }

    public void putLocal(String name, Value value) {
        this.locals.put(name, value);
    }

    public Value getLocal(String name) {
        Value localValue = this.locals.get(name);
        if (localValue == null) {
            throw new SpellExecutionException("Tried to access value of variable '" + name + "' before it was set.");
        }
        return this.locals.get(name);
    }

    public Entity getEntityOrThrow(UUID uuid) {
        return EntityHelper.getFromUuid(this.level, uuid).orElseThrow(() -> new SpellExecutionException("Cannot find entity"));
    }

    public CasterWrapper getCaster() {
        return caster;
    }

    public Level getLevel() {
        return level;
    }

    public String getSpellName() {
        return spellName;
    }

    public static class CasterWrapper {
        private final Object caster;
        public final Vec3 position;
        public final BlockPos blockPos;
        public final Container container;
        public final UUID uuid;

        public CasterWrapper(ServerPlayer player) {
            this.caster = player;
            this.position = player.position();
            this.blockPos = player.blockPosition();
            this.container = player.getInventory();
            this.uuid = player.getUUID();
        }

        public Optional<ServerPlayer> asPlayer() {
            if (this.caster instanceof ServerPlayer player) {
                return Optional.of(player);
            } else {
                return Optional.empty();
            }
        }
    }
}
