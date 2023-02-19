package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.value.Value;

public class StringConstantNode extends BaseConstantNode<String> {
    public StringConstantNode() {
        super("Const. Str.", ModConstants.resLoc("const_str"), () -> "", Value::createString);
    }
}
