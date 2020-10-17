package io.github.overlordsiii.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

@Config(name = "inventory")
@Config.Gui.Background("minecraft:textures/block/barrel_side.png")
@Config.Gui.CategoryBackground(category = "EnchantmentConfig", background = "minecraft:textures/block/bookshelf.png")
@Config.Gui.CategoryBackground(category = "Core", background = "minecraft:textures/block/jukebox_side.png")
public class InventoryConfig implements ConfigData {
    @ConfigEntry.Category("Core")
    @Comment("Effectively turns the mod on and off")
    public boolean enableConfig = true;
    @ConfigEntry.Category("Core")
    @Comment("Specifies if commands can be used to control config values. Requires a game restart to enable changes. Cannot be changed via commands")
    public boolean commandUsage = true;
    @ConfigEntry.Category("Core")
    @Comment("Makes sure you need op to do any of the inventory commands. Defaults to true. Requires a game restart to apply")
    public boolean needsOP = true;
    @ConfigEntry.Category("KeepInventoryConfig")
    @Comment("Allows players to teleport to another player if they die or activate a totem")
    public boolean helpFullDeathMessages = false;
    @ConfigEntry.Category("KeepInventoryConfig")
    @Comment("The message that the players see when hovering over a helpfull death message")
    public String helpFullDeathMessage = "Want to help them out?";
    @ConfigEntry.Category("KeepInventoryConfig")
    @ConfigEntry.BoundedDiscrete(max = 100, min = 0)
    @Comment("Specifies what percentage of items drop. The rest of the items stay in the inventory. Curse of vanishing is not affected by this rule")
    public int configdroprate = 100;
    @ConfigEntry.Category("KeepInventoryConfig")
    @Comment("Tells the inventory to round up when calculating which items to drop and which stay in your inventory. If false it rounds down")
    public boolean roundUp = true;

    @ConfigEntry.Category("KeepInventoryConfig")
    @Comment("Specifies which items will always be saved and never lost, even if config droprate is 100%. Does not work if curse of vanishing is on the item")
    public List<String> itemsSavedList = new ArrayList<>();

    @ConfigEntry.Category("KeepInventoryConfig")
    @Comment("Specifies which names of items will be saved and never lost. Has to be set through a custom name (such as from an anvil), and does not work on curse of vanishing items")
    public List<String> namesSavedList = new ArrayList<>();



    @ConfigEntry.Category("EnchantmentConfig")
    @Comment("Makes vanishing not work. Will apply to other config features on this list, so if this feature is on, items with vanishing will be saved")
    public boolean disableVanishingCurse = false;

    @ConfigEntry.Category("EnchantmentConfig")
    @Comment("Makes Binding curses ineffective. This means that even if an item has binding is can still be moved around the inventory if this is turned on")
    public boolean disableBindingCurse = false;


  //  @ConfigEntry.Category("OffHandTweaks")
   // @Comment("Specifies if the elytra mode is turned on. This means that the server will search your inventory and place rockets in your off hand while taking off and then put your original stack back in your hand when you land again")
   // public boolean elytraRockets = false;

    @ConfigEntry.Category("OffHandTweaks")
    @Comment("Allows for totems be activated from anywhere in your inventory on your death")
    public boolean inventoryTotems = false;
    @ConfigEntry.Category("OffHandTweaks")
    @Comment("If hunger goes below a configurable limit, the server will replenish your hunger bar based on the food in your inventory")
    public boolean hungerReplenish = false;
    @ConfigEntry.Category("OffHandTweaks")
    @Comment("The configurable limit for the rule above. If the hunger level goes lower than this limit, autohunger starts working. Only works if hungerReplenish is set to true")
    @ConfigEntry.BoundedDiscrete(max = 19, min = 0)
    public int hungerRefreshLimit = 6;
    @ConfigEntry.Category("OffHandTweaks")
    @Comment("If this is on, totems will activate even if you fall out of the world")
    public boolean debugTotems = true;

}
