package eu.aaxvv.node_spell.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import eu.aaxvv.node_spell.client.node_widget.Widget;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import org.joml.Vector2i;

public class GraphRenderer {
    private final SpellGraph graph;
    private final int windowWidth;
    private final int windowHeight;
    private final int x;
    private final int y;

    private int windowPanX;
    private int windowPanY;

    private final float[] gridColor;
    private final NodeCanvasWidget parent;

    public GraphRenderer(NodeCanvasWidget parent, int x, int y, int windowWidth, int windowHeight, SpellGraph graph) {
        this.parent = parent;
        this.graph = graph;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.x = x;
        this.y = y;
        this.windowPanX = 0;
        this.windowPanY = 0;

        this.gridColor = new float[4];
        ColorUtil.unpackColor(NodeConstants.NODE_GRAPH_GRID_COLOR, gridColor);
    }

    public void setWindowPan(int x, int y) {
        this.windowPanX = x;
        this.windowPanY = y;
    }

    public int getWindowPanX() {
        return windowPanX;
    }

    public int getWindowPanY() {
        return windowPanY;
    }

    public void renderGraph(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        // Setup
        double scale = Minecraft.getInstance().getWindow().getGuiScale();
        // it took me a while to realize that GlScissor uses a bottom left origin, not top left
        int framebufferHeight = Minecraft.getInstance().getWindow().getHeight();
        RenderSystem.enableScissor((int)(x*scale), framebufferHeight - ((int)(y*scale) + (int)(windowHeight*scale)), (int)(windowWidth*scale), (int)(windowHeight*scale));

        renderGrid(pose);

        renderEdges(pose);

        renderNodes(pose);

        RenderSystem.disableScissor();
    }

