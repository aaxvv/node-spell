package eu.aaxvv.node_spell.client.node_widget;

import com.mojang.blaze3d.vertex.PoseStack;
import eu.aaxvv.node_spell.spell.graph.runtime.InstanceDataContainer;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class VectorNodeWidget extends Widget<Vec3> {
    public VectorNodeWidget(NodeInstance instance, int width) {
        super(instance, width, 37);

        NumberFieldWidget xField = new NumberFieldWidget(getAxisAccessor(Direction.Axis.X), width - 7);
        xField.setLocalPosition(7, 0);
        addChild(xField);
        NumberFieldWidget yField = new NumberFieldWidget(getAxisAccessor(Direction.Axis.Y), width - 7);
        yField.setLocalPosition(7, 12);
        addChild(yField);
        NumberFieldWidget zField = new NumberFieldWidget(getAxisAccessor(Direction.Axis.Z), width - 7);
        zField.setLocalPosition(7, 24);
        addChild(zField);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float tickDelta) {
        Font font = Minecraft.getInstance().font;

        font.draw(pose, "X", this.getGlobalX() + 1, this.getGlobalY() + 2, 0xFF000000);
        font.draw(pose, "Y", this.getGlobalX() + 1, this.getGlobalY() + 14, 0xFF000000);
        font.draw(pose, "Z", this.getGlobalX() + 1, this.getGlobalY() + 26, 0xFF000000);

        super.render(pose, mouseX, mouseY, tickDelta);
    }

    private InstanceDataContainer getAxisAccessor(Direction.Axis axis) {
        return new InstanceDataContainer() {
            @Override
            public void setInstanceData(Object data) {
                Vec3 currData = ((Vec3) VectorNodeWidget.this.instance.getInstanceData());
                VectorNodeWidget.this.instance.setInstanceData(currData.with(axis, (Double) data));
            }

            @Override
            public Object getInstanceData() {
                Vec3 currData = ((Vec3) VectorNodeWidget.this.instance.getInstanceData());
                return currData.get(axis);
            }
        };
    }
}
