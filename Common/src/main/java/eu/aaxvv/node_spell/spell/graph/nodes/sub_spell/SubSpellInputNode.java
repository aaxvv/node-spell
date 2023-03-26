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

public class SubSpellInputNode extends SubSpellSocketNode {
    public final Socket sOut;
    private final Datatype datatype;

    public SubSpellInputNode(Datatype datatype) {
        super(NodeCategories.SUB_SPELL, ModConstants.resLoc("sub_input_" + datatype.shortName));
        this.datatype = datatype;
        this.sOut = addOutputSocket(datatype, "socket.node_spell.empty");
    }

    @Override
    public Widget<?> createWidget(NodeInstance instance) {
        TextFieldWidget field = new TextFieldWidget(instance, this.getWidth() - 6);
        field.setLocalPosition(2, ModConstants.Sizing.HEADER_HEIGHT + 2);
        return field;
    }

    @Override
    public void run(SpellContext ctx, NodeInstance instance) {
        SubSpellRunner subRunner = ctx.getCurrentRunner().getAsSubSpell();
        if (subRunner == null) {
            instance.setSocketInvalid(this.sOut);
            return;
        }

        SocketInstance socket = subRunner.getSocketInstance(instance);
        instance.setSocketValue(this.sOut, socket.getComputedValue(ctx));
    }

    @Override
    public Socket.Direction getDirection() {
        return Socket.Direction.IN;
    }

    @Override
    public Datatype getDatatype() {
        return this.datatype;
    }
}
