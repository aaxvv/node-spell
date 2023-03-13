package eu.aaxvv.node_spell.spell.graph.structure;

import eu.aaxvv.node_spell.ModConstants;

public record NodeStyle(int backgroundColor, boolean drawHeader) {
    public static NodeStyle getDefault() {
        return new NodeStyle(ModConstants.Colors.WHITE, true);
    }

    public int getSocketYOffset() {
        int socketStart = this.drawHeader ? ModConstants.Sizing.SOCKET_START_Y : 3;
        return 1 + (this.drawHeader ? ModConstants.Sizing.HEADER_HEIGHT : 0) + socketStart;
    }
}
