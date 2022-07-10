package io.github.overlordsiii.util;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CommandSourceUtil {
    public static void sendToOps(ServerCommandSource ctx, Text text){
        ctx.getServer().getPlayerManager().getPlayerList().forEach((serverPlayerEntity -> {
            if (ctx.getServer().getPlayerManager().isOperator(serverPlayerEntity.getGameProfile())){
                serverPlayerEntity.sendMessage(text, false);
            }
        }));
    }
}
