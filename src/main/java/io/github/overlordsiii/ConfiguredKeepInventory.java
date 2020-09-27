package io.github.overlordsiii;


import io.github.overlordsiii.config.InventoryConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigManager;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

public class ConfiguredKeepInventory implements ModInitializer {
    public static ConfigManager manager;
    static {
      manager = (ConfigManager) AutoConfig.register(InventoryConfig.class, JanksonConfigSerializer::new);
    }
    public static InventoryConfig Config = AutoConfig.getConfigHolder(InventoryConfig.class).getConfig();
    @Override
    public void onInitialize() {
    }
}
