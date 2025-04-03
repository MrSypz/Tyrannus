package sypztep.tyrannus;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tyrannus implements ModInitializer {
    public static final String MODID = "tyrannus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        LOGGER.info("Tyrannus initialized!");
    }
}
