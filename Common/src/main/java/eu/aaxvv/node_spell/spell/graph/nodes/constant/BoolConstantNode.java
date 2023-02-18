package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.spell.value.Value;

public class BoolConstantNode extends BaseConstantNode<Boolean> {
    public BoolConstantNode() {
        super("Const. Bool", () -> false, Value::createBool);
    }
}
