package io.github.overlordsiii;


import io.github.overlordsiii.command.InventoryCommand;
import io.github.overlordsiii.config.InventoryConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigManager;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ConfiguredKeepInventory implements ModInitializer {
    public static ConfigManager<InventoryConfig> manager;
    static {
      manager = (ConfigManager<InventoryConfig>) AutoConfig.register(InventoryConfig.class, JanksonConfigSerializer::new);
    }
    public static Logger LOGGER = LogManager.getLogger(ConfiguredKeepInventory.class);

    public static InventoryConfig Config = AutoConfig.getConfigHolder(InventoryConfig.class).getConfig();
    @Override
    public void onInitialize() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if (ConfiguredKeepInventory.Config.commandUsage && ConfiguredKeepInventory.Config.enableConfig) {
                InventoryCommand.register(dispatcher, registryAccess);
            }
        });
    }

}
