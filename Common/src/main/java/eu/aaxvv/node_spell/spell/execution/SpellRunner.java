package eu.aaxvv.node_spell.spell.execution;

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

    protected int waitTicks;
    protected NodeInstance currentNode;
    protected SpellRunner activeSubRunner;
    protected boolean running;

    public SpellRunner(SpellGraph graph, SpellContext ctx) {
        this(ctx);
        this.graph = graph;
        this.running = false;
    }

    public SpellRunner(SpellContext ctx) {
        this.ctx = ctx;
    }

    public void start() {
        this.currentNode = this.graph.getEntrypoint();
        if (this.currentNode != null) {
            this.running = true;
        }
    }

    public void stop() {
        this.currentNode = null;
        this.running = false;
    }
    public void tick() {
        if (!this.running) {
            return;
        }

        if (this.waitTicks > 0) {
            waitTicks--;
        }
        while (this.waitTicks == 0 && this.running) {
            try {
                tickInner();
            } catch (SpellExecutionException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new SpellExecutionException("<Unhandled Exception> " + ex.getMessage());
            }
        }
    }

    protected void tickInner() {
        if (this.activeSubRunner != null) {
            this.activeSubRunner.tick();
            this.waitTicks = 1;

            if (!this.activeSubRunner.running) {
                this.activeSubRunner = null;
            }
            return;
        }

        if (this.currentNode == null) {
            this.running = false;
            return;
        }

        Node baseNode = this.currentNode.getBaseNode();

        if (baseNode instanceof FlowNode flowNode) {
            SpellRunner subRunner = flowNode.getSubRunner(this.ctx, this.currentNode);

            if (subRunner != null) {
                this.activeSubRunner = subRunner;
                subRunner.start();
            } else {
                this.currentNode.run(this.ctx);
            }
            this.waitTicks = baseNode.getExecutionDelay();

//            Optional<NodeInstance> nextInstance = flowNode.getNextInstanceInFlow(this.ctx, this.currentNode);
            this.currentNode = getNextInstanceInFlow();
        } else {
            throw new IllegalStateException("Cannot execute non-flow node.");
        }
    }

    protected NodeInstance getNextInstanceInFlow() {
        Node baseNode = this.currentNode.getBaseNode();

        if (baseNode instanceof FlowNode flowNode) {
            return flowNode.getNextInstanceInFlow(this.ctx, this.currentNode).orElse(null);
        } else {
            return null;
        }
    }


//    public boolean run() {
//        NodeInstance entrypoint = this.graph.getEntrypoint();
//        this.runFromNode(entrypoint);
//        return true;
//    }
//
//    protected void runFromNode(NodeInstance instance) {
//        try {
//            NodeInstance currentInstance = instance;
//            while (currentInstance != null) {
//                Node baseNode = currentInstance.getBaseNode();
//
//                if (baseNode instanceof FlowNode flowNode) {
//                    SpellRunner subRunner = flowNode.getSubRunner(this.ctx, currentInstance);
//
//                    if (subRunner != null) {
//                        subRunner.run();
//                    } else {
//                        currentInstance.run(this.ctx);
//                    }
//
//                    Optional<NodeInstance> nextInstance = flowNode.getNextInstanceInFlow(this.ctx, currentInstance);
//                    currentInstance = nextInstance.orElse(null);
//                } else {
//                    throw new IllegalStateException("Cannot execute non-flow node.");
//                }
//            }
//        } catch (SpellExecutionException ex) {
//            if (ctx.getCaster() instanceof Player player) {
//                player.displayClientMessage(Component.literal(ex.getMessage()).withStyle(ChatFormatting.RED), true);
//            }
//        } catch (Exception ex) {
//            if (ctx.getCaster() instanceof Player player) {
//                player.displayClientMessage(Component.literal("<Unhandled Exception> " + ex.getMessage()).withStyle(ChatFormatting.RED), true);
//            }
//        }
//    }
}
