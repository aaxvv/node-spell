package eu.aaxvv.node_spell.spell;

import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.Map;

/**
 * The context for the execution of a single spell.
 * <p>
 * This is also used when invoking sub-spells or SubRunners within this spell execution.
 */
public class SpellContext {
    private Map<String, Value> locals;
    private Entity caster;
    private Level level;

    public Entity getCaster() {
        return caster;
    }

    public Level getLevel() {
        return level;
    }
}
