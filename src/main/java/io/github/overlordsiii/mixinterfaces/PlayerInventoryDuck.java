package io.github.overlordsiii.mixinterfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface PlayerInventoryDuck {
    //TODO add sorting in 1.1
    void sortOffHand(ItemStack stack);
    void sortHotBar(DefaultedList<ItemStack> stack);
}
