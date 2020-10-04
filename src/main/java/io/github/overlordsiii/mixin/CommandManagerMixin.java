package io.github.overlordsiii.mixin;

import com.mojang.brigadier.CommandDispatcher;
import io.github.overlordsiii.ConfiguredKeepInventory;
import io.github.overlordsiii.command.InventoryCommand;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {
    @Final
    @Shadow
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void register(CommandManager.RegistrationEnvironment environment, CallbackInfo ci) {
        //add our commands to register list
        if (ConfiguredKeepInventory.Config.commandUsage && ConfiguredKeepInventory.Config.enableConfig) {
            InventoryCommand.register(dispatcher);
        }

    }
}
