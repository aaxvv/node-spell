package eu.aaxvv.node_spell.spell.graph.nodes.sub_spell;

import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.sub_spell.SubSpellInstanceData;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.resources.ResourceLocation;

public class SubSpellInputNode extends Node {
    public final Socket sOut;

    public SubSpellInputNode(ResourceLocation resourceLocation, Datatype datatype) {
        super(NodeCategories.SUB_SPELL, resourceLocation);
        this.sOut = addOutputSocket(datatype, "socket.node_spell.empty");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        SubSpellInstanceData subSpellData = ctx.getCurrentRunner().getSubSpellData();
        SocketInstance socket = subSpellData.getSocketInstance(instance);
        instance.setSocketValue(this.sOut, socket.getComputedValue(ctx));
    }

    //TODO: use instance data to store name?
}
