package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.NodeSpellCommon;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.gui.spell_list.GuiSpellListItem;
import eu.aaxvv.node_spell.client.screen.SpellBookScreen;
import eu.aaxvv.node_spell.helper.NbtHelper;
import eu.aaxvv.node_spell.spell.Spell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

public interface SpellDeserializationContext {
    Spell findSpell(UUID spellId);

    class ClientSide implements SpellDeserializationContext {
        private final SpellBookScreen screen;

        public ClientSide(SpellBookScreen screen) {
            this.screen = screen;
        }

        @Override
        public Spell findSpell(UUID spellId) {
            for (GuiElement listItem : this.screen.getSpellList().getChildren()) {
                GuiSpellListItem item = ((GuiSpellListItem) listItem);
                if (item.getSpellId().equals(spellId)) {
                    return getFromListItem(item, spellId);
                }
            }

            return null;
        }

        private Spell getFromListItem(GuiSpellListItem item, UUID spellId) {
            if (item.getCachedSpell() != null) {
                return item.getCachedSpell();
            } else {
                CompoundTag spellTag = NbtHelper.findSpellInBookTag(this.screen.getBookStack(), spellId);
                if (spellTag == null) {
                    return null;
                }

                Spell spell = Spell.fromNbt(spellTag, this);
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
        public Spell findSpell(UUID spellId) {
            CompoundTag spellTag = NbtHelper.findSpellInBookTag(this.spellBookStack, spellId);
            if (spellTag == null) {
                return null;
            }

            Optional<Spell> spell = NodeSpellCommon.playerSpellCache.getOrCreate(this.player.getUUID(), spellId, spellTag, this);
            return spell.orElse(null);
        }
    }
}
