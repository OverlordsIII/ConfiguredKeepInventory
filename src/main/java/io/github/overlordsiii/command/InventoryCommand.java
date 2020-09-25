package io.github.overlordsiii.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.overlordsiii.ConfiguredKeepInventory;
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
                    .executes(context -> executeOn(true)))
                .then(literal("off")
                    .executes(context -> executeOn(false)))
                .then(literal("get")
                    .executes(InventoryCommand::executeGet))   );
    }
    public static int execute(CommandContext<ServerCommandSource> ctx, int value) throws CommandSyntaxException {
        ConfiguredKeepInventory.Config.configdroprate = value;
        ConfiguredKeepInventory.manager.save();
        ctx.getSource().sendFeedback(new TranslatableText("inventory.set", value), true);
        return 1;
    }
    public static int executeOn( boolean onoroff){
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
        ctx.getSource().getPlayer().sendMessage(new TranslatableText("droprate.show", ConfiguredKeepInventory.Config.configdroprate).formatted(Formatting.BLUE), false);
        return 1;
    }


}