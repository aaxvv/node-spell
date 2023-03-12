package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;
import eu.aaxvv.node_spell.spell.graph.structure.FlowNode;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.sub_spell.SubSpellInstanceData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class SpellRunner {
    protected SpellGraph graph;
    protected SpellContext ctx;
    protected int waitTicks;
    protected NodeInstance currentNode;
    protected SpellRunner activeSubRunner;
    protected boolean running;

    protected SubSpellInstanceData subSpellData;

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
        } else {
            ctx.getCaster().asPlayer().ifPresent(player -> {
                player.displayClientMessage(Component.translatable("error.node_spell.spell_no_entrypoint").withStyle(ChatFormatting.RED), true);
            });
            ModConstants.LOG.warn("Tried to run spell without an entrypoint: '{}'", this.ctx.getSpellName());
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
                throw new SpellExecutionException(ex);
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
                try {
                    this.currentNode.run(this.ctx);
                } catch (SpellExecutionException ex) {
                    ex.addNodeContext(this.currentNode);
                    throw ex;
                }
            }
            this.waitTicks = baseNode.getExecutionDelay();

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

    public SubSpellInstanceData getSubSpellData() {
        return this.subSpellData;
    }
}
