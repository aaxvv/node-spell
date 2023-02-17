package eu.aaxvv.node_spell.spell;

import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class SpellContext {
    private Map<String, Value> locals;
    private Entity caster;

    private Level level;
}
