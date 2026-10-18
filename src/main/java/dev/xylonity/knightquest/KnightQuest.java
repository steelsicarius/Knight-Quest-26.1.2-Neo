package dev.xylonity.knightquest;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

/**
 * Knight Quest - port to NeoForge 26.1.2 (Minecraft 26.1.2).
 * Ported from Knight-Quest-1.21-multi (NeoForge 21.1.x).
 */
@Mod(KnightQuest.MOD_ID)
public class KnightQuest {
    public static final String MOD_ID = "knightquest";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KnightQuest(IEventBus modBus) {
        LOGGER.info("[Knight Quest] NeoForge 26.1.2 port - bootstrap");
    }
}
