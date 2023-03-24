package eu.aaxvv.node_spell.spell.graph.nodes.custom;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.Spell;
import eu.aaxvv.node_spell.spell.execution.*;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.FlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import java.lang.ref.WeakReference;

public class SubSpellPseudoNode extends FlowNode {
    // As long as a player has not deleted the spell on purpose,
    // it should always be referenced by either the spell cache on the server
    // or a spell list item on the client
    private final WeakReference<Spell> spell;
    private boolean hasSideEffects;
    private Component displayName;
    private Socket fOut;

    public SubSpellPseudoNode(Spell spell) {
        super(NodeCategories.CUSTOM, ModConstants.resLoc("sub_spell"));
        this.spell = new WeakReference<>(spell);
        refresh();
    }

    public void refresh() {
        populateSockets();
        Spell spell = this.spell.get();
        this.displayName = spell != null ? Component.literal(spell.getName()) : Component.literal("<MISSING SPELL>");
    }

    private void populateSockets() {
        clearSockets();
        this.hasSideEffects = false;

        Spell spell = this.spell.get();
        if (spell == null) {
            return;
        }

        this.hasSideEffects = spell.getGraph().hasSideEffects();

        if (this.hasSideEffects) {
            this.addInputSocket(Datatype.FLOW, "socket.node_spell.empty");
            this.fOut = this.addOutputSocket(Datatype.FLOW, "socket.node_spell.empty");
        }

        for (Socket socket : spell.getGraph().getExternalSockets().values()) {
            this.addSocket(socket);
            if (socket.getDirection().isIn()) {
                socket.setPositionOnNode(this.inSocketCount);
                this.inSocketCount++;
            } else {
                socket.setPositionOnNode(this.outSocketCount);
                this.outSocketCount++;
            }
        }
    }

    @Override
    public Socket getFlowContinueSocket(SpellContext ctx, NodeInstance instance) {
        return this.fOut;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Spell spell = this.spell.get();
        if (spell == null) {
            throw new SpellExecutionException(Component.translatable("error.node_spell.sub_spell_gone"));
        }

        new SubSpellRunner(spell.getGraph(), instance, ctx).calculateOutputsOnce();
    }

    @Override
    public SpellRunner getSubRunner(SpellContext ctx, NodeInstance instance) {
        Spell spell = this.spell.get();
        if (spell == null) {
            throw new SpellExecutionException(Component.translatable("error.node_spell.sub_spell_gone"));
        }

        return new SubSpellRunner(spell.getGraph(), instance, ctx);
    }

    @Override
    public boolean hasSideEffects() {
        return this.hasSideEffects;
    }

    @Override
    public Component getDisplayName() {
        return this.displayName;
    }

    @Override
    public Object createInstanceData() {
        return new Object();
    }

    @Override
    public void serializeInstanceData(Object instanceData, CompoundTag dataTag) {
        Spell spell = this.spell.get();
        if (spell != null) {
            dataTag.putUUID("SpellId", spell.getId());
        }
        super.serializeInstanceData(instanceData, dataTag);
    }

    @Override
    public Object deserializeInstanceData(CompoundTag dataTag, SpellDeserializationContext context) {
        return new Object();
    }
}
