package eu.aaxvv.node_spell.spell.graph.nodes.constant;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.node_widget.VectorNodeWidget;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.phys.Vec3;

public class VectorConstantNode extends BaseConstantNode<Vec3> {
    public VectorConstantNode() {
        super(Datatype.VECTOR, ModConstants.resLoc("const_vec"), () -> new Vec3(0, 0, 0), Value::createVector);
    }

    @Override
    public VectorNodeWidget createWidget(NodeInstance instance) {
        VectorNodeWidget field = new VectorNodeWidget(instance, this.getWidth() - 6);
        field.setLocalPosition(2, ModConstants.Sizing.HEADER_HEIGHT + 2);
        return field;
    }

    @Override
    public int getExpectedHeight() {
        return ModConstants.Sizing.SOCKET_START_Y + 34;
    }

    @Override
    public void serializeInstanceData(Object instanceData, CompoundTag dataTag) {
        Vec3 vec = ((Vec3) instanceData);

        ListTag vecList = new ListTag();
        vecList.add(DoubleTag.valueOf(vec.x));
        vecList.add(DoubleTag.valueOf(vec.y));
        vecList.add(DoubleTag.valueOf(vec.z));

        dataTag.put("Val", vecList);
    }

    @Override
    public Object deserializeInstanceData(CompoundTag dataTag) {
        ListTag components = dataTag.getList("Val", Tag.TAG_DOUBLE);
        return new Vec3(((DoubleTag) components.get(0)).getAsDouble(), ((DoubleTag) components.get(1)).getAsDouble(), ((DoubleTag) components.get(2)).getAsDouble());
    }
}
