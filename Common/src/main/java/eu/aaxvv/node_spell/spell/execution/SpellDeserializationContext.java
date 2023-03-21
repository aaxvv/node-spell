package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.NodeSpellCommon;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.gui.spell_list.GuiSpellListItem;
import eu.aaxvv.node_spell.client.screen.SpellBookScreen;
import eu.aaxvv.node_spell.spell.Spell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface SpellDeserializationContext {
    Spell findSpell(String name);

    class ClientSide implements SpellDeserializationContext {
        private final SpellBookScreen screen;

        public ClientSide(SpellBookScreen screen) {
            this.screen = screen;
        }

        @Override
        public Spell findSpell(String name) {
            for (GuiElement listItem : this.screen.getSpellList().getChildren()) {
                GuiSpellListItem item = ((GuiSpellListItem) listItem);
                if (item.getSpellName().equals(name)) {
                    return getFromListItem(item, name);
                }
            }

            return null;
        }

        private Spell getFromListItem(GuiSpellListItem item, String name) {
            if (item.getCachedSpell() != null) {
                return item.getCachedSpell();
            } else {
                CompoundTag spellList = this.screen.getBookStack().getOrCreateTagElement("Spells");
                if (!spellList.contains(name)) {
                    return null;
                }

                Spell spell = Spell.fromNbt(spellList.getCompound(name), this);
                item.setCachedSpell(spell);
                return spell;
            }
        }
    }

    class ServerSide implements SpellDeserializationContext {
        private final Player player;
        private final ItemStack spellBookStack;

        public ServerSide(Player player, ItemStack spellBookStack) {
            this.player = player;
            this.spellBookStack = spellBookStack;
        }

        @Override
        public Spell findSpell(String name) {
            CompoundTag spellList = this.spellBookStack.getOrCreateTagElement("Spells");
            if (!spellList.contains(name)) {
                return null;
            }

            Optional<Spell> spell = NodeSpellCommon.playerSpellCache.getOrCreate(this.player.getUUID(), name, spellList.getCompound(name), this);
            return spell.orElse(null);
        }
    }
}
