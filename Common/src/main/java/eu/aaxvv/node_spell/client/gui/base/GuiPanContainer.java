package eu.aaxvv.node_spell.client.gui.base;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import eu.aaxvv.node_spell.client.gui.GuiElement;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import eu.aaxvv.node_spell.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class GuiPanContainer extends GuiElement {
    public static final int PAN_BUTTON = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
    private static final int GRID_SPACING = 16;
    private static final float[] BG_COLOR = ColorUtil.unpackAndCreateColor(0xFFD6BE96);
    private static final float[] GRID_COLOR = ColorUtil.unpackAndCreateColor(0xFFE7D5B4);

    private final GuiElement contentPane;

    private boolean panning;
    private Vector2d panStartMousePos;
    private Vector2i panStartContentOffset;

    public GuiPanContainer(int width, int height, GuiElement contentPane) {
        super(width, height);
        this.contentPane = contentPane;
        this.addChild(this.contentPane);
        this.panning = false;
    }

    public GuiElement getContentPane() {
        return contentPane;
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        double scale = Minecraft.getInstance().getWindow().getGuiScale();
        int framebufferHeight = Minecraft.getInstance().getWindow().getHeight();
        RenderSystem.enableScissor((int)(this.getGlobalX()*scale), framebufferHeight - ((int)(this.getGlobalY()*scale) + (int)(this.getHeight()*scale)), (int)(this.getWidth()*scale), (int)(this.getHeight()*scale));

        renderBackground(pose);

        //TODO allow for culling on children somehow?
        this.contentPane.render(pose, mouseX, mouseY, tickDelta);

        RenderSystem.disableScissor();
    }

    private void renderBackground(PoseStack pose) {
        Matrix4f mat = pose.last().pose();
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        RenderUtil.putQuad(mat, bb, this.getGlobalX(), this.getGlobalY(), this.getWidth(), this.getHeight(), BG_COLOR[1], BG_COLOR[2], BG_COLOR[3]);

        for (int x = this.getGlobalX(); x < this.getGlobalX() + this.getWidth() + GRID_SPACING; x += GRID_SPACING) {
            RenderUtil.putQuad(mat, bb, x + (this.contentPane.getLocalX() % GRID_SPACING), this.getGlobalY(), 1, this.getHeight(), GRID_COLOR[1], GRID_COLOR[2], GRID_COLOR[3]);
        }

        for (int y = this.getGlobalY(); y < this.getGlobalY() + this.getHeight() + GRID_SPACING; y += GRID_SPACING) {
            RenderUtil.putQuad(mat, bb, this.getGlobalX(), y + (this.contentPane.getLocalY() % GRID_SPACING), this.getWidth(), 1, GRID_COLOR[1], GRID_COLOR[2], GRID_COLOR[3]);
        }

        BufferUploader.drawWithShader(bb.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    @Override
    public boolean onMouseDragged(double screenX, double screenY, int buttons, double dX, double dY) {
        if (this.panning) {
            //move
            int dx = (int)(screenX - this.panStartMousePos.x);
            int dy = (int)(screenY - this.panStartMousePos.y);
            this.contentPane.setLocalPosition(this.panStartContentOffset.x + dx, this.panStartContentOffset.y + dy);
            return true;
        } else {
            return super.onMouseDragged(screenX, screenY, buttons, dX, dY);
        }
    }

    @Override
    public boolean onMouseDown(double screenX, double screenY, int button) {
        if (!this.panning && button == PAN_BUTTON) {
            startPanning(screenX, screenY);
            return true;
        }

        return super.onMouseDown(screenX, screenY, button);
    }

    @Override
    public boolean onMouseUp(double screenX, double screenY, int button) {
        if (this.panning && button == PAN_BUTTON) {
            this.panning = false;
            this.releaseFocus();
            return true;
        }

        return super.onMouseUp(screenX, screenY, button);
    }

    public void startPanning(double screenX, double screenY) {
        this.panning = true;
        this.requestFocus();
        panStartMousePos = new Vector2d(screenX, screenY);
        panStartContentOffset = new Vector2i(this.contentPane.getLocalX(), this.contentPane.getLocalY());
    }

    @Override
    public List<GuiElement> getChildren() {
        return super.getChildren();
    }

    @Override
    public GuiElement addChild(GuiElement child) {
        return super.addChild(child);
    }

    @Override
    public void removeChild(GuiElement child) {
        super.removeChild(child);
    }
}
