package eu.aaxvv.node_spell;

import eu.aaxvv.node_spell.item.ModItems;
import eu.aaxvv.node_spell.spell.graph.NodeCategories;
import eu.aaxvv.node_spell.spell.graph.Nodes;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.graph.structure.NodeCategory;
import net.minecraft.core.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

@Mod(ModConstants.MOD_ID)
public class NodeSpellMod {
    
    public NodeSpellMod() {

        registerEvents();

        ModConstants.LOG.info("Hello Forge world!");
    }

    private void registerEvents() {

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::createRegistries);
        modBus.addListener(this::register);
    }

    private void register(RegisterEvent event) {
        ModItems.register((res, val) -> event.register(ForgeRegistries.Keys.ITEMS, res, () -> val));

        Registry<?> vanillaRegistry = event.getVanillaRegistry();
        if (vanillaRegistry == Nodes.REGISTRY_SUPPLIER.get()) {
            //TODO: is this allowed?
            ModConstants.LOG.error("################### Registering nodes");
            Nodes.registerNodes();
        } else if (vanillaRegistry == NodeCategories.REGISTRY_SUPPLIER.get()) {
            //TODO: is this allowed?
            ModConstants.LOG.error("################### Registering categories");
            NodeCategories.registerCategories();
        }
    }

    @SuppressWarnings("unchecked")
    private void createRegistries(NewRegistryEvent event) {
        try {
            Method hasWrapperMethod = RegistryBuilder.class.getDeclaredMethod("hasWrapper");
            hasWrapperMethod.setAccessible(true);

            RegistryBuilder<Node> nodeRegistryBuilder = new RegistryBuilder<Node>().setName(ModConstants.resLoc("nodes")).disableSaving().disableSync();
            hasWrapperMethod.invoke(nodeRegistryBuilder);

            Supplier<IForgeRegistry<Node>> nodeRegistry = event.create(nodeRegistryBuilder);
            Nodes.initRegistry(() -> this.getWrappedRegistry((ForgeRegistry<Node>)nodeRegistry.get()));


            RegistryBuilder<NodeCategory> nodeCategoryRegistryBuilder =  new RegistryBuilder<NodeCategory>().setName(ModConstants.resLoc("nodes")).disableSaving().disableSync();
            hasWrapperMethod.invoke(nodeCategoryRegistryBuilder);

            Supplier<IForgeRegistry<NodeCategory>> nodeCategoryRegistry = event.create(nodeCategoryRegistryBuilder);
            NodeCategories.initRegistry(() -> this.getWrappedRegistry((ForgeRegistry<NodeCategory>) (nodeCategoryRegistry.get())));
            ModConstants.LOG.error("################### Finished initializing registries");

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    private <T> Registry<T> getWrappedRegistry(ForgeRegistry<T> forgeRegistry) {
        try {
            Method getWrapperMethod = ForgeRegistry.class.getDeclaredMethod("getWrapper");
            getWrapperMethod.setAccessible(true);
            return (Registry<T>) getWrapperMethod.invoke(forgeRegistry);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}