package io.github.overlordsiii.mixin;



import io.github.overlordsiii.ConfiguredKeepInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Redirect(method = "copyFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
    private boolean redirect(GameRules gameRules, GameRules.Key<GameRules.BooleanRule> rule){
        return gameRules.getBoolean(GameRules.KEEP_INVENTORY) || ConfiguredKeepInventory.Config.enableConfig;
    }
}
