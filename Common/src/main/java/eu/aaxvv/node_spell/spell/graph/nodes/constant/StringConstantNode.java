package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.spell.value.Value;

public class StringConstantNode extends BaseConstantNode<String> {
    public StringConstantNode() {
        super("String Constant", () -> "", Value::createString);
    }
}
