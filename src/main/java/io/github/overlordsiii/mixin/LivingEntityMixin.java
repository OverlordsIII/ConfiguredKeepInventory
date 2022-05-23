package io.github.overlordsiii.mixin;

import io.github.overlordsiii.ConfiguredKeepInventory;
import io.github.overlordsiii.config.InventoryConfig;
import io.github.overlordsiii.mixinterfaces.PlayerInventoryExt;
import io.github.overlordsiii.util.CommandSourceUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected abstract void dropXp();

    public InventoryConfig Config = ConfiguredKeepInventory.Config;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "tryUseTotem", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"), index = 3)
    private ItemStack modifyStackCall(ItemStack stack){
        if ((LivingEntity)(Object)this instanceof ServerPlayerEntity && ConfiguredKeepInventory.Config.inventoryTotems && Config.enableConfig){
            ServerPlayerEntity entity = (ServerPlayerEntity)(Object)this;
           if (entity.getInventory().contains(new ItemStack(Items.TOTEM_OF_UNDYING))){
              int slot = ((PlayerInventoryExt)entity.getInventory()).indexOf(new ItemStack(Items.TOTEM_OF_UNDYING));
              if (Config.helpFullDeathMessages) {
                  String suggestedCommand;
                  if (!entity.getWorld().getDimension().equals(Objects.requireNonNull(entity.server.getWorld(entity.getSpawnPointDimension())).getDimension())){
                      suggestedCommand = "/execute in " + entity.getWorld().getRegistryKey().getValue() + " run teleport " + this.getX() + " " + this.getY() + " " + this.getZ();
                  }
                  else{
                      suggestedCommand = "/tp " + this.getX() + " " + this.getY() + " " + this.getZ();
                  }
                  if (!Config.needsOP) {
                      entity.world.getServer().getPlayerManager().broadcast(
                              new LiteralText(entity.getName().asString() + " has used a totem!")
                                      .formatted(Formatting.YELLOW)
                                      .styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT
                                              , new LiteralText(ConfiguredKeepInventory.Config.helpFullDeathMessage)
                                              .formatted(Formatting.AQUA)))
                                              .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND
                                                      , suggestedCommand))),
                              MessageType.SYSTEM,
                              entity.getUuid());
                  }
                  else{
                      entity.server.getPlayerManager().broadcast(new LiteralText(entity.getName().asString() + " has used a totem!"), MessageType.SYSTEM, entity.getUuid());
                      CommandSourceUtil
                              .sendToOps(this.world.getServer().getCommandSource(), (LiteralText) new LiteralText(Config.helpFullDeathMessage)
                                      .formatted(Formatting.YELLOW).styled(style -> style
                                              .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, suggestedCommand))));
                  }
              }
              if (slot != -1){
                 return entity.getInventory().main.get(slot);
              }
              else{
                  return entity.getInventory().offHand.get(0);
              }
           }
        }
        return stack;
    }

    @ModifyVariable(method = "tryUseTotem", at = @At(value = "HEAD", ordinal = 0), index = 1, argsOnly = true)
    private DamageSource print(DamageSource source){
        if (source.isOutOfWorld() && Config.debugTotems && Config.enableConfig){
            return ConfiguredKeepInventory.TOTEM_REPLACEMENT;
        }
        return source;
    }

}
