package io.github.overlordsiii.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.overlordsiii.ConfiguredKeepInventory;
import io.github.overlordsiii.config.InventoryConfig;
import io.github.overlordsiii.mixinterfaces.PlayerInventoryExt;
import me.shedaniel.autoconfig.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class InventoryCommand {
    private static InventoryConfig config = ConfiguredKeepInventory.Config;
    private static ConfigManager manager = ConfiguredKeepInventory.manager;
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access){
        dispatcher.register(literal("inventory")
            .requires(source -> config.needsOP ? source.hasPermissionLevel(2) : source.hasPermissionLevel(0))
                .then(literal("toggle")
                    .then(literal("mod")
                        .executes(context -> executeToggle(context, config.enableConfig, "The mod is now turned ", "mod")))
                    .then(literal("needsop")
                        .executes(context -> executeToggle(context, config.enableConfig, "The needsOp rule has been turned ", "needsop")))
                    .then(literal("roundUp")
                        .executes(context -> executeToggle(context, config.roundUp, "The Keep Inventory Rounding rule has been turned ", "roundup")))
                    .then(literal("vanishing")
                        .executes(context -> executeToggle(context, config.disableVanishingCurse, "Vanishing is now currently turned ", "vanishing")))
                    .then(literal("binding")
                        .executes(context -> executeToggle(context, config.disableBindingCurse, "Binding is now currently turned ", "binding")))
                    .then(literal("inventoryTotem")
                        .executes(context -> executeToggle(context, config.inventoryTotems, "Totems activated from your inventory is now currently turned ", "inventoryTotem")))
                    .then(literal("autoHungerReplenish")
                        .executes(context -> executeToggle(context, config.hungerReplenish, "Hunger Automatically Replenishing is now currently turned ", "autoHungerReplenish")))
                    .then(literal("helpfulDeathMsg")
                        .executes(context -> executeToggle(context, config.helpFullDeathMessages, "Helpful Death Messages is now currently turned ", "helpfulDeathMsg")))
                    .then(literal("xpLostOnDeath")
                        .executes(context -> executeToggle(context, config.loseXpOnDeath, "Xp Lost on death is now turned ", "xpLostOnDeath")))
                    .then(literal("namedItemsDoNotDrop")
                        .executes(context -> executeToggle(context, config.namedItemsDoNotDrop, "Named Items Do Not Drop is now turned ", "namedItemsDoNotDrop"))))
                .then(literal("set")
                    .then(literal("droprate")
                        .then(argument("droprate", IntegerArgumentType.integer(0, 100))
                            .executes(context -> executeSet(context, config.configdroprate, IntegerArgumentType.getInteger(context, "droprate"), "The Inventory Droprate is now set to ", true, "droprate"))))
                    .then(literal("hungerRefresh")
                        .then(argument("hungerRefresh", IntegerArgumentType.integer(0, 19))
                            .executes(context -> executeSet(context, config.hungerRefreshLimit, IntegerArgumentType.getInteger(context, "hungerRefresh"), "The Hunger Refresh Rate is now set to ", false, "hungerRefresh"))))
                    .then(literal("helpfullDeathMsg")
                        .then(argument("message", StringArgumentType.word())
                            .executes(context -> executeSetString(context, StringArgumentType.getString(context, "message"), "helpfullDeathMsg", "The Helpful Death Message is now \"%s\"")))))
                .then(literal("add")
                    .then(literal("items")
                        .then(argument("itemarg", ItemStackArgumentType.itemStack(access))
                            .executes(context -> executeAdd(context, (ArrayList<String>) config.itemsSavedList, ItemStackArgumentType.getItemStackArgument(context, "itemarg").getItem().toString(), "Added item: %s to the item save list.", "items", ItemStackArgumentType.getItemStackArgument(context, "itemarg").getItem()))))
                    .then(literal("names")
                        .then(argument("namearg", StringArgumentType.greedyString())
                            .executes(context -> executeAdd(context, (ArrayList<String>)config.namesSavedList, StringArgumentType.getString(context, "namearg"), "Added name: %s to the name save list", "names", null)))))
                .then(literal("remove")
                    .then(literal("items")
                        .then(argument("itemarg", ItemStackArgumentType.itemStack(access))
                            .executes(context -> executeRemove(context, (ArrayList<String>)config.itemsSavedList, ItemStackArgumentType.getItemStackArgument(context, "itemarg").getItem().toString(), "Removed item %s from the item save list.", "items", ItemStackArgumentType.getItemStackArgument(context, "itemarg").getItem()))))
                    .then(literal("names")
                        .then(argument("namearg", StringArgumentType.string())
                            .executes(context -> executeRemove(context, (ArrayList<String>)config.namesSavedList, StringArgumentType.getString(context, "namearg"), "Removed name %s from the item name save list.", "names", null)))))
                .then(literal("help")
                    .executes(InventoryCommand::executeSummary))
                .then(literal("info")
                    .executes(InventoryCommand::executeInfo))
                .then(literal("sort")
                    .then(literal("offhand")
                        .then(argument("stack", ItemStackArgumentType.itemStack(access))
                            .executes(context -> executeSortOffhand(context, ItemStackArgumentType.getItemStackArgument(context, "stack").getItem(), "offhand", "Sorted a %s stack into your offhand")))))
        );
    }
    private static int executeSetString(CommandContext<ServerCommandSource> ctx, String newString, String literal, String displayText) {
        newString = splitString(newString, "_", " ");
        config.helpFullDeathMessage = newString;
        String finalString = String.format(displayText, newString);
        final String finalNewString = newString;
        ctx.getSource().sendFeedback(() -> Text.literal(finalString)
                .formatted(Formatting.LIGHT_PURPLE)
                .styled(style -> style.withItalic(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/inventory set " + literal)).withHoverEvent(
                                        new HoverEvent(HoverEvent.Action.SHOW_TEXT
                                                , Text.literal(finalNewString)))), true);
        manager.save();
        return 1;
    }
    private static int executeSortOffhand(CommandContext<ServerCommandSource> ctx, Item itemToSort, String literal, String displayText) {
        if (ctx.getSource().getPlayer() == null) {
            ctx.getSource().sendFeedback(() -> Text.literal("Run this command as a player!"), false);
            return -1;
        }

       PlayerInventory inventory = ctx.getSource().getPlayer().getInventory();
        ((PlayerInventoryExt)inventory).sortOffHand(new ItemStack(itemToSort));
       String finalString =  String.format(displayText, itemToSort.toString());
        ctx.getSource().sendFeedback(() -> Text.literal(finalString)
                .formatted(Formatting.GRAY)
                .styled(style -> style.withItalic(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND
                                , "/inventory sort " + literal + " " + Registries.ITEM.getId(itemToSort)))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM,
                                new HoverEvent.ItemStackContent(new ItemStack(itemToSort)))))
                , true);
        manager.save();
        return 1;
    }

    @SuppressWarnings("ALL")
    private static String splitString(String string, String toSplitFromString, String replacement){
        StringBuilder builder = new StringBuilder();
       for (String s : string.split(toSplitFromString)){
           builder.append(s);
           builder.append(replacement);
       }
        return builder.toString();
    }
    private static int executeToggle(CommandContext<ServerCommandSource> ctx, boolean rule, String displayedText, String literal) {
        rule = !rule;
        //sad :( hardcode
        switch (literal){
            case "mod": config.enableConfig = rule; break;
            case "needsop": config.needsOP = rule; break;
            case "roundup": config.roundUp = rule; break;
            case "vanishing": config.disableVanishingCurse = rule; break;
            case "binding": config.disableBindingCurse = rule; break;
            case "inventoryTotem": config.inventoryTotems = rule; break;
            case "autoHungerReplenish": config.hungerReplenish = rule; break;
            case "helpfulDeathMsg": config.helpFullDeathMessages = rule; break;
            case "xpLostOnDeath": config.loseXpOnDeath = rule; break;
            case "namedItemsDoNotDrop": config.namedItemsDoNotDrop = rule; break;
        }
        String added;
        if (displayedText.contains("Vanishing") || displayedText.contains("Binding")){
            added = rule ? "off" : "on";
        } else {
            added = rule ? "on" : "off";
        }
        ctx.getSource().sendFeedback(() -> Text.literal(displayedText + added)
                .formatted(Formatting.GRAY)
                .styled(style -> style.withItalic(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND
                                , "/inventory toggle " + literal))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT
                                , Text.literal("/inventory toggle " + literal))))
                , true);
        manager.save();
        return 1;
    }
    @SuppressWarnings("ParameterCanBeLocal")
    private static int executeSet(CommandContext<ServerCommandSource> ctx, int current, int tobeSet, String displayedtext, boolean percentage, String literal){
        current = tobeSet;
        String percent = percentage ? "percent" : " ";
        switch (literal){
            case "droprate": config.configdroprate = current;
            case "hungerRefresh": config.hungerRefreshLimit = current;
        }
        int finalCurrent = current;
        ctx.getSource().sendFeedback(() -> Text.literal(displayedtext + finalCurrent + " " + percent)
                .formatted(Formatting.WHITE)
                .styled(style -> style.withItalic(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND
                                , "/inventory set " + literal + " " + tobeSet))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT
                                , Text.literal("/inventory set " + literal + " " + tobeSet))))
                , true);
        manager.save();
        return 1;
    }
    private static int executeAdd(CommandContext<ServerCommandSource> ctx, ArrayList<String> list, String toAdd, String displayedText, String literal, Item nullable) throws CommandSyntaxException {
        if (!list.contains(toAdd)) {
            list.add(toAdd);
          String finalString = String.format(displayedText, toAdd);
          if (nullable != null) {
              ctx.getSource().sendFeedback( () -> Text.literal(finalString)
                      .formatted(Formatting.GOLD)
                      .styled(style -> style.withItalic(true)
                              .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                      "/inventory remove " + literal + " " + toAdd))
                              .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM,
                                      new HoverEvent.ItemStackContent(
                                              new ItemStack(nullable)))))
                      , true);
          }
          else{
              ctx.getSource().sendFeedback(() -> Text.literal(finalString)
                      .formatted(Formatting.GOLD)
                      .styled(style -> style.withItalic(true)
                              .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                      "/inventory remove " + literal + " " + toAdd))
                              ), true);
          }
        }
        else{
            ctx.getSource().getPlayer().sendMessage(Text.literal("The Save List you tried to add to already had that item/name").formatted(Formatting.RED), false);
        }
        manager.save();
        return 1;
    }

    private static int executeRemove(CommandContext<ServerCommandSource> ctx, ArrayList<String> list, String toRemove, String displayedText, String literal, Item nullable) throws CommandSyntaxException {
        if (list.contains(toRemove)){
            list.remove(toRemove);
            String finalString = String.format(displayedText, toRemove);
            if (nullable != null){
                ctx.getSource().sendFeedback(() -> Text.literal(finalString)
                                .formatted(Formatting.AQUA)
                                .styled(style -> style.withItalic(true)
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                                "/inventory add " + literal + " " + toRemove))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM,
                                                new HoverEvent.ItemStackContent(
                                                        new ItemStack(nullable)))))
                        , true);
            }
            else{
                ctx.getSource().sendFeedback(() -> Text.literal(finalString)
                        .formatted(Formatting.AQUA)
                        .styled(style -> style.withItalic(true)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                        "/inventory add " + literal + " " + toRemove))
                        ), true);
            }
        }
        else{
            ctx.getSource().getPlayer().sendMessage(Text.literal("The Save List you tried to remove from did not have that item/name").formatted(Formatting.RED), false);
        }
        manager.save();
        return 1;
    }
    private static int executeInfo(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        try {
            for (Field field : InventoryConfig.class.getDeclaredFields()) {
                    ctx.getSource().getPlayer().sendMessage(
                            Text.literal(field.getName() + " = " + field.get(config).toString())
                                    .styled(style -> style.withClickEvent(
                                            FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ?
                                                    new ClickEvent(ClickEvent.Action.OPEN_FILE, FabricLoader.getInstance().getConfigDir() + "\\" + "inventory.json5")
                                                    : new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/inventory info"))), false);


                }
        } catch (IllegalAccessException ex){
            ex.printStackTrace();
        }
        return 1;
    }

    private static int executeSummary(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        player.sendMessage(Text.literal("-------------------------------------------------------------------------------------").formatted(Formatting.AQUA), false);
        player.sendMessage(Text.literal("                           Configured Inventory's Summary/Help"), false);
        player.sendMessage(Text.literal("/inventory set (integer) - sets the inventory droprate "), false);
        player.sendMessage(Text.literal("/inventory on/off - turns the mod on and off"), false);
        player.sendMessage(Text.literal("/inventory get info - sends all the info about the config values of the mod back to the player"), false);
        player.sendMessage(Text.literal("/inventory get summary - sends this message to the player"), false);
        player.sendMessage(Text.literal("/inventory add item (item) - adds an item to the item save list."), false);
        player.sendMessage(Text.literal("/inventory add name (name) - adds an name to the name save list. "), false);
        player.sendMessage(Text.literal("/inventory remove item (item) - removes an item from the item save list"), false);
        player.sendMessage(Text.literal("/inventory remove name (name) - removes an item from the name save list"), false);
        player.sendMessage(Text.literal("/inventory disable vanishing - makes the vanishing curse ineffective and now they drop like normal"), false);
        player.sendMessage(Text.literal("/inventory disable binding - makes the binding curse ineffective"), false);
        player.sendMessage(Text.literal("/inventory enable binding - turns binding back on"), false);
        player.sendMessage(Text.literal("/inventory enable vanishing - turns vanishing back on"), false);
        player.sendMessage(Text.literal("/inventory roundUp - experimental feature, tells inventory to round up or not"), false);
        player.sendMessage(Text.literal("/inventory help  - also displays this message"), false);
        player.sendMessage(Text.literal("--------------------------------------------------------------------------------------").formatted(Formatting.AQUA), false);
        player.sendMessage(Text.literal("Issues ? : https://github.com/OverlordsIII/ConfiguredKeepInventory/issues")
                .styled(style -> style.withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT
                                , Text.literal("Configured Keep Inventory Github Repository")))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL
                                , "https://github.com/OverlordsIII/ConfiguredKeepInventory/issues")))
                , false);
            return 1;
    }


}