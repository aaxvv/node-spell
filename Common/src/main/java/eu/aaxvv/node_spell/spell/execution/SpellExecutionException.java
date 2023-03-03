package eu.aaxvv.node_spell.spell.execution;

public class SpellExecutionException extends RuntimeException {
    public SpellExecutionException(String message) {
        super(message);
    }

    public String getShortDescription() {
        if (this.getCause() != null) {
            return this.getCause().getClass().getName();
        } else {
            return this.getMessage();
        }
    }
}
