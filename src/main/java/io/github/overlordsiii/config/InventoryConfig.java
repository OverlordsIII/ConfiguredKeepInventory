package io.github.overlordsiii.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

@Config(name = "inventoryconfig")
public class InventoryConfig implements ConfigData {
    @Comment("Effectively turns the mod on and off")
    public boolean enableConfig = true;
    @Comment("Specifies what percentage of items drop. The rest of the items stay in the inventory. Curse of vanishing is not affected by this rule")
    public int configdroprate = 100;
    @Comment("Specifies if commands can be used to control config values. Requires a game restart to enable changes. Cannot be changed via commands")
    public boolean commandUsage = true;
    @Comment("Specifies which items will always be saved and never lost, even if config droprate is 100%. Does not work if curse of vanishing is on the item")
    public List<String> itemsSavedList = new ArrayList<>();
    @Comment("Specifies which names of items will be saved and never lost. Has to be set through a custom name (such as from an anvil), and does not work on curse of vanishing items")
    public List<String> namesSavedList = new ArrayList<>();
    @Comment("Makes vanishing not work. Will apply to other config features on this list, so if this feature is on, items with vanishing will be saved")
    public boolean disableVanishingCurse = false;
    @Comment("Makes Binding curses ineffective. This means that even if an item has binding is can still be moved around the inventory if this is turned on")
    public boolean disableBindingCurse = false;
}
