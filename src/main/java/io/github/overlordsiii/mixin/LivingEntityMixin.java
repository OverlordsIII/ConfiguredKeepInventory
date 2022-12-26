package io.github.overlordsiii.mixin;

import io.github.overlordsiii.ConfiguredKeepInventory;
import io.github.overlordsiii.config.InventoryConfig;
import io.github.overlordsiii.mixinterfaces.PlayerInventoryExt;
import io.github.overlordsiii.util.CommandSourceUtil;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected abstract void dropXp();

    public InventoryConfig Config = ConfiguredKeepInventory.Config;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(method = "tryUseTotem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean redirectTotemCheck(ItemStack instance, Item item) {
        if ((LivingEntity)(Object)this instanceof ServerPlayerEntity entity && ConfiguredKeepInventory.Config.inventoryTotems && Config.enableConfig){
            if (entity.getInventory().contains(new ItemStack(Items.TOTEM_OF_UNDYING))){
                int slot = ((PlayerInventoryExt)entity.getInventory()).indexOf(new ItemStack(Items.TOTEM_OF_UNDYING));
                if (Config.helpFullDeathMessages) {
                    String suggestedCommand;
                    if (!entity.world.getDimension().equals(Objects.requireNonNull(entity.server.getWorld(entity.getSpawnPointDimension())).getDimension())){
                        suggestedCommand = "/execute in " + entity.world.getRegistryKey().getValue() + " run teleport " + this.getX() + " " + this.getY() + " " + this.getZ();
                    }
                    else{
                        suggestedCommand = "/tp " + this.getX() + " " + this.getY() + " " + this.getZ();
                    }
                    if (!Config.needsOP) {
                        entity.world.getServer().getPlayerManager().broadcast(
                                Text.literal(entity.getName().getString() + " has used a totem!")
                                        .formatted(Formatting.YELLOW)
                                        .styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT
                                                , Text.literal(ConfiguredKeepInventory.Config.helpFullDeathMessage)
                                                .formatted(Formatting.AQUA)))
                                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND
                                                        , suggestedCommand))),
                                false);
                    }
                    else{
                        entity.server.getPlayerManager().broadcast(Text.literal(entity.getName().getString() + " has used a totem!"), false);
                        CommandSourceUtil
                                .sendToOps(this.world.getServer().getCommandSource(), Text.literal(Config.helpFullDeathMessage)
                                        .formatted(Formatting.YELLOW).styled(style -> style
                                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, suggestedCommand))));
                    }
                }
                //if totem
                if (slot != -1){
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        return instance.isOf(item);
    }

    @ModifyVariable(method = "tryUseTotem", at = @At(value = "HEAD", ordinal = 0), index = 1, argsOnly = true)
    private DamageSource makeTotemWorkOnKillCommand(DamageSource source){
        if (source.isOutOfWorld() && Config.debugTotems && Config.enableConfig){
            return ConfiguredKeepInventory.TOTEM_REPLACEMENT;
        }
        return source;
    }

}
