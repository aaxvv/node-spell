package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;

public class SpellExecutionException extends RuntimeException {
    private NodeInstance nodeContext = null;

    public SpellExecutionException(String message) {
        super(message);
    }

    public SpellExecutionException(Throwable cause) {
        super(cause);
    }

    public String getShortDescription() {
        if (this.getCause() != null) {
            return "Unhandled Exception during spell execution. <" + this.getCause().getClass().getName() + ">";
        } else {
            if (this.nodeContext != null) {
                return this.getMessage() + " [" + this.nodeContext.getBaseNode().getResourceLocation() + "]";
            } else {
                return this.getMessage();
            }
        }
    }

    public void addNodeContext(NodeInstance nodeContext) {
        if (this.nodeContext == null) {
            this.nodeContext = nodeContext;
        }
    }
}
