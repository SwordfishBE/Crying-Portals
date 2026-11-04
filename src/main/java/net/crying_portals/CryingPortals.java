package net.crying_portals;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.LoggerFactory;

/** Portal behavior is supplied by the additive nether_portal_frame block tag. */
public final class CryingPortals implements ModInitializer {
    public static final String MOD_ID = "crying_portals";

    @Override
    public void onInitialize() {
        String version = FabricLoader.getInstance().getModContainer(MOD_ID)
                .orElseThrow().getMetadata().getVersion().getFriendlyString();
        LoggerFactory.getLogger(MOD_ID).info("[Crying Portals] Mod initialized. Version: {}", version);
    }
}
