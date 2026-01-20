package pl.olafcio.deathscreenframes.mixins;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.deathscreenframes.Main;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Unique
    private static final long COMBAT_TIME = 1000 * 30;

    @Unique
    private static final boolean DEV_MODE = true;

    @Inject(at = @At("HEAD"), method = "handleDamageEvent")
    public void handleDamageEvent(DamageSource damageSource, CallbackInfo ci) {
        if ((Object) this instanceof LocalPlayer) {
            var cause = damageSource.getEntity();
            if (DEV_MODE ? (cause != null) : (cause instanceof Player)) {
                Main.attackersReduce();
                Main.attackers.put(cause, System.currentTimeMillis() + COMBAT_TIME);
            }
        } else if (
                damageSource.getEntity() instanceof LocalPlayer &&
                (Object) this instanceof RemotePlayer // TODO: Is this condition reachable?
        ) {
            Main.attackersReduce();
            Main.attackers.put((Entity) (Object) this, System.currentTimeMillis() + COMBAT_TIME);
        }
    }

    @Inject(at = @At("HEAD"), method = "die")
    public void die(DamageSource damageSource, CallbackInfo ci) {
        Main.attackers.remove((Entity) (Object) this);
    }
}
