package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.node_widget.TextFieldWidget;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.nbt.CompoundTag;

public class StringConstantNode extends BaseConstantNode<String> {
    public StringConstantNode() {
        super(Datatype.STRING, ModConstants.resLoc("const_str"), () -> "", Value::createString);
    }

    @Override
    public TextFieldWidget createWidget(NodeInstance instance) {
        TextFieldWidget field = new TextFieldWidget(instance, this.getWidth() - 6);
        field.setLocalPosition(2, ModConstants.Sizing.HEADER_HEIGHT + 2);
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
