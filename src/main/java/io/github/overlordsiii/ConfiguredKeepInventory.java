package io.github.overlordsiii;


import io.github.overlordsiii.config.InventoryConfig;
import io.github.overlordsiii.mixin.DamageSourceInvoker;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigManager;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.damage.DamageSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ConfiguredKeepInventory implements ModInitializer {
    public static ConfigManager<InventoryConfig> manager;
    static {
      manager = (ConfigManager<InventoryConfig>) AutoConfig.register(InventoryConfig.class, JanksonConfigSerializer::new);
    }
    public static Logger LOGGER = LogManager.getLogger(ConfiguredKeepInventory.class);

    public static InventoryConfig Config = AutoConfig.getConfigHolder(InventoryConfig.class).getConfig();
    public static final DamageSource TOTEM_REPLACEMENT = (DamageSourceInvoker.createNewDamageSource("outOfWorld"));
    @Override
    public void onInitialize() {
        TOTEM_REPLACEMENT.bypassesArmor();
    }

}
