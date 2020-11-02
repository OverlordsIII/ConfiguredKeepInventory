package io.github.overlordsiii.mixin;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerCommandSource.class)
public interface ServerCommandSourceInvoker {
    @Invoker("sendToOps")
    public void sendToOps(Text message);
}
