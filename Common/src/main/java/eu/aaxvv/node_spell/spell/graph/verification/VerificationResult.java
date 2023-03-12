package eu.aaxvv.node_spell.spell.graph.verification;

import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class VerificationResult {
    private final ErrorLevel errorLevel;
    @Nullable
    private final Component description;

    private final List<NodeInstance> problematicInstances;

    private VerificationResult(ErrorLevel errorLevel, Component description, List<NodeInstance> problematicInstances) {
        this.errorLevel = errorLevel;
        this.description = description;
        this.problematicInstances = problematicInstances;
    }

    private VerificationResult(ErrorLevel errorLevel) {
        this.errorLevel = errorLevel;
        this.description = null;
        this.problematicInstances = null;
    }

    public static VerificationResult error(Component description, List<NodeInstance> problematicInstances) {
        return new VerificationResult(ErrorLevel.ERROR, description, problematicInstances);
    }

    public static VerificationResult error(Component description, NodeInstance problematicInstances) {
        return new VerificationResult(ErrorLevel.ERROR, description, List.of(problematicInstances));
    }

    public static VerificationResult error(Component description) {
        return new VerificationResult(ErrorLevel.ERROR, description, Collections.emptyList());
    }

    public static VerificationResult warning(Component description, List<NodeInstance> problematicInstances) {
        return new VerificationResult(ErrorLevel.WARNING, description, problematicInstances);
    }

    public static VerificationResult warning(Component description, NodeInstance problematicInstances) {
        return new VerificationResult(ErrorLevel.WARNING, description, List.of(problematicInstances));
    }

    public static VerificationResult warning(Component description) {
        return new VerificationResult(ErrorLevel.WARNING, description, Collections.emptyList());
    }

    public static VerificationResult ok() {
        return new VerificationResult(ErrorLevel.OK, null, Collections.emptyList());
    }

    public boolean isError() {
        return this.errorLevel == ErrorLevel.ERROR;
    }

    public boolean isWarning() {
        return this.errorLevel == ErrorLevel.WARNING;
    }

    public boolean isOk() {
        return this.errorLevel == ErrorLevel.OK;
    }

    public ErrorLevel getErrorLevel() {
        return errorLevel;
    }

    public @Nullable Component getDescription() {
        return description;
    }

    public List<NodeInstance> getProblematicInstances() {
        return problematicInstances;
    }

}
