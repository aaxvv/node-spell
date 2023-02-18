package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BaseConstantNode<T> extends Node<T> {
    public final Socket sValue;
    private final Function<T, Value> valueCreatorFunc;
    private final Supplier<T> defaultValueSupplier;

    public BaseConstantNode(String name, Supplier<T> defaultValueSupplier, Function<T, Value> valueCreatorFunc) {
        super(name, "Constants");
        this.sValue = addOutputSocket(Datatype.NUMBER, "Value");
        this.defaultValueSupplier = defaultValueSupplier;
        this.valueCreatorFunc = valueCreatorFunc;
    }

    @Override
    public T createInstanceData() {
        return defaultValueSupplier.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run(SpellContext ctx, NodeInstance instance) {
        instance.setSocketValue(this.sValue, valueCreatorFunc.apply((T) instance.getInstanceData()));
    }
}
