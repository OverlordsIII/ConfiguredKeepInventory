package io.github.overlordsiii.mixin;



import com.mojang.authlib.GameProfile;
import io.github.overlordsiii.ConfiguredKeepInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Redirect(method = "copyFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
    private boolean redirect(GameRules gameRules, GameRules.Key<GameRules.BooleanRule> rule){
        return gameRules.getBoolean(GameRules.KEEP_INVENTORY) || ConfiguredKeepInventory.Config.enableConfig;
    }
    @Inject(method = "playerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;getStack(I)Lnet/minecraft/item/ItemStack;", shift = At.Shift.BEFORE))
    private void updateHotbar(CallbackInfo ci){
        System.out.println(this.inventory.selectedSlot);
    }

}
