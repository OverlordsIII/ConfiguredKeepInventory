package io.github.overlordsiii.mixin;


import io.github.overlordsiii.mixinterfaces.PlayerInventoryExt;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.overlordsiii.ConfiguredKeepInventory.Config;

@Mixin(value = PlayerEntity.class, priority = 999999)
public abstract class PlayerEntityMixin extends LivingEntity {
    //Coming soon

    @Shadow
    @Final
    public PlayerInventory inventory;
    @Shadow protected HungerManager hungerManager;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract ItemStack eatFood(World world, ItemStack stack);
/*
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
            setStack = this.inventory.offHand.get(0);
            System.out.println("The set Stack = " + setStack);
            ((PlayerInventoryExt)this.inventory).sortOffHand(new ItemStack(Items.FIREWORK_ROCKET));
        }
    }
    @Inject(method = "checkFallFlying", at = @At("TAIL"))
    private void reset(CallbackInfoReturnable<Boolean> cir){
        if (this.setStack != null && (((PlayerInventoryExt)inventory).indexOf(setStack) != -1 || setStack.isEmpty()) && Config.elytraRockets) {
            int slot = ((PlayerInventoryExt)inventory).indexOf(setStack);

            ItemStack stack = this.inventory.main.get(slot);
            ItemStack fireworks = this.inventory.offHand.get(0);
            if (!fireworks.isEmpty() && !stack.isEmpty()){
                this.inventory.main.set(slot, fireworks);
                this.inventory.offHand.set(slot, stack);
            }

            if (setStack.isEmpty() && !(setStack.getItem() instanceof FireworkItem)) {
               int slot = this.inventory.getEmptySlot();
               ItemStack stack = this.inventory.offHand.get(0);
               this.inventory.main.set(slot, stack);
               this.inventory.offHand.set(0, ItemStack.EMPTY);
            } else {
                ((PlayerInventoryExt) this.inventory).sortOffHand(setStack);
            }
        }
        else{
            ConfiguredKeepInventory.LOGGER.error("The saved elytra stack was not set correctly or was not found in the target inventory. Stack = " + setStack);
            ConfiguredKeepInventory.LOGGER.error("This is a problem with the mod");
        }
    }
 */
    @Inject(method = "tick", at = @At("TAIL"))
    private void checkHunger(CallbackInfo ci){
     //   System.out.println(this.hungerManager.getFoodLevel());
        if(this.hungerManager.getFoodLevel() <= Config.hungerRefreshLimit && Config.hungerReplenish && Config.enableConfig && this.hungerManager.isNotFull()) {
            for (int i = 0; i < 40; i++) {
                int slot = ((PlayerInventoryExt) this.inventory).indexOfFood();
                if (slot != -1) {
                    ItemStack stack = this.inventory.main.get(slot);
                    this.eatFood(this.world, stack);
                    this.spawnConsumptionEffects(stack, 5);
                }
                else if (this.inventory.offHand.get(0).getItem().isFood()){
                    ItemStack stack = this.inventory.offHand.get(0);
                    this.eatFood(this.world, stack);
                    this.spawnConsumptionEffects(stack, 5);
                }
                if (!this.hungerManager.isNotFull()) {
                    break;
                }
            }
        }
    }

    @Redirect(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"))
    public void safeInv(PlayerInventory instance) {
        if (Config.enableConfig){
            ((PlayerInventoryExt)this.inventory).dropInventory();
        }
    }

/*
    //soft override better or worse than redirect?
    //still up for debate
    @Inject(method = "dropInventory", at = @At("HEAD"), cancellable = true)
    public void safeInv(CallbackInfo ci){
        if (Config.enableConfig){
            ((PlayerInventoryExt)this.inventory).dropInventory();
            ci.cancel();
        }

 */
    }







