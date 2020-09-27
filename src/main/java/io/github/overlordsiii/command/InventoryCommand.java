package io.github.overlordsiii.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.overlordsiii.ConfiguredKeepInventory;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;




public class InventoryCommand

{


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("inventory")
            .requires(source -> source.hasPermissionLevel(2))
                .then(literal("set")
                    .then(argument("percentage", integer(0, 100))
                        .executes(ctx -> execute(ctx, getInteger(ctx, "percentage")))))
                .then(literal("on")
                    .executes(context -> executeOn(context, true)))
                .then(literal("off")
                    .executes(context -> executeOn(context, false)))
                .then(literal("get")
                    .executes(InventoryCommand::executeGet))
                .then (literal("add")
                    .then(literal("item")
                        .then(argument("item", ItemStackArgumentType.itemStack())
                            .executes(context -> executeAddItem(context, ItemStackArgumentType.getItemStackArgument(context, "item").getItem()))))
                    .then(literal("name"))
                        .then(argument("name", StringArgumentType.string())
                            .executes(context -> executeAddName(context, StringArgumentType.getString(context, "name")))))
                .then(literal("remove")
                        .then(literal("item")
                            .then(argument("item", ItemStackArgumentType.itemStack())
                                .executes(context -> executeRemoveItem(context, ItemStackArgumentType.getItemStackArgument(context, "item").getItem())))))
                        .then(literal("name")
                            .then(argument("name", StringArgumentType.string())
                                .executes(context -> executeRemoveName(context, StringArgumentType.getString(context, "name"))))));
    }
    public static int execute(CommandContext<ServerCommandSource> ctx, int value) {
        ConfiguredKeepInventory.Config.configdroprate = value;
        ConfiguredKeepInventory.manager.save();
        ctx.getSource().sendFeedback(new TranslatableText("inventory.set", value), true);
        return 1;
    }
    public static int executeOn(CommandContext<ServerCommandSource> ctx, boolean onoroff){
        if (onoroff){
            ConfiguredKeepInventory.Config.enableConfig = true;
            ConfiguredKeepInventory.manager.save();
        }
        else{
            ConfiguredKeepInventory.Config.enableConfig = false;
            ConfiguredKeepInventory.manager.save();
        }
        return 1;
    }
    public static int executeGet(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ctx.getSource().getPlayer().sendMessage(new TranslatableText("droprate.show", ConfiguredKeepInventory.Config.configdroprate), false);
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
            ConfiguredKeepInventory.Config.itemsSavedList.remove(string);
            ctx.getSource().sendFeedback(new TranslatableText("removed.item"), true);
        }
        else{
            ConfiguredKeepInventory.Config.namesSavedList.remove(string);
            ctx.getSource().sendFeedback(new TranslatableText("removed.name"), true);
        }
        ConfiguredKeepInventory.manager.save();
    }

    public static void add(String string, CommandContext<ServerCommandSource> ctx, boolean itemOrName){
        if (itemOrName ){
            if (!ConfiguredKeepInventory.Config.itemsSavedList.contains(string)){
                ConfiguredKeepInventory.Config.itemsSavedList.add(string);
                ctx.getSource().sendFeedback(new TranslatableText("added.item", string), true);
            }
            else{
                ctx.getSource().sendFeedback(new TranslatableText("added.item.fail").formatted(Formatting.RED), false);
            }
        }
        else{
            if (!ConfiguredKeepInventory.Config.namesSavedList.contains(string)){
                ConfiguredKeepInventory.Config.namesSavedList.add(string);
                ctx.getSource().sendFeedback(new TranslatableText("added.name", string), true);
            }
            else{
                ctx.getSource().sendFeedback(new TranslatableText("added.name.fail", string).formatted(Formatting.RED), false);
            }
        }
        ConfiguredKeepInventory.manager.save();
    }


}