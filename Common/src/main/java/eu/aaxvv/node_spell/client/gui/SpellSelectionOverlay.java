package eu.aaxvv.node_spell.client.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import eu.aaxvv.node_spell.item.ModItems;
import eu.aaxvv.node_spell.network.packet.UpdateWandActiveSpellC2SPacket;
import eu.aaxvv.node_spell.platform.services.ClientPlatformHelper;
import eu.aaxvv.node_spell.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class SpellSelectionOverlay extends GuiComponent {
    private static final int SLICE_COUNT = 8;
    private static final double SLICE_ANGLE = (2 * Math.PI) / SLICE_COUNT;
    private static final int CENTER_OFFSET = 10;
    private static final int MENU_RADIUS = 150;
    private static final int TEXT_RADIUS = 100;

    private static final int SLICE_COLOR = 0xC0101010;
    private static final int ACTIVE_SLICE_COLOR = 0xC0505050;

    private boolean active;
    private int activeSlice;
    private int wandSlot;
    private String[] activeSpellNames;
    private ErrorType error;

    public SpellSelectionOverlay() {
        this.active = false;
        this.activeSlice = -1;
        this.error = ErrorType.NONE;
        this.activeSpellNames = new String[SLICE_COUNT];
    }

    public void activate() {
        setSpellNamesFromBook();
        this.active = true;
        Minecraft.getInstance().mouseHandler.releaseMouse();

        Player player = Minecraft.getInstance().player;
        if (player != null && player.getMainHandItem().is(ModItems.WAND)) {
            this.wandSlot = player.getInventory().selected;
        } else {
            this.wandSlot = -1;
        }
    }

    public void deactivate() {
        this.active = false;
        Minecraft.getInstance().mouseHandler.grabMouse();

        if (this.error == ErrorType.NONE && this.activeSlice != -1 && this.wandSlot != -1) {
            String spellName = this.activeSpellNames[this.activeSlice];
            if (spellName == null) {
                spellName = "";
            }

            ClientPlatformHelper.INSTANCE.sendToServer(new UpdateWandActiveSpellC2SPacket(this.wandSlot, spellName));
        }
    }

    public void render(PoseStack pose, float tickDelta) {
        if (!this.active) {
            return;
        }

        if (shouldClose()) {
            deactivate();
            // released
            return;
        }

        double mouseX = Minecraft.getInstance().mouseHandler.xpos();
        double mouseY = Minecraft.getInstance().mouseHandler.ypos();

        if (this.error == ErrorType.NONE) {
            drawSlices(pose, mouseX, mouseY);
            drawText(pose);
        }
    }

    private void drawSlices(PoseStack pose, double mouseX, double mouseY) {
        Window window = Minecraft.getInstance().getWindow();
        Matrix4f mat = pose.last().pose();
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder bb = tess.getBuilder();

        Vector2f center = new Vector2f(window.getGuiScaledWidth() / 2f, window.getGuiScaledHeight() / 2f);
        float[] inactiveColor = new float[4];
        ColorUtil.unpackColor(SLICE_COLOR, inactiveColor);
        float[] activeColor = new float[4];
        ColorUtil.unpackColor(ACTIVE_SLICE_COLOR, activeColor);
        double mouseDx = (mouseX / window.getGuiScale()) - center.x;
        double mouseDy = (mouseY / window.getGuiScale()) - center.y;
        double mouseAngle = Math.atan2(mouseDx, -mouseDy);
        if (mouseAngle < 0) {
            mouseAngle += 2 * Math.PI;
        }
        double mouseDistance = Math.sqrt(mouseDx*mouseDx + mouseDy*mouseDy);

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        for (int i = 0; i < SLICE_COUNT; i++) {
            double startAngle = SLICE_ANGLE * i;
            double endAngle = SLICE_ANGLE * (i + 1);

            float[] color;

            if (mouseDistance > CENTER_OFFSET && mouseAngle >= startAngle && mouseAngle < endAngle) {
                this.activeSlice = i;
                color = activeColor;
            } else {
                color = inactiveColor;
            }

            float startDx = (float) Math.sin(startAngle);
            float startDy = (float) -Math.cos(startAngle);

            float endDx = (float) Math.sin(endAngle);
            float endDy = (float) -Math.cos(endAngle);

            bb.vertex(mat, center.x + startDx*CENTER_OFFSET, center.y + startDy*CENTER_OFFSET, 0).color(color[1], color[2], color[3], color[0]).endVertex();
            bb.vertex(mat, center.x + endDx*CENTER_OFFSET, center.y + endDy*CENTER_OFFSET, 0).color(color[1], color[2], color[3], color[0]).endVertex();
            bb.vertex(mat, center.x + endDx*MENU_RADIUS, center.y + endDy*MENU_RADIUS, 0).color(color[1], color[2], color[3], color[0]).endVertex();
            bb.vertex(mat, center.x + startDx*MENU_RADIUS, center.y + startDy*MENU_RADIUS, 0).color(color[1], color[2], color[3], color[0]).endVertex();
        }
        BufferUploader.drawWithShader(bb.end());
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    private void drawText(PoseStack pose) {
        Window window = Minecraft.getInstance().getWindow();
        Vector2f center = new Vector2f(window.getGuiScaledWidth() / 2f, window.getGuiScaledHeight() / 2f);
        Font font = Minecraft.getInstance().font;

        for (int i = 0; i < SLICE_COUNT; i++) {
            double startAngle = SLICE_ANGLE * i + (SLICE_ANGLE * 0.5);

            float dX = (float) Math.sin(startAngle);
            float dY = (float) -Math.cos(startAngle);

            String text = this.activeSpellNames[i];
            if (text == null) {
                continue;
            }

            int width = font.width(text);
            font.draw(pose, text, (center.x + dX*TEXT_RADIUS) - (width / 2f), center.y + dY*TEXT_RADIUS, 0xFFFFFFFF);
        }
    }

    private boolean shouldClose() {
        if (!Minecraft.getInstance().options.keyAttack.isDown()) {
            return true;
        }

        Player player = Minecraft.getInstance().player;
        return player == null || player.getMainHandItem().getItem() != ModItems.WAND;
    }

    private void setSpellNamesFromBook() {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        ItemStack found = null;

        for(int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(ModItems.SPELL_BOOK)) {
                if (found != null) {
                    this.error = ErrorType.MULTIPLE_BOOKS;
                    return;
                }

                found = stack;
            }
        }

        if (found == null) {
            this.error = ErrorType.NO_BOOK;
            return;
        }

        ListTag activeSpellsList = found.getOrCreateTag().getList("ActiveSpells", Tag.TAG_STRING);
        for (int i = 0; i < activeSpellsList.size(); i++) {
            this.activeSpellNames[i] = activeSpellsList.get(i).getAsString();
        }
    }

    private enum ErrorType {
        NONE,
        NO_BOOK,
        MULTIPLE_BOOKS
    }
}
