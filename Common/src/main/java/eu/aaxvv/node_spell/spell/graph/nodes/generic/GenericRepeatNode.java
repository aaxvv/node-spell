package eu.aaxvv.node_spell.spell.graph.nodes.generic;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import eu.aaxvv.node_spell.spell.graph.structure.NodeStyle;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.resources.ResourceLocation;

public class GenericRepeatNode extends Node {
    public final Socket sIn;
    public final Socket sOut;

    public GenericRepeatNode(NodeCategory category, ResourceLocation resourceLocation, Datatype datatype) {
        super(category, resourceLocation);
        setStyle(new NodeStyle(ModConstants.Colors.WHITE, false));
        this.sIn = addInputSocket(datatype, "socket.node_spell.empty");
        this.sOut = addOutputSocket(datatype, "socket.node_spell.empty");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        instance.setSocketValue(this.sOut, instance.getSocketValue(this.sIn, ctx));
    }

    @Override
    public int getWidth() {
        return 12;
    }

    @Override
    public int getExpectedHeight() {
        return 9;
    }


}
