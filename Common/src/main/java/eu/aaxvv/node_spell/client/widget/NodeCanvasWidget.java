package eu.aaxvv.node_spell.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2i;

import java.util.Optional;

/**
 * TODO: look at for inspiration in rendering
 * {@link  net.minecraft.client.gui.screens.advancements.AdvancementsScreen#render}
 */
public class NodeCanvasWidget implements Renderable, GuiEventListener, NarratableEntry {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final GraphRenderer renderer;
    private int prevWindowPanX;
    private int prevWindowPanY;
    private SpellGraph graph;
    public NodeCanvasWidget(int x, int y, int width, int height, SpellGraph graph) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.graph = graph;
        this.renderer = new GraphRenderer(x, y, width, height, graph);
        this.prevWindowPanX = 0;
        this.prevWindowPanY = 0;
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        this.renderer.renderGraph(pose, mouseX, mouseY, tickDelta);
    }

    public void startWindowPan() {
        this.prevWindowPanX = this.renderer.getWindowPanX();
        this.prevWindowPanY = this.renderer.getWindowPanY();
    }

    public void setWindowPanOffset(int dx, int dy) {
        this.renderer.setWindowPan(this.prevWindowPanX + dx, this.prevWindowPanY + dy);
    }

    public Optional<Object> getObjectAtLocation(int x, int y) {
        int localX = x - this.x - this.renderer.getWindowPanX();
        int localY = y - this.y - this.renderer.getWindowPanY();

        // first check sockets, then nodes
        for (NodeInstance instance : this.graph.getNodeInstances()) {
            for (SocketInstance socket : instance.getSocketInstances()) {
                if (socket.containsPoint(localX, localY)) {
                    return Optional.of(socket);
                }
            }

            if (instance.containsPoint(localX, localY)) {
                return Optional.of(instance);
            }
        }

        return Optional.empty();
    }

    public Vector2i getNodePositionGlobal(NodeInstance nodeInstance) {
        int globalX = nodeInstance.getX() + this.x + this.renderer.getWindowPanX();
        int globalY = nodeInstance.getY() + this.y + this.renderer.getWindowPanY();

        return new Vector2i(globalX, globalY);
    }

    public void setNodePositionLocal(NodeInstance nodeInstance, int x, int y) {
        int localX = x - this.x - this.renderer.getWindowPanX();
        int localY = y - this.y - this.renderer.getWindowPanY();

        nodeInstance.setPosition(localX, localY);
    }

    public boolean containsPoint(int x, int y) {
        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput var1) {

    }

}
