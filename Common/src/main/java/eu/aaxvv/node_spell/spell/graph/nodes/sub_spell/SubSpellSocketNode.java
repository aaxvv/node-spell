package eu.aaxvv.node_spell.spell.graph.nodes.sub_spell;

import eu.aaxvv.node_spell.spell.execution.SpellDeserializationContext;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public abstract class SubSpellSocketNode extends Node {
    public SubSpellSocketNode(NodeCategory category, ResourceLocation resourceLocation) {
        super(category, resourceLocation);
    }

    public String getName(NodeInstance instance) {
        if (instance.getInstanceData() instanceof String string) {
            return string;
        } else {
            return "";
        }
    }

    public abstract Socket.Direction getDirection();
    public abstract Datatype getDatatype();

    public int getSocketHash(NodeInstance instance) {
        return Socket.calculateSerializationHash(this.getDatatype(), this.getDirection(), this.getName(instance));
    }

    @Override
    public Object createInstanceData() {
        return "";
    }

    @Override
    public void serializeInstanceData(Object instanceData, CompoundTag dataTag) {
        dataTag.putString("Name", ((String) instanceData));
    }

    @Override
    public Object deserializeInstanceData(CompoundTag dataTag, SpellDeserializationContext context) {
        return dataTag.getString("Name");
    }

    public Socket createSocket(NodeInstance instance) {
        String name = ((String) instance.getInstanceData());
        return new Socket(this.getDatatype(), name, this.getDirection(), 0);
    }
}
