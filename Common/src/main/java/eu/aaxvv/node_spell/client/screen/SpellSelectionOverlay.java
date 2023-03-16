package eu.aaxvv.node_spell.client.screen;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import eu.aaxvv.node_spell.client.util.RenderUtil;
import eu.aaxvv.node_spell.item.ModItems;
import eu.aaxvv.node_spell.network.packet.UpdateWandActiveSpellC2SPacket;
import eu.aaxvv.node_spell.platform.services.ClientPlatformHelper;
import eu.aaxvv.node_spell.util.ColorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.Arrays;

public class SpellSelectionOverlay extends GuiComponent {
    private static final int SLICE_COUNT = 8;
    private static final double SLICE_ANGLE = (2 * Math.PI) / SLICE_COUNT;
    private static final int CENTER_OFFSET = 10;
    private static final int MENU_RADIUS = 105;
    private static final int TEXT_MAX_WIDTH = 80;

    private static final int SLICE_COLOR = 0xC0101010;
    private static final int ACTIVE_SLICE_COLOR = 0xC0505050;

    private boolean active;
    private int activeSlice;
    private int wandSlot;

    private final String[] activeSpellNames;

    public SpellSelectionOverlay() {
        this.active = false;
        this.activeSlice = -1;
        this.activeSpellNames = new String[SLICE_COUNT];
    }

    public void activate() {
        boolean success = setSpellNamesFromBook();
        if (!success) {
            return;
        }

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

        if (this.activeSlice != -1 && this.wandSlot != -1) {
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

        drawSlices(pose, mouseX, mouseY);
        drawText(pose);
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

        pose.pushPose();
        for (int i = 0; i < SLICE_COUNT; i++) {
            double startAngle = SLICE_ANGLE * i + (SLICE_ANGLE * 0.5);

            String text = this.activeSpellNames[i];
            if (text == null) {
                continue;
            }

            pose.setIdentity();
            // translate to middle of wheel
            pose.last().pose().translate(center.x, center.y, 0);

            int fullWidth = font.width(text);
            int width = Math.min(fullWidth, TEXT_MAX_WIDTH);

            // rotate to angle and translate out to middle of ring
            if (startAngle > Math.PI) {
                pose.last().pose().rotate((float)(startAngle - ((3 * Math.PI) / 2)), 0, 0, 1);
                pose.last().pose().translate(-(MENU_RADIUS / 2f), -4.5f, 0);
            } else {
                pose.last().pose().rotate((float)(startAngle - (Math.PI / 2)), 0, 0, 1);
                pose.last().pose().translate((MENU_RADIUS / 2f), -4.5f, 0);
            }

            RenderUtil.drawTextTruncated(pose, font, text, -(width / 2), 0, TEXT_MAX_WIDTH, 0xFFFFFFFF);
        }
        pose.popPose();
    }

    private boolean shouldClose() {
        if (!Minecraft.getInstance().options.keyAttack.isDown()) {
            return true;
        }

        Player player = Minecraft.getInstance().player;
        return player == null || player.getMainHandItem().getItem() != ModItems.WAND;
    }

    private boolean setSpellNamesFromBook() {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return false;
        }

        ItemStack found = null;

        for(int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(ModItems.SPELL_BOOK)) {
                if (found != null) {
                    player.displayClientMessage(Component.translatable("gui.node_spell.multiple_spell_books_found").withStyle(ChatFormatting.RED), true);
                    return false;
                }

                found = stack;
            }
        }

        if (found == null) {
            player.displayClientMessage(Component.translatable("gui.node_spell.spell_book_not_found").withStyle(ChatFormatting.RED), true);

            return false;
        }

        ListTag activeSpellsList = found.getOrCreateTag().getList("ActiveSpells", Tag.TAG_STRING);
        Arrays.fill(this.activeSpellNames, null);
        for (int i = 0; i < activeSpellsList.size(); i++) {
            this.activeSpellNames[i] = activeSpellsList.get(i).getAsString();
        }
        return true;
    }
}
