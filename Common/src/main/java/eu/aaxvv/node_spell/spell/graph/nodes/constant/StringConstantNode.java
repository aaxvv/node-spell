package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.spell.value.Value;

public class StringConstantNode extends BaseConstantNode<String> {
    public StringConstantNode() {
        super("String Constant");
    }

    @Override
    protected String getDefaultValue() {
        return "";
    }

    @Override
    protected Value createValue(String instanceData) {
        return Value.createString(instanceData);
    }
}
