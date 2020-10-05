package io.github.overlordsiii.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    /*
    @Redirect(method = "tryUseTotem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private Item redirectTotemCall(ItemStack stack){
        if ((LivingEntity)(Object)this instanceof PlayerEntity){
            PlayerEntity entity = (PlayerEntity)(Object)this;
           if (entity.inventory.contains(new ItemStack(Items.TOTEM_OF_UNDYING))){
               int slot = ((PlayerInventoryExt)entity.inventory).indexOf(new ItemStack(Items.TOTEM_OF_UNDYING));
               if (slot != -1) {
                   //if this starts to cause issues, decrement the stack instead, but we should be able to safely assume stack is index of a Totem of undying
                  entity.inventory.main.set(slot, ItemStack.EMPTY);
               }
               else{
                   //also check if this is a problem
                   entity.inventory.offHand.set(0, ItemStack.EMPTY);
               }
               return Items.TOTEM_OF_UNDYING;
           }
        }
        return stack.getItem();
    }
    @Redirect(method = "tryUseTotem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    private void redirect(ItemStack stack, int amount){
        String lol = "ha ha die trash, the whole point of this is to do nothing lol. Your stack is useless mc, how do you feel now." +
                " I get really mad when you jank up stuff like this that i have to mixin into, " +
                "like cmon now i have to redirect twice which prob breaks a lot mods, that is why ur bad";

    }
     */
}
