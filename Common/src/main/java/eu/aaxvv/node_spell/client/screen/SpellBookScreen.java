package eu.aaxvv.node_spell.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.widget.NodeCanvasWidget;
import eu.aaxvv.node_spell.client.widget.NodeConstants;
import eu.aaxvv.node_spell.client.widget.NodePickerWidget;
import eu.aaxvv.node_spell.spell.Spell;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

/**
 * Look at BookEditScreen for inspiration
 */
public class SpellBookScreen extends Screen {
    private static final ResourceLocation BACKGROUND_LOCATION = ModConstants.resLoc("textures/gui/spell_book_bg.png");
    // normal inventory width. can be extended if needed (e.g. beacon ui is 230)
    private final int mainAreaWidth = 288;
    // height of double chest screen
    private final int mainAreaHeight = 208;
    private int x;
    private int y;

    private final Spell spell;
    private NodeCanvasWidget canvas;
    private NodePickerWidget picker;
    private final InteractionHandler dragHandler;

    public SpellBookScreen(Component title) {
        super(title);
        this.dragHandler = new InteractionHandler();
        this.spell = NodeConstants.TEST_SPELL;
    }

    @Override
    protected void init() {
        //TODO the canvas may also need to contain the node list in order to make dragging from canvas to list possible?
        this.x = (this.width / 2) - (this.mainAreaWidth / 2);
        this.y = (this.height / 2) - (this.mainAreaHeight / 2);
        this.picker = new NodePickerWidget(this, x, y + mainAreaHeight, mainAreaWidth);
        this.canvas = new NodeCanvasWidget(x, y, mainAreaWidth, mainAreaHeight - this.picker.getHeight(), this.spell.getGraph());

        addRenderableWidget(this.canvas);
        addRenderableWidget(this.picker);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        this.fillGradient(pose, 0, 0, this.width, this.height, -1072689136, -804253680);

//        int x = (this.width / 2) - (this.mainAreaWidth / 2);
//        int y = (this.height / 2) - (this.mainAreaHeight / 2);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
        GuiComponent.blit(pose, x - 8, y - 8, 0, 0, 304, 224, 512, 256);
        RenderSystem.disableBlend();

        super.render(pose, mouseX, mouseY, tickDelta);

        // draw delete icon
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
        GuiComponent.blit(pose, x + this.mainAreaWidth - 16, y, 0, 224, 16, 16, 512, 256);
        RenderSystem.disableBlend();

        if (mouseX >= x + this.mainAreaWidth - 16 && mouseX < x + this.mainAreaWidth && mouseY >= y && mouseY < y + 16) {
            renderTooltip(pose, Component.literal("Drag Node here to delete"), mouseX, mouseY);
        }
    }

    @Override
    public void mouseMoved(double x, double y) {
        this.dragHandler.mouseMove((int) x, (int) y);
    }

    @Override
    public boolean mouseDragged(double x, double y, int activeButton, double dx, double dy) {
        return true;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        this.dragHandler.mouseDown((int) x, (int) y, button);
        return true;
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        this.dragHandler.mouseUp((int) x, (int) y, button);
        return true;
    }

    public NodeCanvasWidget getCanvas() {
        return canvas;
    }

    public InteractionHandler getInteractionHandler() {
        return dragHandler;
    }

    public class InteractionHandler {
        public DragState dragState = DragState.NOT_DRAGGING;
        public Vector2i startPoint;
        public Object draggedObject;
        public Vector2i grabOffset;

        public void mouseDown(int mouseX, int mouseY, int button) {
            if (dragState != DragState.NOT_DRAGGING) {
                return;
            }

            if (SpellBookScreen.this.picker.handleClick(mouseX, mouseY)) {
                return;
            }

            if (!SpellBookScreen.this.canvas.containsPoint(mouseX, mouseY)) {
                return;
            }

            Optional<Object> clicked = SpellBookScreen.this.canvas.getObjectAtLocation(mouseX, mouseY);

            if (clicked.isEmpty()) {
                this.dragState = DragState.PANNING_CANVAS;
                this.startPoint = new Vector2i(mouseX, mouseY);
                SpellBookScreen.this.canvas.startWindowPan();
            } else {
                if (clicked.get() instanceof SocketInstance socket) {
                    this.dragState = DragState.DRAGGING_EDGE;
                    this.draggedObject = socket;
                    SpellBookScreen.this.canvas.startDragEdge(socket, mouseX, mouseY);
                } else if (clicked.get() instanceof NodeInstance node) {
                    this.dragState = DragState.DRAGGING_NODE;
                    Vector2i nodeGlobalPos = SpellBookScreen.this.canvas.getNodePositionGlobal(node);
                    this.grabOffset = new Vector2i(nodeGlobalPos.x - mouseX, nodeGlobalPos.y - mouseY);

                    if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                        NodeInstance copy = node.getBaseNode().createInstance();
                        SpellBookScreen.this.spell.getGraph().addInstance(copy);
                        copy.setPosition(node.getX(), node.getY());
                        this.draggedObject = copy;
                    } else {
                        this.draggedObject = node;
                        SpellBookScreen.this.spell.getGraph().moveInstanceToTop(node);
                    }
                } else {
                    ModConstants.LOG.warn("Received unknown clicked object from node canvas: {}", clicked.get().getClass().getName());
                }
            }
        }

        public void mouseMove(int x, int y) {
            if (this.dragState == DragState.PANNING_CANVAS) {
                int dx = x - this.startPoint.x;
                int dy = y - this.startPoint.y;
                SpellBookScreen.this.canvas.setWindowPanOffset(dx, dy);
            } else if (this.dragState == DragState.DRAGGING_NODE) {
                SpellBookScreen.this.canvas.setNodePositionLocal((NodeInstance)this.draggedObject, x + this.grabOffset.x ,y + this.grabOffset.y);
            } else if (this.dragState == DragState.DRAGGING_EDGE) {
                SpellBookScreen.this.canvas.setDraggedEdgePos(x, y);
            }
        }

        public void mouseUp(int mouseX, int mouseY, int button) {
            // drop nodes, connect edges
            if (this.dragState == DragState.DRAGGING_EDGE) {
                SpellBookScreen.this.canvas.stopDragEdge(mouseX, mouseY);
            }

            if (this.dragState == DragState.DRAGGING_NODE) {
                if (mouseX >= SpellBookScreen.this.x + SpellBookScreen.this.mainAreaWidth - 16 && mouseX < SpellBookScreen.this.x + SpellBookScreen.this.mainAreaWidth && mouseY >= SpellBookScreen.this.y && mouseY < mouseY + SpellBookScreen.this.mainAreaHeight) {
                    SpellBookScreen.this.canvas.deleteNode((NodeInstance)this.draggedObject);
                }
            }

            this.dragState = DragState.NOT_DRAGGING;
            this.draggedObject = null;
            this.startPoint = null;
        }

    }

    public enum DragState {
        NOT_DRAGGING,
        PANNING_CANVAS,
        DRAGGING_NODE,
        DRAGGING_EDGE,

    }
}
