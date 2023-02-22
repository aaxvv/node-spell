package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class NumberConstantNode extends BaseConstantNode<Double> {
    public NumberConstantNode() {
        super("Const. Num.", Datatype.NUMBER, ModConstants.resLoc("const_num"), () -> 0.0, Value::createNumber);
    }
}
