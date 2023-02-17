package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.spell.value.Value;

public class BoolConstantNode extends BaseConstantNode<Boolean> {
    public BoolConstantNode() {
        super("Bool Constant");
    }

    @Override
    protected Boolean getDefaultValue() {
        return false;
    }

    @Override
    protected Value createValue(Boolean instanceData) {
        return Value.createBool(instanceData);
    }
}
