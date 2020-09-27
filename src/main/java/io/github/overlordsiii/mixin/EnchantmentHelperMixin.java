package io.github.overlordsiii.mixin;

import io.github.overlordsiii.ConfiguredKeepInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {


    @Inject(method = "hasVanishingCurse", at = @At("HEAD"), cancellable = true)
    private static void vanishdisable(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if (ConfiguredKeepInventory.Config.enableConfig && ConfiguredKeepInventory.Config.disableVanishingCurse){
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "hasBindingCurse", at = @At("HEAD"), cancellable = true)
    private static void bindingdisable(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if (ConfiguredKeepInventory.Config.enableConfig && ConfiguredKeepInventory.Config.disableBindingCurse){
            cir.setReturnValue(false);
        }
    }
}
