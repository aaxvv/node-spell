package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public abstract class BaseConstantNode<T> extends Node<T> {
    public final Socket sValue;

    public BaseConstantNode(String name) {
        super(name, "Constants");
        this.sValue = addOutputSocket(Datatype.NUMBER, "Value");
    }

    @Override
    public T createInstanceData() {
        return getDefaultValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run(SpellContext ctx, NodeInstance instance) {
        instance.setSocketValue(this.sValue, createValue((T) instance.getInstanceData()));
    }

    protected abstract T getDefaultValue();

    protected abstract Value createValue(T instanceData);
}
