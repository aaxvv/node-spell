package eu.aaxvv.node_spell.spell;

import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.FlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class SpellRunner {
    protected SpellGraph graph;
    protected SpellContext ctx;

    public SpellRunner(SpellGraph graph, SpellContext ctx) {
        this(ctx);
        this.graph = graph;
    }

    public SpellRunner(SpellContext ctx) {
        this.ctx = ctx;
    }

    public boolean run() {
        NodeInstance entrypoint = this.graph.getEntrypoint();
        this.runFromNode(entrypoint);
        return true;
    }

    protected void runFromNode(NodeInstance instance) {
        try {
            NodeInstance currentInstance = instance;
            while (currentInstance != null) {
                Node baseNode = currentInstance.getBaseNode();

                if (baseNode instanceof FlowNode flowNode) {
                    SpellRunner subRunner = flowNode.getSubRunner(this.ctx, currentInstance);

                    if (subRunner != null) {
                        subRunner.run();
                    } else {
                        currentInstance.run(this.ctx);
                    }

                    Optional<NodeInstance> nextInstance = flowNode.getNextInstanceInFlow(this.ctx, currentInstance);
                    currentInstance = nextInstance.orElse(null);
                } else {
                    throw new IllegalStateException("Cannot execute non-flow node.");
                }
            }
        } catch (SpellExecutionException ex) {
            if (ctx.getCaster() instanceof Player player) {
                player.displayClientMessage(Component.literal(ex.getMessage()).withStyle(ChatFormatting.RED), true);
            }
        } catch (Exception ex) {
            if (ctx.getCaster() instanceof Player player) {
                player.displayClientMessage(Component.literal("<Unhandled Exception> " + ex.getMessage()).withStyle(ChatFormatting.RED), true);
            }
        }
    }
}
