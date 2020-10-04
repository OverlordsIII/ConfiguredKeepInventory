package io.github.overlordsiii;


import io.github.overlordsiii.config.InventoryConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigManager;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ConfiguredKeepInventory implements ModInitializer {
    public static ConfigManager manager;
    static {
      manager = (ConfigManager) AutoConfig.register(InventoryConfig.class, JanksonConfigSerializer::new);
    }
    public static Logger LOGGER = LogManager.getLogger("ConfiguredKeepInventory");
    public static InventoryConfig Config = AutoConfig.getConfigHolder(InventoryConfig.class).getConfig();
    @Override
    public void onInitialize() {

    }
}
