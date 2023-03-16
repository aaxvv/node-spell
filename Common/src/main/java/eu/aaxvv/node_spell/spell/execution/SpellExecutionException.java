package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class SpellExecutionException extends RuntimeException {
    private NodeInstance nodeContext = null;
    private MutableComponent message = null;

    public SpellExecutionException(MutableComponent message) {
        this.message = message;
    }

    public SpellExecutionException(Throwable cause) {
        super(cause);
    }

    public MutableComponent getShortDescription() {
        if (this.getCause() != null) {
            return Component.translatable("error.node_spell.unhandled_exception", this.getCause().getClass().getName());
        } else {
            if (this.nodeContext != null) {
                return this.message.append(Component.literal(" [" + Component.translatable(this.nodeContext.getBaseNode().getTranslationKey()).getString() + "]"));
            } else {
                return this.message;
            }
        }
    }

    public void addNodeContext(NodeInstance nodeContext) {
        if (this.nodeContext == null) {
            this.nodeContext = nodeContext;
        }
    }
}
