package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.node_widget.BoolFieldWidget;
import eu.aaxvv.node_spell.client.widget.NodeConstants;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.nbt.CompoundTag;

public class BoolConstantNode extends BaseConstantNode<Boolean> {
    public BoolConstantNode() {
        super(Datatype.BOOL, ModConstants.resLoc("const_bool"), () -> false, Value::createBool);
    }

    @Override
    public BoolFieldWidget createWidget(NodeInstance instance) {
        BoolFieldWidget field = new BoolFieldWidget(instance, this.getWidth() - 6 - 30);
        field.setLocalPosition(2, NodeConstants.HEADER_HEIGHT + 3);
        return field;
    }

    @Override
    public void serializeInstanceData(Object instanceData, CompoundTag dataTag) {
        dataTag.putBoolean("Val", (Boolean) instanceData);
    }

    @Override
    public Object deserializeInstanceData(CompoundTag dataTag) {
        return dataTag.getBoolean("Val");
    }
}
