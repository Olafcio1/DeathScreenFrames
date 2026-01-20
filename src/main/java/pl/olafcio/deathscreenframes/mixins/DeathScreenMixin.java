package pl.olafcio.deathscreenframes.mixins;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.deathscreenframes.Main;

@Mixin(DeathScreen.class)
public class DeathScreenMixin extends Screen {
    protected DeathScreenMixin(Component component) {
        super(component);
    }

    @Unique
    private long time;

    @Inject(at = @At("TAIL"), method = "init")
    protected void init(CallbackInfo ci) {
        time = 0;
    }

    @Inject(at = @At("HEAD"), method = "render")
    public void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        Main.attackersReduce();
        if (Main.attackers.isEmpty())
            return;

        var diff = time++ - Main.frames.size();
        if (time < 0)
            guiGraphics.blit(
                    Main.frames.get((int) time),
                    0, 0,
                    width, height,
                    0, 0,
                    0, 0
            );

        this.setAlpha(Math.clamp(((float) diff) / 80F, 0, 1));
    }

    @Unique
    private void setAlpha(float alpha) {
        for (GuiEventListener guiEventListener : this.children())
            if (guiEventListener instanceof AbstractWidget abstractWidget)
                abstractWidget.setAlpha(alpha);
    }
}
