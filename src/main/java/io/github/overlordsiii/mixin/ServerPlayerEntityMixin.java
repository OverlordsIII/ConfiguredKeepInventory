package io.github.overlordsiii.mixin;


import com.mojang.authlib.GameProfile;
import io.github.overlordsiii.ConfiguredKeepInventory;
import io.github.overlordsiii.util.CommandSourceUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    @Shadow public abstract ServerWorld getServerWorld();

    @Shadow public abstract RegistryKey<World> getSpawnPointDimension();

    @Shadow @Final public MinecraftServer server;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Redirect(method = "copyFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
    private boolean redirect(GameRules gameRules, GameRules.Key<GameRules.BooleanRule> rule){
        return gameRules.getBoolean(GameRules.KEEP_INVENTORY) || ConfiguredKeepInventory.Config.enableConfig;
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void changeXP(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if (ConfiguredKeepInventory.Config.enableConfig && ConfiguredKeepInventory.Config.loseXpOnDeath && !this.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            this.experienceLevel = 0;
            this.experienceProgress = 0;
            this.totalExperience = 0;
        }
    }


    @Inject(method = "onDeath", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/damage/DamageTracker;getDeathMessage()Lnet/minecraft/text/Text;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void helpfuldeathMessages(DamageSource arg0, CallbackInfo ci, boolean bl, Text text){
        if (text instanceof TranslatableText && ConfiguredKeepInventory.Config.helpFullDeathMessages) {
            String suggestedCommand;
            if (!this.getServerWorld().getDimension().equals(Objects.requireNonNull(this.server.getWorld(getSpawnPointDimension())).getDimension())) {
                suggestedCommand = "/execute in " + this.getServerWorld().getRegistryKey().getValue() + " run teleport " + this.getX() + " " + this.getY() + " " + this.getZ();
            } else {
                suggestedCommand = "/tp " + this.getX() + " " + this.getY() + " " + this.getZ();
            }
            TranslatableText translatableText = (TranslatableText) text;
            if (!ConfiguredKeepInventory.Config.needsOP) {
              translatableText
                        .styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT
                                , new LiteralText(ConfiguredKeepInventory.Config.helpFullDeathMessage)))
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND
                                        , suggestedCommand)));
            }
            else{
                CommandSourceUtil
                        .sendToOps(this.server.getCommandSource(), (LiteralText) new LiteralText(ConfiguredKeepInventory.Config.helpFullDeathMessage)
                                .styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, suggestedCommand))));
            }
        }

    }


}
