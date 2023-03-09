package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.node_widget.NumberFieldWidget;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.nbt.CompoundTag;

public class NumberConstantNode extends BaseConstantNode<Double> {
    public NumberConstantNode() {
        super(Datatype.NUMBER, ModConstants.resLoc("const_num"), () -> 0.0, Value::createNumber);
    }

    @Override
    public NumberFieldWidget createWidget(NodeInstance instance) {
        NumberFieldWidget field = new NumberFieldWidget(instance, this.getWidth() - 6);
        field.setLocalPosition(2, ModConstants.Sizing.HEADER_HEIGHT + 2);
        return field;
    }

    @Override
    public void serializeInstanceData(Object instanceData, CompoundTag dataTag) {
        dataTag.putDouble("Val", (Double) instanceData);
    }

    @Override
    public Object deserializeInstanceData(CompoundTag dataTag) {
        return dataTag.getDouble("Val");
    }
}
