package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.NodeSpellCommon;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.gui.spell_list.GuiSpellListItem;
import eu.aaxvv.node_spell.client.screen.SpellBookScreen;
import eu.aaxvv.node_spell.helper.NbtHelper;
import eu.aaxvv.node_spell.spell.Spell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public abstract class SpellDeserializationContext {
    private final Deque<Spell> currentSpellStack = new ArrayDeque<>();

    public abstract Spell findSpell(UUID spellId);

    public abstract List<Spell> getAllSpells();

    public Spell getCurrentSpell() {
        if (this.currentSpellStack.isEmpty()) {
            throw new IllegalStateException("Tried to access current spell while none was set.");
        }

        return this.currentSpellStack.peekLast();
    }
    public void pushCurrentSpell(Spell spell) {
        this.currentSpellStack.push(spell);
    }
    public void popCurrentSpell() {
        this.currentSpellStack.removeLast();
    }

    public static class ClientSide extends SpellDeserializationContext {
        private final SpellBookScreen screen;

        public ClientSide(SpellBookScreen screen) {
            this.screen = screen;
        }

        @Override
        public Spell findSpell(UUID spellId) {
            for (GuiElement listItem : this.screen.getSpellList().getChildren()) {
                GuiSpellListItem item = ((GuiSpellListItem) listItem);
                if (item.getSpellId().equals(spellId)) {
                    return getFromListItem(item);
                }
            }

            return null;
        }

        @Override
        public List<Spell> getAllSpells() {
            return this.screen
                    .getSpellList()
                    .getChildren()
                    .stream()
                    .map(item -> getFromListItem(((GuiSpellListItem) item)))
                    .toList();
        }

        private Spell getFromListItem(GuiSpellListItem item) {
            if (item.getCachedSpell() != null) {
                return item.getCachedSpell();
            } else {
                CompoundTag spellTag = NbtHelper.findSpellInBookTag(this.screen.getBookStack(), item.getSpellId());
                if (spellTag == null) {
                    return null;
                }

                Spell spell = Spell.fromNbt(spellTag, this);
                item.setCachedSpell(spell);
                return spell;
            }
        }
    }

    public static class ServerSide extends SpellDeserializationContext {
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

        @Override
        public List<Spell> getAllSpells() {
            List<Spell> spells = new ArrayList<>();

            for (Tag spellTag : this.spellBookStack.getOrCreateTag().getList("Spells", Tag.TAG_COMPOUND)) {
                if (spellTag instanceof CompoundTag compound) {
                    UUID id = compound.getUUID("Id");
                    Optional<Spell> spell = NodeSpellCommon.playerSpellCache.getOrCreate(this.player.getUUID(), id, compound, this);
                    spell.ifPresent(spells::add);
                }
            }

            return spells;
        }
    }
}
