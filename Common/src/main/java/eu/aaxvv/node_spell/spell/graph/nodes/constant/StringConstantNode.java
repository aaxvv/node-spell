package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.node_widget.TextFieldWidget;
import eu.aaxvv.node_spell.client.widget.NodeConstants;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.nbt.CompoundTag;

public class StringConstantNode extends BaseConstantNode<String> {
    public StringConstantNode() {
        super("Const. Str.", Datatype.STRING, ModConstants.resLoc("const_str"), () -> "", Value::createString);
    }

    @Override
    public TextFieldWidget createWidget(NodeInstance instance) {
        TextFieldWidget field = new TextFieldWidget(instance, this.getWidth() - 6);
        field.setLocalPosition(2, NodeConstants.HEADER_HEIGHT + 2);
        return field;
    }

    @Override
    public void serializeInstanceData(Object instanceData, CompoundTag dataTag) {
        dataTag.putString("Val", (String) instanceData);
    }

    @Override
    public Object deserializeInstanceData(CompoundTag dataTag) {
        return dataTag.getString("Val");
    }
}
