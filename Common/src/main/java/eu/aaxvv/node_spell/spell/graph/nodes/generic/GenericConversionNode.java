package eu.aaxvv.node_spell.spell.graph.nodes.generic;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.Function;

/**
 * A node which converts one type of value into another without side effects.
 * <p>
 * If the specified input datatype is 'ANY', the conversion function will receive the raw {@link Value} instead of an unpacked inner value.
 * @param <I> inner java type of input value
 * @param <O> inner java type of output value
 */
public class GenericConversionNode<I, O> extends Node {
    public final Socket sIn;
    public final Socket sOut;
    private final Function<I, O> accessFunction;
    private final Datatype inputType;
    private final Datatype outputType;

    /**
     * If the specified input datatype is 'ANY', the conversion function will receive the raw {@link Value} instead of an unpacked inner value.
     * @param category node category
     * @param resourceLocation node resource location
     * @param inputType datatype of input socket
     * @param outputType datatype of output socket
     * @param inputSocketTranslationKey translation key of input socket
     * @param outputSocketTranslationKey translation key of output socket
     * @param conversionFunction function that will be called to convert input to output value
     */
    public GenericConversionNode(NodeCategory category, ResourceLocation resourceLocation, Datatype inputType, Datatype outputType, String inputSocketTranslationKey, String outputSocketTranslationKey, Function<I, O> conversionFunction) {
        super(category, resourceLocation);
        this.accessFunction = conversionFunction;
        this.inputType = inputType;
        this.outputType = outputType;
        this.sIn = addInputSocket(inputType, inputSocketTranslationKey);
        this.sOut = addOutputSocket(outputType, outputSocketTranslationKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run(SpellContext ctx, NodeInstance instance) {
        Value socketVal = instance.getSocketValue(sIn, ctx);

        I input = (I) (inputType == Datatype.ANY ? socketVal : socketVal.uncheckedValue());
        O output = accessFunction.apply(input);

        instance.setSocketValue(sOut, Value.create(outputType, output));
    }

    public static class Builder<I, O> {
        private final NodeCategory category;
        private final ResourceLocation resourceLocation;
        private Datatype inputType;
        private Datatype outputType;
        private String inputSocketName;
        private String outputSocketName;
        private Function<I, O> conversionFunction;


        public Builder(NodeCategory category, String resourceLocationPath) {
            this.category = category;
            this.resourceLocation = ModConstants.resLoc(resourceLocationPath);
        }

        public Builder<I, O> types(Datatype inputType, Datatype outputType) {
            this.inputType = inputType;
            this.outputType = outputType;
            return this;
        }

        public Builder<I, O> socketNames(String inputName, String outputName) {
            this.inputSocketName = "socket.node_spell." + inputName;
            this.outputSocketName = "socket.node_spell." + outputName;
            return this;
        }
        public Builder<I, O> function(Function<I, O> conversionFunction) {
            this.conversionFunction = conversionFunction;
            return this;
        }

        public GenericConversionNode<I, O> build() {
            Objects.requireNonNull(category);
            Objects.requireNonNull(resourceLocation);
            Objects.requireNonNull(inputType);
            Objects.requireNonNull(outputType);
            Objects.requireNonNull(inputSocketName);
            Objects.requireNonNull(outputSocketName);
            Objects.requireNonNull(conversionFunction);

            return new GenericConversionNode<>(
                    category, resourceLocation,
                    inputType, outputType,
                    inputSocketName, outputSocketName,
                    conversionFunction
            );
        }
    }
}
