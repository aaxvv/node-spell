package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.spell.value.Value;

public class NumberConstantNode extends BaseConstantNode<Double> {
    public NumberConstantNode() {
        super("Number Constant");
    }

    @Override
    protected Double getDefaultValue() {
        return 0.0;
    }

    @Override
    protected Value createValue(Double instanceData) {
        return Value.createNumber(instanceData);
    }
}
