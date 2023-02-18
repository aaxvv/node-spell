package eu.aaxvv.node_spell.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
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
        Matrix4f mat = pose.last().pose();
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        // will need scissor here to cut off grid properly

        putQuad(mat, bb, x, y, width, height, 0, 1, 1);

        BufferUploader.drawWithShader(bb.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        this.renderer.renderGraph(pose, mouseX, mouseY, tickDelta);
    }

//    private void renderNode(Matrix4f mat, BufferBuilder bb, NodeInstance instance) {
//        int x = instance.getX() + this.x;
//        int y = instance.getY() + this.y;
//
//        int nW = 64;
//        int nH = instance.getBaseNode().getExpectedHeight();
//        int headerHeight = 10;
//        putQuad(mat, bb, x, y, nW, nH, 0, 0, 0);
//        putQuad(mat, bb, x + 1, y + 1, nW - 2, headerHeight, 0.285f, 0.659f, 0.310f);
//        putQuad(mat, bb, x + 1, y + headerHeight + 1, nW - 2, nH - headerHeight - 2, 0.9f, 0.9f, 0.9f);
//
//        int inputX = x - 2;
//        int outputX = x + nW - 3;
//        int outputY = y + 15;
//        int inputY = y + 15;
//
//        for (Socket socket : instance.getBaseNode().getSockets()) {
//            Datatype dt = socket.getDataType();
//            if (socket.getDirection() == Socket.Direction.IN) {
//                putQuad(mat, bb, inputX + 1, inputY, 3, 5, dt.r, dt.g, dt.b);
//                putQuad(mat, bb, inputX, inputY + 1, 5, 3, dt.r, dt.g, dt.b);
//                inputY += 12;
//            } else {
//                putQuad(mat, bb, outputX + 1, outputY, 3, 5, dt.r, dt.g, dt.b);
//                putQuad(mat, bb, outputX, outputY + 1, 5, 3, dt.r, dt.g, dt.b);
//                outputY += 12;
//            }
//        }
//    }
//
//    private void renderNodeText(PoseStack pose, NodeInstance instance) {
//        int x = instance.getX() + this.x;
//        int y = instance.getY() + this.y;
//
//        Minecraft.getInstance().font.draw(pose, instance.getBaseNode().getName(), x + 2, y + 2, NodeConstants.TITLE_TEXT_COLOR);
//
//        // TODO socket name
//    }
//
    private void putQuad(Matrix4f mat, BufferBuilder bb, int x, int y, int w, int h, float r, float g, float b) {
        bb.vertex(mat, (float)x, (float)y, 0.0F).color(r, g, b, 1).endVertex();
        bb.vertex(mat, (float)x, (float)y+h, 0.0F).color(r, g, b, 1).endVertex();
        bb.vertex(mat, (float)x+w, (float)y+h, 0.0F).color(r, g, b, 1).endVertex();
        bb.vertex(mat, (float)x+w, (float)y, 0.0F).color(r, g, b, 1).endVertex();
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput var1) {

    }
}
