package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.spell.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class SpellTaskRunner {
    private final Map<UUID, SpellRunner> runningSpells;

    public SpellTaskRunner() {
        runningSpells = new HashMap<>();
    }

    public void startSpell(UUID casterId, Spell spell, SpellContext context) {
        SpellRunner newRunner = new SpellRunner(spell.getGraph(), context);
        newRunner.start();

        SpellRunner prevRunner = this.runningSpells.put(casterId, newRunner);
        if (prevRunner != null) {
            prevRunner.stop();
        }
    }

    public void onTick(Level level) {
        Iterator<Map.Entry<UUID, SpellRunner>> iterator = this.runningSpells.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            SpellRunner runner = entry.getValue();
            if (runner.ctx.getLevel() != level) {
                continue;
            }

            if (!runner.running) {
                iterator.remove();
            }

            try {
                runner.tick();

            } catch (SpellExecutionException ex) {
                if (runner.ctx.getCaster() instanceof Player player) {
                    player.displayClientMessage(Component.literal(ex.getMessage()).withStyle(ChatFormatting.RED), true);
                }
                runner.stop();
            }
        }
    }
}
