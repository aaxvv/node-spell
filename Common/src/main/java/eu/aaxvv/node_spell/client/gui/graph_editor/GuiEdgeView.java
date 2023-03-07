package eu.aaxvv.node_spell.client.gui.graph_editor;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public class GuiEdgeView extends GuiElement {
    private final Edge instance;

    public GuiEdgeView(Edge instance) {
        super(0, 0);
        this.instance = instance;
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

        int leftX = this.getGlobalX() + 1;
        int width = this.getWidth();

        int leftY = this.getLeftY() + this.getParent().getGlobalY();
        int rightY = this.getRightY() + this.getParent().getGlobalY();

        int middleX = leftX + (int) Math.ceil(width / 2.0f);
        Datatype dt = this.getDatatype();

        RenderUtil.putQuad(mat, bb, leftX, leftY, width / 2 + 1, 1, dt.r, dt.g, dt.b);
        RenderUtil.putQuad(mat, bb, middleX, this.getGlobalY(), 1, this.getHeight() + 1, dt.r, dt.g, dt.b);
        RenderUtil.putQuad(mat, bb, middleX, rightY, width / 2, 1, dt.r, dt.g, dt.b);

        BufferUploader.drawWithShader(bb.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        super.render(pose, mouseX, mouseY, tickDelta);
    }

    public Edge getInstance() {
        return instance;
    }

    protected Datatype getDatatype() {
        return this.instance.getDatatype();
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
    public int getWidth() {
        return instance.getWidth();
    }

    @Override
    public int getHeight() {
        return instance.getHeight();
    }

    protected int getLeftY() {
        return this.instance.getLeftY();
    }
    protected int getRightY() {
        return this.instance.getRightY();
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
