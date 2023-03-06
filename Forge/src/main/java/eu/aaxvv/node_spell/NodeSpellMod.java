package eu.aaxvv.node_spell;

import eu.aaxvv.node_spell.item.ModItems;
import eu.aaxvv.node_spell.network.ForgePacketHandler;
import eu.aaxvv.node_spell.platform.registry.ForgeRegistryWrapper;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.Nodes;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

@Mod(ModConstants.MOD_ID)
public class NodeSpellMod {
    
    public NodeSpellMod() {
        registerEvents();
    }

    private void registerEvents() {

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::createRegistries);
        modBus.addListener(this::register);
        modBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.addListener(this::onLevelTick);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NodeSpellCommon.init();
        ForgePacketHandler.init();
    }

    private void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            NodeSpellCommon.onLevelTick((ServerLevel) event.level);
        }
    }

    private void onServerStarted(ServerStartedEvent event) {
        NodeSpellCommon.spellPersistentStorage.init(event.getServer());
    }

    private void register(RegisterEvent event) {
        ModItems.register((res, val) -> event.register(ForgeRegistries.Keys.ITEMS, res, () -> val));

        ResourceKey<?> registryKey = event.getRegistryKey();
        if (registryKey == Nodes.REGISTRY.getResourceKey()) {
            Nodes.registerNodes();
        } else if (registryKey == NodeCategories.REGISTRY.getResourceKey()) {
            NodeCategories.registerCategories();
        }
    }

    private void createRegistries(NewRegistryEvent event) {
        RegistryBuilder<Node> nodeRegistryBuilder = new RegistryBuilder<Node>().setName(ModConstants.resLoc("nodes"));//.disableSaving().disableSync();

        Supplier<IForgeRegistry<Node>> nodeRegistry = event.create(nodeRegistryBuilder);
        Nodes.initRegistry(new ForgeRegistryWrapper<>(nodeRegistry));


        RegistryBuilder<NodeCategory> nodeCategoryRegistryBuilder =  new RegistryBuilder<NodeCategory>().setName(ModConstants.resLoc("node_categories"));//.disableSaving().disableSync();

        Supplier<IForgeRegistry<NodeCategory>> nodeCategoryRegistry = event.create(nodeCategoryRegistryBuilder);
        NodeCategories.initRegistry(new ForgeRegistryWrapper<>(nodeCategoryRegistry));

    }
}