    private void renderEdges(PoseStack pose) {
        Matrix4f mat = pose.last().pose();
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        for (Edge instance : graph.getEdges()) {
            renderEdge(mat, bb, instance);
        }

        if (this.parent.getDraggedEdge() != null) {
            renderEdge(mat, bb, this.parent.getDraggedEdge());
        }

        BufferUploader.drawWithShader(bb.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private void renderGrid(PoseStack pose) {
        Matrix4f mat = pose.last().pose();
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        int spacing = NodeConstants.NODE_GRAPH_GRID_SPACING;

        for (int x = this.x; x < this.x + this.windowWidth + spacing; x += spacing) {
            RenderUtil.putQuad(mat, bb, x + (this.windowPanX % spacing), this.y, 1, this.windowHeight, gridColor[1], gridColor[2], gridColor[3]);
        }

        for (int y = this.y; y < this.y + this.windowHeight + spacing; y += spacing) {
            RenderUtil.putQuad(mat, bb, this.x, y + (this.windowPanY % spacing), this.windowWidth, 1, gridColor[1], gridColor[2], gridColor[3]);
        }

        BufferUploader.drawWithShader(bb.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private void renderNodes(PoseStack pose) {
        // Nodes
        for (NodeInstance instance : graph.getNodeInstances()) {
            renderNode(pose, instance);
        }
    }

    private void renderNode(PoseStack pose, NodeInstance instance) {
        Matrix4f mat = pose.last().pose();
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        int x = instance.getX() + this.x + this.windowPanX;
        int y = instance.getY() + this.y + this.windowPanY;

        int nodeWidth = instance.getBaseNode().getWidth();
        int nodeHeight = instance.getBaseNode().getExpectedHeight();
        RenderUtil.putQuad(mat, bb, x, y, nodeWidth, nodeHeight, 0, 0, 0);
        NodeCategory category = instance.getBaseNode().getCategory();
        RenderUtil.putQuad(mat, bb, x + 1, y + 1, nodeWidth - 2, NodeConstants.HEADER_HEIGHT, category.r, category.g, category.b);
        RenderUtil.putQuad(mat, bb, x + 1, y + NodeConstants.HEADER_HEIGHT + 1, nodeWidth - 2, nodeHeight - NodeConstants.HEADER_HEIGHT - 2, 0.9f, 0.9f, 0.9f);

        for (SocketInstance socketInstance : instance.getSocketInstances()) {
            Datatype dt = socketInstance.getBase().getDataType();
            int socketX = socketInstance.getX() + this.x + this.windowPanX;
            int socketY = socketInstance.getY() + this.y + this.windowPanY;

            if (dt != Datatype.FLOW) {
                RenderUtil.putQuad(mat, bb, socketX + 1, socketY, 3, 5, dt.r, dt.g, dt.b);
                RenderUtil.putQuad(mat, bb, socketX, socketY + 1, 5, 3, dt.r, dt.g, dt.b);
            } else {
                RenderUtil.putQuad(mat, bb, socketX, socketY, 4, 1, dt.r, dt.g, dt.b);
                RenderUtil.putQuad(mat, bb, socketX + 1, socketY + 1, 4, 3, dt.r, dt.g, dt.b);
                RenderUtil.putQuad(mat, bb, socketX, socketY + 4, 4, 1, dt.r, dt.g, dt.b);
            }
        }

        BufferUploader.drawWithShader(bb.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        renderNodeText(pose, instance);

        if (instance.getWidget() != null) {
            Widget<?> widget = instance.getWidget();
            widget.draw(pose, x + widget.getLocalX(), y + widget.getLocalY());
        }
    }

    private void renderEdge(Matrix4f mat, BufferBuilder bb, Edge edge) {
        if (this.parent.getDraggedEdge() == edge) {
            Vector2i dragPos = this.parent.getDragPos();
            if (this.parent.isDraggingStart()) {
                renderEdgeRaw(mat, bb, dragPos.x, dragPos.y, edge.getEnd().getX() + 2, edge.getEnd().getY() + 2, edge.getDatatype());
            } else {
                renderEdgeRaw(mat, bb, edge.getStart().getX() + 2, edge.getStart().getY() + 2, dragPos.x, dragPos.y, edge.getDatatype());
            }
        } else {
            renderEdgeRaw(mat, bb, edge.getStart().getX() + 2, edge.getStart().getY() + 2, edge.getEnd().getX() + 2, edge.getEnd().getY() + 2, edge.getDatatype());
        }
    }

    private void renderEdgeRaw(Matrix4f mat, BufferBuilder bb, int xStart, int yStart, int xEnd, int yEnd, Datatype dt) {
        int x1 = xStart + this.x + this.windowPanX;
        int y1 = yStart + this.y + this.windowPanY;
        int x2 = xEnd + this.x + this.windowPanX;
        int y2 = yEnd + this.y + this.windowPanY;

        int leftX, leftY;
        int rightX, rightY;

        if (x1 < x2) {
            leftX = x1;
            leftY = y1;
            rightX = x2;
            rightY = y2;
        } else {
            leftX = x2;
            leftY = y2;
            rightX = x1;
            rightY = y1;
        }

        int middleX = leftX + (int) Math.ceil((rightX - leftX) / 2.0f);
        int topY = Math.min(leftY, rightY);
        int height = Math.abs(rightY - leftY);

        RenderUtil.putQuad(mat, bb, leftX, leftY, middleX - leftX + 1, 1, dt.r, dt.g, dt.b);
        RenderUtil.putQuad(mat, bb, middleX, topY, 1, height, dt.r, dt.g, dt.b);
        RenderUtil.putQuad(mat, bb, middleX, rightY, rightX - middleX + 1, 1, dt.r, dt.g, dt.b);
    }

    private void renderNodeText(PoseStack pose, NodeInstance instance) {
        Font font = Minecraft.getInstance().font;
        int x = instance.getX() + this.x + this.windowPanX;
        int y = instance.getY() + this.y + this.windowPanY;

        font.draw(pose, Component.translatable(instance.getBaseNode().getTranslationKey()), x + 2, y + 2, NodeConstants.TITLE_TEXT_COLOR);

        for (SocketInstance socketInstance : instance.getSocketInstances()) {
            Component name = Component.translatable(socketInstance.getBase().getTranslationKey());
            int socketX = socketInstance.getX() + this.x + this.windowPanX;
            int socketY = socketInstance.getY() + this.y + this.windowPanY;

            if (socketInstance.getBase().getDirection() == Socket.Direction.OUT) {
                int length = font.width(name);
                font.draw(pose, name, socketX - length - 2, socketY - 1, NodeConstants.SOCKET_TEXT_COLOR);
            } else {
                font.draw(pose, name, socketX + 7, socketY - 1, NodeConstants.SOCKET_TEXT_COLOR);
            }
        }
    }

//    public void renderSingleNode(PoseStack pose, NodeInstance instance) {
//        // Setup
//        Matrix4f mat = pose.last().pose();
//        BufferBuilder bb = Tesselator.getInstance().getBuilder();
//        RenderSystem.enableBlend();
//        RenderSystem.disableTexture();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.setShader(GameRenderer::getPositionColorShader);
//        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
//
//        renderNode(mat, bb, instance);
//
//        BufferUploader.drawWithShader(bb.end());
//        RenderSystem.enableTexture();
//        RenderSystem.disableBlend();
//
//        // Node Text
//        renderNodeText(pose, instance);
//    }
}
