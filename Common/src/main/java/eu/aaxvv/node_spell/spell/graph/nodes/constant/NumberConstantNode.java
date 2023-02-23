package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.node_widget.NumberFieldWidget;
import eu.aaxvv.node_spell.client.widget.NodeConstants;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class NumberConstantNode extends BaseConstantNode<Double> {
    public NumberConstantNode() {
        super("Const. Num.", Datatype.NUMBER, ModConstants.resLoc("const_num"), () -> 0.0, Value::createNumber);
    }

    @Override
    public NumberFieldWidget createWidget(NodeInstance instance) {
        NumberFieldWidget field = new NumberFieldWidget(instance, this.getWidth() - 6);
        field.setLocalPosition(2, NodeConstants.HEADER_HEIGHT + 2);
        return field;
    }
}
