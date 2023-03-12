package eu.aaxvv.node_spell.spell.graph.verification;

import eu.aaxvv.node_spell.ModConstants;

public enum ErrorLevel {
    OK(0),
    WARNING(ModConstants.Colors.YELLOW),
    ERROR(ModConstants.Colors.RED);

    ErrorLevel(int highlightColor) {
        this.highlightColor = highlightColor;
    }

    public final int highlightColor;
}
