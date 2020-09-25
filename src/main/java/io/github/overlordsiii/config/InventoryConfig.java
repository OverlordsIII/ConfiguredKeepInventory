package io.github.overlordsiii.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "inventoryconfig")
public class InventoryConfig implements ConfigData {
    @Comment("Effectively turns the mod on and off")
    public boolean enableConfig = true;
    @Comment("Specifies what percentage of items drop. The rest of the items stay in the inventory")
    public int configdroprate = 0;
    @Comment("Specifies if commands can be used to control config values. Requires a game restart to enable changes. Cannot be changed via commands")
    public boolean commandUsage = true;
}
