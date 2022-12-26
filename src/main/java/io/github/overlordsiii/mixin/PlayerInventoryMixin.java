package io.github.overlordsiii.mixin;


import io.github.overlordsiii.mixinterfaces.PlayerInventoryExt;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.overlordsiii.ConfiguredKeepInventory.Config;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements PlayerInventoryExt {

    public int cycle = 0;
    @Shadow @Final public PlayerEntity player;

    @Shadow @Final public DefaultedList<ItemStack> offHand;


    @Shadow @Final public DefaultedList<ItemStack> main;

    @Shadow @Final public DefaultedList<ItemStack> armor;

    /**
     * method that sorts a specific inventory
     * @param stacks the inventory that you want to sort
     */
    @Unique
    @Override
    public void configureDrop(DefaultedList<ItemStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            List<String> namesSavedList = Config.namesSavedList;
            List<String> itemSaveList = Config.itemsSavedList;
            boolean bl = !namesSavedList.contains(stack.getName().getString()) && !itemSaveList.contains(stack.getItem().toString());
            double percent = Config.configdroprate / 100.0;
            double newStackCount = (percent * stack.getCount());
            if (!stack.isEmpty()) {
                if (EnchantmentHelper.hasVanishingCurse(stack)) {
                    stacks.set(i, ItemStack.EMPTY);
                } else if (bl) {
                    if (stack.getCount() == 1) {
                        if (Config.configdroprate > 50) {
                            this.player.dropItem(stack, false);
                            stacks.set(i, ItemStack.EMPTY);
                        }
                    } else {
                        ItemStack copyStack;
                        copyStack = stack.copy();
                        if (Config.roundUp) {
                           copyStack.setCount((int)Math.round(newStackCount));
                            stack.decrement(((int) Math.round(newStackCount)));
                        }
                        else{
                            copyStack.setCount((int)newStackCount);
                            stack.decrement((int)newStackCount);
                        }
                        ItemEntity entity = this.player.dropItem(copyStack, false);
                        stacks.set(i, stack);
                    }
                }
            }
        }
    }
    //coming soon
    @Unique
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
                else {
                    int slot = this.indexOfArmor(stack);
                    if (slot != -1) {
                        ItemStack currentOffHandStack = this.offHand.get(0);
                        ItemStack wantedStack = this.armor.get(slot);
                        this.armor.set(slot, currentOffHandStack);
                        this.offHand.set(0, wantedStack);
                    }
                }
            }
        }

    /**
     * a method that searches through a pregenerated list based on the cycle num
     */
    @Unique
    @Override
    public void sortOffHand() {

    }
    @Unique
    @Override
    public void sortHotBar(DefaultedList<ItemStack> stack) {

    }

    /**
     *  this only searches on the main inventory
     * @param stack wanted stack
     * @return index of stack in the main slot
     */
    @Unique
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
    @Unique
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

    /**
     * Searches through the main inventory and returns the slot number of a food item
     * @return the int of the slot that the stack that is a food is in
     */
    @Unique
    @Override
    public int indexOfFood() {
        for (int i = 0; i < this.main.size(); i++){
            ItemStack stack = this.main.get(i);
           Item item = stack.getItem();
           if (item.isFood()){
               return i;
           }
        }

        return -1;
    }

    /**
     * Attempt to use less invasive mixins
     */
    @Unique
    @Override
    public void dropInventory() {
        if (Config.enableConfig) {
            this.configureDrop(this.main);
            this.configureDrop(this.armor);
            this.configureDrop(this.offHand);
        }
    }
}

