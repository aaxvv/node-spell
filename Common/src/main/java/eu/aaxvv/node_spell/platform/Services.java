package eu.aaxvv.node_spell.platform;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.platform.services.ClientPlatformHelper;
import eu.aaxvv.node_spell.platform.services.PlatformHelper;

import java.util.ServiceLoader;

public class Services {
    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        ModConstants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}