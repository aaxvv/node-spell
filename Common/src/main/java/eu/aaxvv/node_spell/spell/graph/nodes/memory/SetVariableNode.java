package eu.aaxvv.node_spell.spell.graph.nodes.memory;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.node_widget.TextFieldWidget;
import eu.aaxvv.node_spell.client.gui.node_widget.Widget;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.SimpleFlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.nbt.CompoundTag;

public class SetVariableNode extends SimpleFlowNode {
    public final Socket sIn;

    public SetVariableNode() {
        super(NodeCategories.MEMORY, ModConstants.resLoc("set_variable"));
        sIn = addInputSocket(Datatype.ANY, "socket.node_spell.value");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        String variableName = ((String) instance.getInstanceData());
        Value varValue = instance.getSocketValue(sIn, ctx);
        ctx.putLocal(variableName, varValue);
    }

    @Override
    public Widget<?> createWidget(NodeInstance instance) {
        TextFieldWidget field = new TextFieldWidget(instance, this.getWidth() - 8);
        field.setLocalPosition(4, ModConstants.Sizing.HEADER_HEIGHT + 2);
        return field;
    }

    @Override
    public Object createInstanceData() {
        return "";
    }

    @Override
    public void serializeInstanceData(Object instanceData, CompoundTag dataTag) {
        dataTag.putString("Var", (String) instanceData);
    }

    @Override
    public Object deserializeInstanceData(CompoundTag dataTag) {
        return dataTag.getString("Var");
    }
}
