package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.node_widget.TextFieldWidget;
import eu.aaxvv.node_spell.client.widget.NodeConstants;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class StringConstantNode extends BaseConstantNode<String> {
    public StringConstantNode() {
        super("Const. Str.", Datatype.STRING, ModConstants.resLoc("const_str"), () -> "", Value::createString);
    }

    @Override
    public TextFieldWidget createWidget(NodeInstance instance) {
        TextFieldWidget field = new TextFieldWidget(instance, this.getWidth() - 6);
        field.setLocalPosition(2, NodeConstants.HEADER_HEIGHT + 2);
        return field;
    }
}
