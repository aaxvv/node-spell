package eu.aaxvv.node_spell.spell.graph.nodes.generic;

import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.FlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.resources.ResourceLocation;

public class GenericCastNode extends FlowNode {
    public final Socket fIn;
    public final Socket fSucceededOut;
    public final Socket fFailedOut;

    public final Socket sValueIn;
    public final Socket sValueOut;

    private final Datatype outputType;

    public GenericCastNode(Datatype outputType, ResourceLocation resourceLocation) {
        super(NodeCategories.DATA_FLOW, resourceLocation);
        this.outputType = outputType;
        this.fIn = addInputSocket(Datatype.FLOW, "socket.node_spell.empty");
        this.fSucceededOut = addOutputSocket(Datatype.FLOW, "socket.node_spell.success");
        this.fFailedOut = addOutputSocket(Datatype.FLOW, "socket.node_spell.failure");

        this.sValueIn = addInputSocket(Datatype.ANY, "socket.node_spell.val");
        this.sValueOut = addOutputSocket(outputType, outputType.translationKey);
    }

    @Override
    public Socket getFlowContinueSocket(SpellContext ctx, NodeInstance instance) {
        Value inputVal = instance.getSocketValue(this.sValueIn, ctx);

        if (inputVal.getDatatype() == this.outputType) {
            return this.fSucceededOut;
        } else {
            return this.fFailedOut;
        }
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Value inputVal = instance.getSocketValue(this.sValueIn, ctx);

        if (inputVal.getDatatype() == this.outputType) {
            instance.setSocketValue(this.sValueOut, inputVal);
        } else {
            instance.setSocketInvalid(this.sValueOut);
        }
    }
}
