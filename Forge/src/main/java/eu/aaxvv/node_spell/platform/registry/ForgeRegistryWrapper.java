package eu.aaxvv.node_spell.platform.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ForgeRegistryWrapper<T> implements PlatformRegistryWrapper<T> {
    private IForgeRegistry<T> wrapped;
    private final Supplier<IForgeRegistry<T>> registrySupplier;

    public ForgeRegistryWrapper(Supplier<IForgeRegistry<T>> registrySupplier) {
        this.registrySupplier = registrySupplier;
    }

    private IForgeRegistry<T> getRegistry() {
        if (this.wrapped == null) {
            this.wrapped = this.registrySupplier.get();
        }

        return this.wrapped;
    }

    @Override
    public void register(ResourceLocation resLoc, T entry) {
        getRegistry().register(resLoc, entry);
    }

    @Override
    public T get(ResourceLocation resLoc) {
        return getRegistry().getValue(resLoc);
    }

    @Override
    public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
        return getRegistry().getEntries();
    }

    @Override
    public ResourceKey<? extends Registry<T>> getResourceKey() {
        IForgeRegistry<T> registry = getRegistry();
        if (registry == null) {
            return null;
        } else {
            return registry.getRegistryKey();
        }
    }
}
