package eu.aaxvv.node_spell.spell.graph.nodes.memory;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.node_widget.TextFieldWidget;
import eu.aaxvv.node_spell.client.gui.node_widget.Widget;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SpellDeserializationContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.nbt.CompoundTag;

public class GetVariableNode extends Node {
    public final Socket sOut;

    public GetVariableNode() {
        super(NodeCategories.MEMORY, ModConstants.resLoc("get_variable"));
        sOut = addOutputSocket(Datatype.ANY, "socket.node_spell.empty");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        String variableName = ((String) instance.getInstanceData());
        Value varValue = ctx.getLocal(variableName);
        instance.setSocketValue(sOut, varValue);
    }

    @Override
    public Widget<?> createWidget(NodeInstance instance) {
        TextFieldWidget field = new TextFieldWidget(instance, this.getMinWidth() - 6);
        field.setLocalPosition(2, ModConstants.Sizing.HEADER_HEIGHT + 2);
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
    public Object deserializeInstanceData(CompoundTag dataTag, SpellDeserializationContext context) {
        return dataTag.getString("Var");
    }
}
