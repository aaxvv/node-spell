package eu.aaxvv.node_spell.platform.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Set;

public interface PlatformRegistryWrapper<T> {
    void register(ResourceLocation resLoc, T entry);
    T get(ResourceLocation resLoc);

    Set<Map.Entry<ResourceKey<T>, T>> entrySet();

    ResourceKey<? extends Registry<T>> getResourceKey();
}
