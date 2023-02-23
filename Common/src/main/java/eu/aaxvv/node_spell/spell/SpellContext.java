package eu.aaxvv.node_spell.spell;

import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

/**
 * The context for the execution of a single spell.
 * <p>
 * This is also used when invoking sub-spells or SubRunners within this spell execution.
 */
public class SpellContext {
    private final Map<String, Value> locals;
    private final Entity caster;
    private final Level level;

    public SpellContext(Entity caster, Level level) {
        this.caster = caster;
        this.level = level;
        this.locals = new HashMap<>();
    }

    public void putLocal(String name, Value value) {
        this.locals.put(name, value);
    }

    public Value getLocal(String name) {
        return this.locals.get(name);
    }

    public Entity getCaster() {
        return caster;
    }

    public Level getLevel() {
        return level;
    }
}
