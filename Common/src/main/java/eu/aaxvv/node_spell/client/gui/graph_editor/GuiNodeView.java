package eu.aaxvv.node_spell.client.gui.graph_editor;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import eu.aaxvv.node_spell.client.widget.NodeConstants;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiConsumer;

public class GuiNodeView extends GuiElement {
    private final NodeGraphView graph;
    private final NodeInstance instance;
    private boolean selected;
    private Vector2i dragOffset;
    private BiConsumer<Double, Double> clickedCallback;

    public GuiNodeView(NodeInstance instance, NodeGraphView graph) {
        super(instance.getBaseNode().getWidth(), instance.getBaseNode().getExpectedHeight());
        this.graph = graph;
        this.instance = instance;
        this.selected = false;
        this.invalidate();
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        Matrix4f mat = pose.last().pose();
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        int nodeWidth = instance.getBaseNode().getWidth();
        int nodeHeight = instance.getBaseNode().getExpectedHeight();
        if (this.selected) {
            RenderUtil.putQuad(mat, bb, getGlobalX(), getGlobalY(), nodeWidth, nodeHeight, 0.5f, 0.5f, 0.5f);
        } else {
            RenderUtil.putQuad(mat, bb, getGlobalX(), getGlobalY(), nodeWidth, nodeHeight, 0, 0, 0);
        }
        NodeCategory category = instance.getBaseNode().getCategory();
        RenderUtil.putQuad(mat, bb, getGlobalX() + 1, getGlobalY() + 1, nodeWidth - 2, NodeConstants.HEADER_HEIGHT, category.r, category.g, category.b);
        RenderUtil.putQuad(mat, bb, getGlobalX() + 1, getGlobalY() + NodeConstants.HEADER_HEIGHT + 1, nodeWidth - 2, nodeHeight - NodeConstants.HEADER_HEIGHT - 2, 0.9f, 0.9f, 0.9f);

        for (SocketInstance socketInstance : instance.getSocketInstances()) {
            Datatype dt = socketInstance.getBase().getDataType();
            int socketX = this.getGlobalX() + socketInstance.getLocalX();
            int socketY = this.getGlobalY() + socketInstance.getLocalY();

            if (dt != Datatype.FLOW) {
                RenderUtil.putQuad(mat, bb, socketX + 1, socketY, 3, 5, dt.r, dt.g, dt.b);
                RenderUtil.putQuad(mat, bb, socketX, socketY + 1, 5, 3, dt.r, dt.g, dt.b);
                if (socketInstance.getConnections().isEmpty() && socketInstance.getBase().getDirection() == Socket.Direction.IN) {
                    RenderUtil.putQuad(mat, bb, socketX + 2, socketY + 2, 1, 1, 0, 0, 0);
                }
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

        //TODO: widgets should be proper children of nodes
//        if (instance.getWidget() != null) {
//            Widget<?> widget = instance.getWidget();
//            widget.draw(pose, x + widget.getLocalX(), y + widget.getLocalY());
//        }

        super.render(pose, mouseX, mouseY, tickDelta);
    }

    private void renderNodeText(PoseStack pose, NodeInstance instance) {
        Font font = Minecraft.getInstance().font;

        font.draw(pose, Component.translatable(instance.getBaseNode().getTranslationKey()), this.getGlobalX() + 2, this.getGlobalY() + 2, NodeConstants.TITLE_TEXT_COLOR);

        for (SocketInstance socketInstance : instance.getSocketInstances()) {
            Component name = Component.translatable(socketInstance.getBase().getTranslationKey());
            int socketX = this.getGlobalX() + socketInstance.getLocalX();
            int socketY = this.getGlobalY() + socketInstance.getLocalY();

            if (socketInstance.getBase().getDirection() == Socket.Direction.OUT) {
                int length = font.width(name);
                font.draw(pose, name, socketX - length - 2, socketY - 1, NodeConstants.SOCKET_TEXT_COLOR);
            } else {
                font.draw(pose, name, socketX + 7, socketY - 1, NodeConstants.SOCKET_TEXT_COLOR);
            }
        }
    }

    @Override
    public boolean onMouseDown(double screenX, double screenY, int button) {
        if(super.onMouseDown(screenX, screenY, button)) {
            return true;
        }

        if (this.clickedCallback != null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            this.clickedCallback.accept(screenX, screenY);
            return true;
        }

        return false;
    }

    public void setClickedCallback(BiConsumer<Double, Double> clickedCallback) {
        this.clickedCallback = clickedCallback;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setDragOffset(Vector2i dragOffset) {
        this.dragOffset = dragOffset;
    }

    public Vector2i getDragOffset() {
        return dragOffset;
    }

    public NodeInstance getInstance() {
        return instance;
    }

    @Override
    public int getLocalX() {
        return instance.getX();
    }

    @Override
    public int getLocalY() {
        return instance.getY();
    }

    @Override
    public void setLocalPosition(int x, int y) {
        this.instance.setPosition(x, y);
        invalidate();
    }

    @Override
    public void setWidth(int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHeight(int height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSize(int width, int height) {
        throw new UnsupportedOperationException();
    }
}
