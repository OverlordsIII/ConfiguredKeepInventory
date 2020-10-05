package io.github.overlordsiii.mixin;


import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    //Coming soon
/*
    @Shadow
    @Final
    public PlayerInventory inventory;
    private ItemStack setStack;
    @Inject(method = "checkFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;startFallFlying()V"))
    private void insert(CallbackInfoReturnable<Boolean> cir) {
        if (Config.elytraRockets) {
            ItemStack stack = this.inventory.offHand.get(0);
            if (!stack.getItem().equals(Items.FIREWORK_ROCKET)) {
                int slot = ((PlayerInventoryExt) inventory).indexOf(new ItemStack(Items.FIREWORK_ROCKET));
                if (slot != -1) {
                    ItemStack fireworks = this.inventory.main.get(slot);
                    this.inventory.main.set(slot, stack);
                    setStack = stack.copy();
                    this.inventory.offHand.set(0, fireworks);
                }
            }
        }
    }
    @Inject(method = "checkFallFlying", at = @At("TAIL"))
    private void reset(CallbackInfoReturnable<Boolean> cir){
        if (this.setStack != null && (((PlayerInventoryExt)inventory).indexOf(setStack) != -1 || setStack.isEmpty()) && Config.elytraRockets){
            int slot = ((PlayerInventoryExt)inventory).indexOf(setStack);

            ItemStack stack = this.inventory.main.get(slot);
            ItemStack fireworks = this.inventory.offHand.get(0);
            if (!fireworks.isEmpty() && !stack.isEmpty()){
                this.inventory.main.set(slot, fireworks);
                this.inventory.offHand.set(slot, stack);
            }
        }
        else{
            LOGGER.error("The saved elytra stack was not set correctly or was not found in the target inventory. Stack = " + setStack);
            LOGGER.error("This is a problem with the mod");
        }
    }


 */

}
