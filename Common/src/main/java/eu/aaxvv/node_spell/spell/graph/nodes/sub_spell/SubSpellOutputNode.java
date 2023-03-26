package eu.aaxvv.node_spell.spell.graph.nodes.sub_spell;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.gui.node_widget.TextFieldWidget;
import eu.aaxvv.node_spell.client.gui.node_widget.Widget;
import eu.aaxvv.node_spell.spell.execution.SpellContext;
import eu.aaxvv.node_spell.spell.execution.SubSpellRunner;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.runtime.SocketInstance;
import eu.aaxvv.node_spell.spell.graph.structure.Socket;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;

public class SubSpellOutputNode extends SubSpellSocketNode {
    public final Socket sIn;
    private final Datatype datatype;

    public SubSpellOutputNode(Datatype datatype) {
        super(NodeCategories.SUB_SPELL, ModConstants.resLoc("sub_output_" + datatype.shortName));
        this.datatype = datatype;
        this.sIn = addInputSocket(datatype, "socket.node_spell.empty");
    }

    @Override
    public Widget<?> createWidget(NodeInstance instance) {
        TextFieldWidget field = new TextFieldWidget(instance, this.getWidth() - 6);
        field.setLocalPosition(4, ModConstants.Sizing.HEADER_HEIGHT + 2);
        return field;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        SubSpellRunner subRunner = ctx.getCurrentRunner().getAsSubSpell();
        if (subRunner == null) {
            return;
        }

        SocketInstance socket = subRunner.getSocketInstance(instance);
        Value value = instance.getSocketValue(this.sIn, ctx);
        socket.setCurrentValue(value);
    }

    @Override
    public Socket.Direction getDirection() {
        return Socket.Direction.OUT;
    }

    @Override
    public Datatype getDatatype() {
        return this.datatype;
    }
}
