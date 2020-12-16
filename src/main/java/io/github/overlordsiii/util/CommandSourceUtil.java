package io.github.overlordsiii.util;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class CommandSourceUtil {
    public static void sendToOps(ServerCommandSource ctx, LiteralText text){
        ctx.getMinecraftServer().getPlayerManager().getPlayerList().forEach((serverPlayerEntity -> {
            if (ctx.getMinecraftServer().getPlayerManager().isOperator(serverPlayerEntity.getGameProfile())){
                serverPlayerEntity.sendMessage(text, false);
            }
        }));
    }
}
