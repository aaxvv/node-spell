package eu.aaxvv.node_spell.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.node_widget.Widget;
import eu.aaxvv.node_spell.client.widget.NodeCanvasWidget;
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

import java.util.List;
import java.util.Optional;

/**
 * Look at BookEditScreen for inspiration
 */
public class SpellEditScreen extends Screen {
    private static final ResourceLocation BACKGROUND_LOCATION = ModConstants.resLoc("textures/gui/spell_edit_bg.png");
    private static final List<Component> DELETE_TOOLTIP = List.of(Component.translatable("gui.node_spell.delete_tooltip_1"), Component.translatable("gui.node_spell.delete_tooltip_2"));
    // normal inventory width. can be extended if needed (e.g. beacon ui is 230)
    private final int mainAreaWidth = 288;
    // height of double chest screen
    private final int mainAreaHeight = 208;
    private int x;
    private int y;

    private final Spell spell;
    private final Screen parentScreen;

    private NodeCanvasWidget canvas;
    private NodePickerWidget picker;
    private final InteractionHandler dragHandler;

    public SpellEditScreen(SpellBookScreen parentScreen, Spell spell) {
        super(Component.literal(spell.getName()));

        this.dragHandler = new InteractionHandler();
        this.spell = spell;
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        this.x = (this.width / 2) - (this.mainAreaWidth / 2);
        this.y = (this.height / 2) - (this.mainAreaHeight / 2);
        this.picker = new NodePickerWidget(this, x, y + mainAreaHeight, mainAreaWidth);
        this.canvas = new NodeCanvasWidget(x, y, mainAreaWidth, mainAreaHeight - this.picker.getHeight(), this.spell.getGraph());

        addRenderableWidget(this.canvas);
        addRenderableWidget(this.picker);
    }

