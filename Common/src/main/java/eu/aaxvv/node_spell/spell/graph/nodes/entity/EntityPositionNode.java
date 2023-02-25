package eu.aaxvv.node_spell.spell.graph.nodes.entity;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.SpellContext;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.world.entity.Entity;

public class EntityPositionNode extends Node
{
    public final Socket sEntity;
    public final Socket sPosition;

    public EntityPositionNode() {
        super("Entity Pos.", NodeCategories.ENTITY, ModConstants.resLoc("entity_pos"));
        this.sEntity = addInputSocket(Datatype.ENTITY, "Entity");
        this.sPosition = addOutputSocket(Datatype.VECTOR, "Pos.");
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        Entity entity = instance.getSocketValue(sEntity, ctx).entityValue();
        instance.setSocketValue(sPosition, Value.createVector(entity.position()));
    }
}
