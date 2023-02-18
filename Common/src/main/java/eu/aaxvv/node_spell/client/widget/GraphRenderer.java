package eu.aaxvv.node_spell.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public class GraphRenderer {
    private final SpellGraph graph;
    private final int windowWidth;
    private final int windowHeight;
    private final int x;
    private final int y;

    private int windowPanX;
    private int windowPanY;

    public GraphRenderer(int x, int y, int windowWidth, int windowHeight, SpellGraph graph) {
        this.graph = graph;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.x = x;
        this.y = y;
        this.windowPanX = 0;
        this.windowPanY = 0;
    }

    public void offsetWindowPan(int dx, int dy) {
        this.windowPanX += dx;
        this.windowPanY += dy;
    }

    public void setWindowPan(int x, int y) {
        this.windowPanX = x;
        this.windowPanY = y;
    }

    public void renderGraph(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        // Setup
        Matrix4f mat = pose.last().pose();
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        double scale = Minecraft.getInstance().getWindow().getGuiScale();
        RenderSystem.enableScissor((int)(x*scale), (int)(y*scale), (int)(windowWidth*scale), (int)(windowHeight*scale));
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

//        putQuad(mat, bb, x, y, windowWidth, windowHeight, 0, 1, 1);

        // Nodes
        for (NodeInstance instance : graph.getNodeInstances()) {
            renderNode(mat, bb, instance);
        }

        // Edges
        for (Edge instance : graph.getEdges()) {

        }

        BufferUploader.drawWithShader(bb.end());
        RenderSystem.disableScissor();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        // Node Text
        for (NodeInstance instance : graph.getNodeInstances()) {
            renderNodeText(pose, instance);
        }
    }

    public void renderSingleNode(PoseStack pose, NodeInstance instance) {
        // Setup
        Matrix4f mat = pose.last().pose();
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        renderNode(mat, bb, instance);

        BufferUploader.drawWithShader(bb.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        // Node Text
        renderNodeText(pose, instance);
    }

    private void renderNode(Matrix4f mat, BufferBuilder bb, NodeInstance instance) {
        if (instance.isDragged()) {
            // has special handling in NodeCanvasWidget
            return;
        }

        int x = instance.getX() + this.x + this.windowPanX;
        int y = instance.getY() + this.y + this.windowPanY;

        int nodeWidth = NodeConstants.DEFAULT_NODE_WIDTH;
        int nodeHeight = instance.getBaseNode().getExpectedHeight();
        putQuad(mat, bb, x, y, nodeWidth, nodeHeight, 0, 0, 0);
        putQuad(mat, bb, x + 1, y + 1, nodeWidth - 2, NodeConstants.HEADER_HEIGHT, 0.285f, 0.659f, 0.310f);
        putQuad(mat, bb, x + 1, y + NodeConstants.HEADER_HEIGHT + 1, nodeWidth - 2, nodeHeight - NodeConstants.HEADER_HEIGHT - 2, 0.9f, 0.9f, 0.9f);

//        int inputX = x - 2;
//        int outputX = x + nodeWidth - 3;
//        int outputY = y + 15;
//        int inputY = y + 15;

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

        for (SocketInstance socketInstance : instance.getSocketInstances()) {
            Datatype dt = socketInstance.getBase().getDataType();
            int socketX = socketInstance.getX() + this.x + this.windowPanX;
            int socketY = socketInstance.getY() + this.y + this.windowPanY;

            putQuad(mat, bb, socketX + 1, socketY, 3, 5, dt.r, dt.g, dt.b);
            putQuad(mat, bb, socketX, socketY + 1, 5, 3, dt.r, dt.g, dt.b);
        }
    }

    private void renderNodeText(PoseStack pose, NodeInstance instance) {
        int x = instance.getX() + this.x;
        int y = instance.getY() + this.y;

        Minecraft.getInstance().font.draw(pose, instance.getBaseNode().getName(), x + 2, y + 2, 0xFF000000);

        // TODO socket name
    }

    private void putQuad(Matrix4f mat, BufferBuilder bb, int x, int y, int w, int h, float r, float g, float b) {
        bb.vertex(mat, (float)x, (float)y, 0.0F).color(r, g, b, 1).endVertex();
        bb.vertex(mat, (float)x, (float)y+h, 0.0F).color(r, g, b, 1).endVertex();
        bb.vertex(mat, (float)x+w, (float)y+h, 0.0F).color(r, g, b, 1).endVertex();
        bb.vertex(mat, (float)x+w, (float)y, 0.0F).color(r, g, b, 1).endVertex();
    }
}