    @Override
    public void onClose() {
        this.spell.getGraph().findEntrypoint();
        Minecraft.getInstance().setScreen(this.parentScreen);
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
            renderTooltip(pose, DELETE_TOOLTIP, Optional.empty(), mouseX, mouseY);
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

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if (!this.dragHandler.keyPressed(key, scanCode, modifiers)) {
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                return super.keyPressed(key, scanCode, modifiers);
            }
        }
        return true;
    }

    @Override
    public boolean charTyped(char character, int modifiers) {
        if (!this.dragHandler.charTyped(character, modifiers)) {
            super.charTyped(character, modifiers);
        }

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
        public Widget<?> focusedWidget;
        public boolean panButtonHeld;

        public void mouseDown(int mouseX, int mouseY, int button) {
            Widget<?> prevWidget = this.focusedWidget;

//            if (dragState != DragState.NOT_DRAGGING) {
//                return;
//            }

            if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                this.panButtonHeld = true;
                SpellEditScreen.this.canvas.startWindowPan();
                this.startPoint = new Vector2i(mouseX, mouseY);
                return;
            }

            if (mouseOverDeleteIcon(mouseX, mouseY)) {
                SpellEditScreen.this.canvas.clearGraph();
            }

            if (SpellEditScreen.this.picker.handleClick(mouseX, mouseY)) {
                return;
            }

            if (!SpellEditScreen.this.canvas.containsPoint(mouseX, mouseY)) {
                return;
            }

            Optional<Object> clicked = SpellEditScreen.this.canvas.getObjectAtLocation(mouseX, mouseY);

            boolean focusedWidgetClicked = false;

            if (clicked.isEmpty()) {
//                this.dragState = DragState.PANNING_CANVAS;
//                this.startPoint = new Vector2i(mouseX, mouseY);
//                SpellBookScreen.this.canvas.startWindowPan();
            } else {
                if (clicked.get() instanceof SocketInstance socket) {
                    this.dragState = DragState.DRAGGING_EDGE;
                    this.draggedObject = socket;
                    SpellEditScreen.this.canvas.startDragEdge(socket, mouseX, mouseY);
                } else if (clicked.get() instanceof Widget<?> widget){
                    if (widget == prevWidget) {
                        focusedWidgetClicked = true;
                    }

                    this.focusedWidget = widget;
//                    widget.setFocused(true);
                    Vector2i mouseLocal = SpellEditScreen.this.canvas.toLocal(mouseX, mouseY);
//                    widget.receiveMouseInput(mouseLocal.x, mouseLocal.y, button);
                    if (!this.focusedWidget.isFocused()) {
                        this.focusedWidget = null;
                    }
                } else if (clicked.get() instanceof NodeInstance node) {
                    this.dragState = DragState.DRAGGING_NODE;
                    Vector2i nodeGlobalPos = SpellEditScreen.this.canvas.getNodePositionGlobal(node);
                    this.grabOffset = new Vector2i(nodeGlobalPos.x - mouseX, nodeGlobalPos.y - mouseY);

                    if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                        NodeInstance copy = node.getBaseNode().createInstance();
                        SpellEditScreen.this.spell.getGraph().addInstance(copy);
                        copy.setPosition(node.getX(), node.getY());
                        this.draggedObject = copy;
                    } else if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
                        SpellEditScreen.this.canvas.deleteNode(node);
                        this.draggedObject = null;
                        this.dragState = DragState.NOT_DRAGGING;
                    } else {
                        this.draggedObject = node;
                        SpellEditScreen.this.spell.getGraph().moveInstanceToTop(node);
                    }
                } else {
                    ModConstants.LOG.warn("Received unknown clicked object from node canvas: {}", clicked.get().getClass().getName());
                }
            }

            if (!focusedWidgetClicked && prevWidget != null) {
//                prevWidget.setFocused(false);
                prevWidget.rollbackValue();
            }
        }

        public void mouseMove(int mouseX, int mouseY) {
//            if (this.dragState == DragState.PANNING_CANVAS) {
            if (this.panButtonHeld) {
                int dx = mouseX - this.startPoint.x;
                int dy = mouseY - this.startPoint.y;
                SpellEditScreen.this.canvas.setWindowPanOffset(dx, dy);
            }

            if (this.dragState == DragState.DRAGGING_NODE) {
                SpellEditScreen.this.canvas.setNodePositionLocal((NodeInstance)this.draggedObject, mouseX + this.grabOffset.x ,mouseY + this.grabOffset.y);
            } else if (this.dragState == DragState.DRAGGING_EDGE) {
                SpellEditScreen.this.canvas.setDraggedEdgePos(mouseX, mouseY);
                NodeCanvasWidget canvas = SpellEditScreen.this.canvas;
            }
        }

        public void mouseUp(int mouseX, int mouseY, int button) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                this.panButtonHeld = false;
                this.startPoint = null;
                return;
            }

            // drop nodes, connect edges
            if (this.dragState == DragState.DRAGGING_EDGE) {
                SpellEditScreen.this.canvas.stopDragEdge(mouseX, mouseY);
            }

            if (this.dragState == DragState.DRAGGING_NODE) {
                if (mouseOverDeleteIcon(mouseX, mouseY)) {
                    SpellEditScreen.this.canvas.deleteNode((NodeInstance)this.draggedObject);
                }
            }

            this.dragState = DragState.NOT_DRAGGING;
            this.draggedObject = null;
        }

        public boolean keyPressed(int key, int scanCode, int modifiers) {
            if (this.focusedWidget != null) {
//                this.focusedWidget.receiveKeyPress(key, scanCode, modifiers);
                if (!this.focusedWidget.isFocused()) {
                    this.focusedWidget = null;
                    return true;
                }
            }
            return false;
        }

        public boolean charTyped(char character, int modifiers) {
            if (this.focusedWidget != null) {
//                this.focusedWidget.receiveCharTyped(character, modifiers);
                if (!this.focusedWidget.isFocused()) {
                    this.focusedWidget = null;
                }
                return true;
            }

            return false;
        }

        private boolean mouseOverDeleteIcon(int mouseX, int mouseY) {
            return mouseX >= SpellEditScreen.this.x + SpellEditScreen.this.mainAreaWidth - 16
                    && mouseX < SpellEditScreen.this.x + SpellEditScreen.this.mainAreaWidth
                    && mouseY >= SpellEditScreen.this.y
                    && mouseY < SpellEditScreen.this.y + 16;
        }
    }

    public enum DragState {
        NOT_DRAGGING,
        PANNING_CANVAS,
        DRAGGING_NODE,
        DRAGGING_EDGE,

    }
}
