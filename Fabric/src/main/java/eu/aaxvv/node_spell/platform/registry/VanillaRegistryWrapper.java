package eu.aaxvv.node_spell.platform.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class VanillaRegistryWrapper<T> implements PlatformRegistryWrapper<T> {
    private final Registry<T> wrapped;

    public VanillaRegistryWrapper(Registry<T> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void register(ResourceLocation resLoc, T entry) {
        Registry.register(this.wrapped, resLoc, entry);
    }

    @Override
    public T get(ResourceLocation resLoc) {
        return this.wrapped.get(resLoc);
    }

    @Override
    public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
        return this.wrapped.entrySet();
    }

    @Override
    public ResourceKey<? extends Registry<T>> getResourceKey() {
        return this.wrapped.key();
    }
}
