package eu.aaxvv.node_spell.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

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
    public NodeCanvasWidget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.renderer = new GraphRenderer(x, y, width, height, NodeConstants.TEST_GRAPH);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        // Setup
//        Matrix4f mat = pose.last().pose();
//        BufferBuilder bb = Tesselator.getInstance().getBuilder();
//        RenderSystem.enableBlend();
//        RenderSystem.disableTexture();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.setShader(GameRenderer::getPositionColorShader);
//        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
//        // will need scissor here to cut off grid properly
//
////        RenderUtil.putQuad(mat, bb, x, y, width, height, 0, 1, 1);
//
//        BufferUploader.drawWithShader(bb.end());
//        RenderSystem.enableTexture();
//        RenderSystem.disableBlend();

        this.renderer.renderGraph(pose, mouseX, mouseY, tickDelta);
    }

    public void offsetWindowPan(int dx, int dy) {
        this.renderer.offsetWindowPan(dx, dy);
    }

    public void setWindowPan(int x, int y) {
        this.renderer.setWindowPan(x, y);
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput var1) {

    }
}
