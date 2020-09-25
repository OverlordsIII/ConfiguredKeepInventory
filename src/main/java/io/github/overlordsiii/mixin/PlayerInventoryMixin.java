package io.github.overlordsiii.mixin;



import io.github.overlordsiii.ConfiguredKeepInventory;
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
public abstract class PlayerInventoryMixin {


    @Shadow @Final private List<DefaultedList<ItemStack>> combinedInventory;

    @Shadow @Final public PlayerEntity player;

    @Redirect(method = "dropAll", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"))
    private boolean droredirect(Iterator iterator){
        //redirect for vanilla behavior
        return iterator.hasNext() && !ConfiguredKeepInventory.Config.enableConfig;
    }
    @Inject(method = "dropAll", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void capture(CallbackInfo ci, Iterator<List<ItemStack>> var1){
        //ad custom behavior if mod is enabled
        if (var1.hasNext() && ConfiguredKeepInventory.Config.enableConfig){
            //iterate to next inventory type
            List<ItemStack> list = var1.next();
            //iterate over whole inventory
            for(int i = 0; i < list.size(); ++i) {
                ItemStack stack = list.get(i);
                //if stack is not empty
                if (!stack.isEmpty()) {
                    System.out.println(this.combinedInventory);
                    //find the percent for which we need to multiply
                    double percent = ConfiguredKeepInventory.Config.configdroprate / 100.0;
                    System.out.println("decimal percent = " + percent + " percent = " + ConfiguredKeepInventory.Config.configdroprate);
                    //get the stack count
                    double newStackCount = (percent * stack.getCount());
                    System.out.println("new stack Count = " + newStackCount + " old stack count = " + stack.getCount());
                    //decrement the original stack by the droprate found in the config
                    stack.decrement(((int) Math.round(newStackCount)));
                    System.out.println("stack count after decrement = " + stack.getCount());
                    //create a copy stack to house the items we want to drop
                    ItemStack copyStack = new ItemStack(stack.getItem());
                    //set the copy stack count to the amount we decremented above
                    copyStack.setCount((int) Math.round(newStackCount));
                    System.out.println("copy stack = " + copyStack);
                    //drop the copystack at the right spot
                    this.player.dropItem(copyStack, true);
                    //validate that the inventory has recived the decrement changes to it
                    list.set(i, stack);
            }
        }
    }
    }
}
