package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.value.Value;

public class BoolConstantNode extends BaseConstantNode<Boolean> {
    public BoolConstantNode() {
        super("Const. Bool", ModConstants.resLoc("const_bool"), () -> false, Value::createBool);
    }
}
