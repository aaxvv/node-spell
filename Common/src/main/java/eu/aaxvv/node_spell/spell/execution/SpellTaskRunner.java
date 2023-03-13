package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResultHolder;
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

    public boolean startSpell(UUID casterId, Spell spell, SpellContext context) {
         if (spell.hasErrors()) {
             context.getCaster().asPlayer().ifPresent(player -> {
                 player.displayClientMessage(Component.translatable("gui.node_spell.spell_has_errors").withStyle(ChatFormatting.RED), true);
             });
            return false;
        }

        SpellRunner newRunner = new SpellRunner(spell.getGraph(), context);
        newRunner.start();

        SpellRunner prevRunner = this.runningSpells.put(casterId, newRunner);
        if (prevRunner != null) {
            prevRunner.stop();
        }

        return true;
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
                runner.ctx.getCaster().asPlayer().ifPresent(player -> {
                    player.displayClientMessage(Component.literal(ex.getShortDescription()).withStyle(ChatFormatting.RED), true);
                    if (ex.getCause() != null) {
                        player.sendSystemMessage(Component.literal(ex.getMessage()).withStyle(ChatFormatting.RED));
                    }
                });
                ModConstants.LOG.error("Failed to execute spell.", ex);
                runner.stop();
            }
        }
    }
}
