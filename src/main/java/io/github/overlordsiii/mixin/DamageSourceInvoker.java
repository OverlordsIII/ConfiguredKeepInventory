package io.github.overlordsiii.mixin;

import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DamageSource.class)
public interface DamageSourceInvoker {
    @Invoker("<init>")
    static DamageSource createNewDamageSource(String name) {
        return null;
    }
}
