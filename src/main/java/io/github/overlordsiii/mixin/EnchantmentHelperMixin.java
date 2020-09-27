package io.github.overlordsiii.mixin;

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

    }
    @Inject(method = "hasBindingCurse", at = @At("HEAD"), cancellable = true)
    private static void bindingdisable(ItemStack stack, CallbackInfoReturnable<Boolean> cir){

    }
}
