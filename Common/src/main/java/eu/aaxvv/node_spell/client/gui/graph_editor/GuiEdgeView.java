package eu.aaxvv.node_spell.client.gui.graph_editor;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.value.Datatype;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.joml.Vector2i;

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

    public Vector2i intersectLine(Vector2i start, Vector2i end) {
        // check bounding box for early exit
        int bbXLow = getLocalX() - STRAIGHT_EDGE_LENGTH;
        int bbXHigh = getLocalX() + getWidth() + STRAIGHT_EDGE_LENGTH;
        int bbYLow = getLocalY();
        int bbYHigh = getLocalY() + getHeight();

        if (Math.max(start.x, end.x) < bbXLow || Math.min(start.x, end.x) > bbXHigh ||
            Math.max(start.y, end.y) < bbYLow || Math.min(start.y, end.y) > bbYHigh) {
            return null;
        }

        int endX = this.cachedInX - this.getParent().getGlobalX();
        int endY = this.cachedInY - this.getParent().getGlobalY();
        int startX = this.cachedOutX - this.getParent().getGlobalX();
        int startY = this.cachedOutY - this.getParent().getGlobalY();

        Vector2i intersectMid = linesIntersect(startX + STRAIGHT_EDGE_LENGTH, startY, endX - STRAIGHT_EDGE_LENGTH, endY, start.x, start.y, end.x, end.y);
        if (intersectMid != null) {
            return intersectMid;
        }
        Vector2i intersectStart = linesIntersect(startX, startY, startX + STRAIGHT_EDGE_LENGTH, startY, start.x, start.y, end.x, end.y);
        if (intersectStart != null) {
            return intersectStart;
        }
        Vector2i intersectEnd = linesIntersect(endX, endY, endX - STRAIGHT_EDGE_LENGTH, endY, start.x, start.y, end.x, end.y);
        return intersectEnd;
    }

    private Vector2i linesIntersect(int pX1, int pY1, int pX2, int pY2, int qX1, int qY1, int qX2, int qY2) {
        float denom = (float) ((pX1 - pX2)*(qY1 - qY2) - (pY1 - pY2)*(qX1 - qX2));
        float t = ((pX1 - qX1)*(qY1 - qY2) - (pY1 - qY1)*(qX1 - qX2)) / denom;
        float u = ((pX1 - qX1)*(pY1 - pY2) - (pY1 - qY1)*(pX1 - pX2)) / denom;

       if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
           return new Vector2i((int)(pX1 + t*(pX2 - pX1)), (int)(pY1 + t*(pY2 - pY1)));
       } else {
           return null;
       }
    }
}
