package io.github.overlordsiii.mixin;


import io.github.overlordsiii.ConfiguredKeepInventory;
import io.github.overlordsiii.mixinterfaces.PlayerInventoryExt;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements PlayerInventoryExt {

    public int cycle = 0;
    @Shadow @Final public PlayerEntity player;

    @Shadow @Final public DefaultedList<ItemStack> offHand;


    @Shadow @Final public DefaultedList<ItemStack> main;

    @Shadow @Final public DefaultedList<ItemStack> armor;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructList(PlayerEntity player, CallbackInfo ci){

    }
    @Redirect(method = "dropAll", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"))
    private boolean droredirect(Iterator iterator){
        //redirect for vanilla behavior
        return iterator.hasNext() && !ConfiguredKeepInventory.Config.enableConfig;
    }


    @Inject(method = "dropAll", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void capture(CallbackInfo ci, Iterator<List<ItemStack>> var1){
        //ad custom behavior if mod is enabled
        if (ConfiguredKeepInventory.Config.enableConfig){
                    this.configureDrop(this.main);
                    this.configureDrop(this.armor);
                    this.configureDrop(this.offHand);
                }
        }


    @Override
    public void configureDrop(DefaultedList<ItemStack> stacks) {
        for (int i = 0; i < stacks.size(); i++){
            ItemStack stack = stacks.get(i);
            double percent = ConfiguredKeepInventory.Config.configdroprate / 100.0;
            double newStackCount = (percent * stack.getCount());
            if (EnchantmentHelper.hasVanishingCurse(stack)) {
                stacks.set(i, ItemStack.EMPTY);
            }
            else if (stack.getCount() == 1){
                if (ConfiguredKeepInventory.Config.configdroprate > 50){
                    this.player.dropItem(stack, false);
                    stacks.set(i, ItemStack.EMPTY);
                }
            }
            else if (!ConfiguredKeepInventory.Config.namesSavedList.contains(stack.getName().asString()) && !ConfiguredKeepInventory.Config.itemsSavedList.contains(stack.getItem().toString())){
                stack.decrement(((int) Math.round(newStackCount)));
                ItemStack copyStack = new ItemStack(stack.getItem(), (int) Math.round(newStackCount));
                this.player.dropItem(copyStack, false);
                stacks.set(i, stack);
            }
        }
    }
    //coming soon
    /**
     * A method that sorts your offhand based on the stack.
     * @param stack that you want sorted
     */


    @Override
    public void sortOffHand(ItemStack stack) {
            if (stack != null){
                if (this.indexOf(stack) != -1){
                    int item = indexOf(stack);
                   ItemStack currentOffStack = this.offHand.get(0);
                   ItemStack wantedStack = this.main.get(item);
                   this.main.set(item, currentOffStack);
                   this.offHand.set(0, wantedStack);
                }
                else{
                   int slot = this.indexOfArmor(stack);
                   ItemStack currentOffHandStack = this.offHand.get(0);
                   ItemStack wantedStack = this.armor.get(slot);
                   this.armor.set(slot, currentOffHandStack);
                   this.offHand.set(0, wantedStack);
                }
            }
        }

    /**
     * a method that searches through a pregenerated list based on the cycle num
     */
    @Override
    public void sortOffHand() {

    }

    @Override

    public void sortHotBar(DefaultedList<ItemStack> stack) {

    }

    /**
     *  this only searches on the main inventory
     * @param stack wanted stack
     * @return index of stack in the main slot
     */

    @Override

    public int indexOf(ItemStack stack) {
        for (int i = 0; i < this.main.size(); i++){
            ItemStack itemStack = this.main.get(i);
            if (itemStack.isItemEqual(stack)){
                return i;
            }
        }
        return -1;
    }

    /**
     * only searches armor inventory
     * @param stack wanted stack
     * @return index of the stack in armor slots
    */

    @Override
    public int indexOfArmor(ItemStack stack){
        for (int i = 0; i < this.armor.size(); i++){
            ItemStack stacker = this.armor.get(i);
            if (stacker.isItemEqual(stack)){
                return i;
            }
        }
        return -1;
    }


}

