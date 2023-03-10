package eu.aaxvv.node_spell.client.gui.graph_editor;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public class GuiEdgeView extends GuiElement {
    private static final int STRAIGHT_EDGE_LENGTH = 8;

    private final Edge instance;

    protected int cachedOutX;
    protected int cachedOutY;
    protected int cachedInX;
    protected int cachedInY;

    public GuiEdgeView(Edge instance) {
        super(0, 0);
        this.instance = instance;
    }

    // old rendering with only axis aligned lines
//    @Override
//    public void renderOld(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
//        Matrix4f mat = pose.last().pose();
//        BufferBuilder bb = Tesselator.getInstance().getBuilder();
//        RenderSystem.enableBlend();
//        RenderSystem.disableTexture();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.setShader(GameRenderer::getPositionColorShader);
//        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
//
//        int leftX = this.getGlobalX() + 1;
//        int width = this.getWidth();
//
//        int leftY = this.getLeftY() + this.getParent().getGlobalY();
//        int rightY = this.getRightY() + this.getParent().getGlobalY();
//
//        int middleX = leftX + (int) Math.ceil(width / 2.0f);
//        Datatype dt = this.getDatatype();
//
//        RenderUtil.putQuad(mat, bb, leftX, leftY, width / 2 + 1, 1, dt.r, dt.g, dt.b);
//        RenderUtil.putQuad(mat, bb, middleX, this.getGlobalY(), 1, this.getHeight() + 1, dt.r, dt.g, dt.b);
//        RenderUtil.putQuad(mat, bb, middleX, rightY, width / 2, 1, dt.r, dt.g, dt.b);
//
//        BufferUploader.drawWithShader(bb.end());
//        RenderSystem.enableTexture();
//        RenderSystem.disableBlend();
//
//        super.render(pose, mouseX, mouseY, tickDelta);
//    }


    @Override
    public void invalidate() {
        super.invalidate();
        invalidateEndpoints();
    }

    protected void invalidateEndpoints() {
        if (getParent() != null) {
            if (this.getInstance().getStart().getBase().getDirection().isOut()) {
                cachedOutX = this.getInstance().getStart().getX();
                cachedOutY = this.getInstance().getStart().getY();
                cachedInX = this.getInstance().getEnd().getX();
                cachedInY = this.getInstance().getEnd().getY();
            } else {
                cachedInX = this.getInstance().getStart().getX();
                cachedInY = this.getInstance().getStart().getY();
                cachedOutX = this.getInstance().getEnd().getX();
                cachedOutY = this.getInstance().getEnd().getY();
            }

            cachedOutX += this.getParent().getGlobalX();
            cachedOutY += this.getParent().getGlobalY();
            cachedInX += this.getParent().getGlobalX();
            cachedInY += this.getParent().getGlobalY();
        }
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
        Datatype dt = this.getDatatype();


        // start
        bb.vertex(mat, (float)cachedOutX, (float)cachedOutY, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
        bb.vertex(mat, (float)cachedOutX, (float)cachedOutY + 1, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
        bb.vertex(mat, (float)cachedOutX + STRAIGHT_EDGE_LENGTH, (float)cachedOutY + 1, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
        bb.vertex(mat, (float)cachedOutX + STRAIGHT_EDGE_LENGTH, (float)cachedOutY, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();

        // middle
        double angle = Math.atan2(cachedInY - cachedOutY, cachedInX - cachedOutX - (2*STRAIGHT_EDGE_LENGTH));
        float xOffset = (float)Math.sin(angle);
        float yOffset = (float)Math.cos(angle);

        if (cachedOutX + STRAIGHT_EDGE_LENGTH > cachedInX - STRAIGHT_EDGE_LENGTH) {
            // flip
            if (cachedOutY <= cachedInY) {
                // up
                bb.vertex(mat, (float)cachedOutX + STRAIGHT_EDGE_LENGTH, (float)cachedOutY + 1, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
                bb.vertex(mat, (float)cachedOutX + STRAIGHT_EDGE_LENGTH - xOffset, (float)cachedOutY + 1 + yOffset, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
                bb.vertex(mat, (float)cachedInX - STRAIGHT_EDGE_LENGTH, (float)cachedInY, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
                bb.vertex(mat, (float)cachedInX - STRAIGHT_EDGE_LENGTH + xOffset, (float)cachedInY - yOffset, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
            } else {
                // down
                bb.vertex(mat, (float)cachedOutX + STRAIGHT_EDGE_LENGTH + xOffset, (float)cachedOutY - yOffset, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
                bb.vertex(mat, (float)cachedOutX + STRAIGHT_EDGE_LENGTH, (float)cachedOutY, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
                bb.vertex(mat, (float)cachedInX - STRAIGHT_EDGE_LENGTH - xOffset, (float)cachedInY + 1 + yOffset, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
                bb.vertex(mat, (float)cachedInX - STRAIGHT_EDGE_LENGTH, (float)cachedInY + 1, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
            }

        } else {
            // normal
            if (cachedOutY <= cachedInY) {
                // down
                bb.vertex(mat, (float)cachedOutX + STRAIGHT_EDGE_LENGTH, (float)cachedOutY, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
                bb.vertex(mat, (float)cachedOutX + STRAIGHT_EDGE_LENGTH - xOffset, (float)cachedOutY + yOffset, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
                bb.vertex(mat, (float)cachedInX - STRAIGHT_EDGE_LENGTH, (float)cachedInY + yOffset, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
                bb.vertex(mat, (float)cachedInX - STRAIGHT_EDGE_LENGTH + xOffset, (float)cachedInY, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
            } else {
                // up
                bb.vertex(mat, (float)cachedOutX + STRAIGHT_EDGE_LENGTH + xOffset, (float)cachedOutY, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
                bb.vertex(mat, (float)cachedOutX + STRAIGHT_EDGE_LENGTH, (float)cachedOutY + 1, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
                bb.vertex(mat, (float)cachedInX - STRAIGHT_EDGE_LENGTH - xOffset, (float)cachedInY + yOffset, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
                bb.vertex(mat, (float)cachedInX - STRAIGHT_EDGE_LENGTH, (float)cachedInY, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
            }
        }

        // end
        bb.vertex(mat, (float)cachedInX, (float)cachedInY, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
        bb.vertex(mat, (float)cachedInX - STRAIGHT_EDGE_LENGTH, (float)cachedInY, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
        bb.vertex(mat, (float)cachedInX - STRAIGHT_EDGE_LENGTH, (float)cachedInY + 1, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();
        bb.vertex(mat, (float)cachedInX, (float)cachedInY + 1, 0.0F).color(dt.r, dt.g, dt.b, 1).endVertex();

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
        return Math.min(instance.getStart().getX(), instance.getEnd().getX());
    }

    @Override
    public int getLocalY() {
        return Math.min(instance.getStart().getY(), instance.getEnd().getY());
    }

    @Override
    public int getWidth() {
        return Math.abs(instance.getStart().getX() - instance.getEnd().getX());
    }

    @Override
    public int getHeight() {
        return Math.abs(instance.getStart().getY() - instance.getEnd().getY());
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
