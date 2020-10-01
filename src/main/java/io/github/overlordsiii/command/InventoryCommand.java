package io.github.overlordsiii.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.overlordsiii.ConfiguredKeepInventory;
import io.github.overlordsiii.config.InventoryConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigManager;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;




public class InventoryCommand {
    static InventoryConfig config = ConfiguredKeepInventory.Config;
    static ConfigManager manager = ConfiguredKeepInventory.manager;
    //TODO redo the Translatable text cuz lang wasnt working
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("inventory")
            .requires(source -> config.needsOP ? source.hasPermissionLevel(2) : source.hasPermissionLevel(0))
                .then(literal("set")
                    .then(argument("percentage", integer(0, 100))
                        .executes(ctx -> execute(ctx, getInteger(ctx, "percentage")))))
                .then(literal("on")
                    .executes(context -> executeOn(context, true)))
                .then(literal("off")
                    .executes(context -> executeOn(context, false)))
                .then(literal("info")
                        .executes(InventoryCommand::executeGet))
                .then(literal("add")
                    .then(literal("item")
                        .then(argument("item", ItemStackArgumentType.itemStack())
                            .executes(context -> executeAddItem(context, ItemStackArgumentType.getItemStackArgument(context, "item").getItem()))))
                    .then(literal("name"))
                        .then(argument("name", StringArgumentType.string())
                            .executes(context -> executeAddName(context, StringArgumentType.getString(context, "name")))))
                .then(literal("remove")
                    .then(literal("item")
                        .then(argument("item", ItemStackArgumentType.itemStack())
                            .executes(context -> executeRemoveItem(context, ItemStackArgumentType.getItemStackArgument(context, "item").getItem()))))
                    .then(literal("name")
                        .then(argument("name", StringArgumentType.string())
                            .executes(context -> executeRemoveName(context, StringArgumentType.getString(context, "name"))))))
                .then(literal("enable")
                    .then(literal("vanishing")
                        .executes(context -> executeEnchantEnable("vanishing", context))))
                    .then(literal("binding")
                        .executes(context -> executeEnchantEnable("binding", context)))
                .then(literal("help")
                    .executes(InventoryCommand::executeSummary))
                .then(literal("disable")
                    .then(literal("vanishing")
                        .executes(context -> executeEnchantDisable("vanishing", context))))
                    .then(literal("binding")
                        .executes(context -> executeEnchantDisable("binding", context))));
    }
    public static int execute(CommandContext<ServerCommandSource> ctx, int value) {
        config.configdroprate = value;
        manager.save();
      //  ctx.getSource().sendFeedback(new TranslatableText("inventory.set", value), true);
        ctx.getSource().sendFeedback(new LiteralText("set the inventory droprate to " + value + " percent").formatted(Formatting.AQUA), true);
        return 1;
    }
    public static int executeOn(CommandContext<ServerCommandSource> ctx, boolean onoroff){
        if (onoroff){
            config.enableConfig = true;
            manager.save();
            ctx.getSource().sendFeedback(new LiteralText("the config has been turned on").formatted(Formatting.GREEN), true);
        }
        else{
            config.enableConfig = false;
            manager.save();
            ctx.getSource().sendFeedback(new LiteralText("the config has been turned off").formatted(Formatting.RED), true);
        }
       // ctx.getSource().sendFeedback(new TranslatableText("config.on").formatted(Formatting.GREEN), true);

        return 1;
    }
    public static int executeGet(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity entity = ctx.getSource().getPlayer();
        ctx.getSource().sendFeedback(new LiteralText("showing config values").formatted(Formatting.GREEN), false);
        entity.sendMessage(new LiteralText("the config droprate is currently set to " + config.configdroprate).formatted(Formatting.AQUA), false);
        entity.sendMessage(new LiteralText("starting to read the items save list...").formatted(Formatting.AQUA), false);
        for (String item : config.itemsSavedList){
            entity.sendMessage(new LiteralText(item).formatted(Formatting.AQUA), false);
        }
        entity.sendMessage(new LiteralText("starting to read the names save list...").formatted(Formatting.AQUA), false);
        for (String name : config.namesSavedList){
            entity.sendMessage(new LiteralText(name).formatted(Formatting.AQUA), false);
        }
        entity.sendMessage(new TranslatableText("vanishingenabled.show", config.disableVanishingCurse).formatted(Formatting.AQUA), false);
        entity.sendMessage(new TranslatableText("bindingdisabled.show", config.disableBindingCurse).formatted(Formatting.AQUA), false);
        return 1;
    }
    public static int executeEnchantEnable(String enchant, CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {

        switch (enchant){
            case "vanishing":
                config.disableVanishingCurse = false;

            case "binding":
                config.disableBindingCurse = false;

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + enchant);
        }
        manager.save();
        ctx.getSource().getPlayer().sendMessage(new LiteralText("disabled the effects of the " + enchant + " curse").formatted(Formatting.AQUA), false);
        return 1;
    }
    public static int executeEnchantDisable(String enchant, CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        switch (enchant){
            case "vanishing":
                config.disableVanishingCurse = true;

            case "binding":
                config.disableBindingCurse = true;

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + enchant);
        }
        manager.save();
        ctx.getSource().getPlayer().sendMessage(new LiteralText("reenabled the effects of the " + enchant + " curse").formatted(Formatting.AQUA), false);
        return 1;
    }
    public static int executeSummary(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        player.sendMessage(new LiteralText("----------------------------------------------------------------------------------------------------").formatted(Formatting.AQUA), false);
        player.sendMessage(new LiteralText("                           Configured Inventory's Summary/Help"), false);
        player.sendMessage(new LiteralText("/inventory set (integer) - sets the inventory droprate (Sets the config value of \"configdroprate\"."), false);
        player.sendMessage(new LiteralText("/inventory on/off - turns the mod on and off. Requires a restart to fully take effect (Sets the config value of \"enableconfig\")"), false);
        player.sendMessage(new LiteralText("/inventory get info - sends all the info about the config values of the mod back to the player"), false);
        player.sendMessage(new LiteralText("/inventory get summary - sends this message to the player"), false);
        player.sendMessage(new LiteralText("/inventory add item (item) - adds an item to the item save list. Makes it so the item will be saved no matter the config droprate"), false);
        player.sendMessage(new LiteralText("/inventory add name (name) - adds an name to the name save list. If an item has a custom name and the name of the item is on the list, that item will be saved and never drop"), false);
        player.sendMessage(new LiteralText("/inventory remove item (item) - removes an item from the item save list"), false);
        player.sendMessage(new LiteralText("/inventory remove name (name) - removes an item from the name save list"), false);
        player.sendMessage(new LiteralText("/inventory disable vanishing - makes the vanishing curse ineffective and now they drop like normal"), false);
        player.sendMessage(new LiteralText("/inventory disable binding - makes the binding curse ineffective and now items can move around the inventory freely"), false);
        player.sendMessage(new LiteralText("/inventory enable binding - turns binding back on"), false);
        player.sendMessage(new LiteralText("/inventory enable vanishing - turns vanishing back on"), false);
        player.sendMessage(new LiteralText("/inventory help  - also displays this message"), false);
        player.sendMessage(new LiteralText("------------------------------------------------------------------------------------------------------").formatted(Formatting.AQUA), false);
        player.sendMessage(new LiteralText("Issues ? : https://github.com/OverlordsIII/ConfiguredKeepInventory/issues"), false);
            return 1;
    }
    public static int executeAddItem(CommandContext<ServerCommandSource> ctx, Item item){
            add(item.toString(), ctx, true);
        return 1;
    }
    public static int executeAddName(CommandContext<ServerCommandSource> ctx, String name){
        add(name, ctx, false);

        return 1;
    }
    public static int executeRemoveItem(CommandContext<ServerCommandSource> ctx, Item item){
        remove(item.toString(), ctx, true);
        return 1;
    }
    public static int executeRemoveName(CommandContext<ServerCommandSource> ctx, String name){
        remove(name, ctx, false);
        return 1;
    }
    public static void remove(String string, CommandContext<ServerCommandSource> ctx, boolean itemorname){
        if (itemorname){
            config.itemsSavedList.remove(string);
            ctx.getSource().sendFeedback(new LiteralText("removed item " + string + " from the item save list"), true);
        }
        else{
            config.namesSavedList.remove(string);
            ctx.getSource().sendFeedback(new LiteralText("removed name " + string + " from the item save list"), true);
        }
        manager.save();
    }

    public static void add(String string, CommandContext<ServerCommandSource> ctx, boolean itemOrName){
        if (itemOrName ){
            if (!config.itemsSavedList.contains(string)){
                config.itemsSavedList.add(string);
                ctx.getSource().sendFeedback(new LiteralText("added item " + string + " to the item save list"), true);
            }
            else{
                ctx.getSource().sendFeedback(new LiteralText("the item list already contains said item!").formatted(Formatting.RED), false);
            }
        }
        else{
            if (!config.namesSavedList.contains(string)){
                config.namesSavedList.add(string);
                ctx.getSource().sendFeedback(new LiteralText("added name " + string + " to the name save list"), true);
            }
            else{
                ctx.getSource().sendFeedback(new LiteralText("the name list already contains said name!").formatted(Formatting.RED), false);
            }
        }
        manager.save();
    }


}