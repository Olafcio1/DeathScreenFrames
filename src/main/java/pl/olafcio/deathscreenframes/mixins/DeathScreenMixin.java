package pl.olafcio.deathscreenframes.mixins;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
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

    @Unique
    private boolean on;

    @Inject(at = @At("TAIL"), method = "init")
    protected void init(CallbackInfo ci) {
        Main.attackersReduce();

        time = 0;
        on = !Main.attackers.isEmpty();

        Main.attackers.clear();
    }

    @Inject(at = @At("HEAD"), method = "render")
    public void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (!on)
            return;

        var diff = time++ - Main.frames.size();
        if (diff < -1)
            guiGraphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    Main.frames.get((int) time),
                    0, 0,
                    0, 0,
                    width, height,
                    1920, 1080,
                    1920, 1080
            );

        this.setAlpha(Math.clamp(((float)diff - 10F) / 60F, 0, 1));
    }

    @Unique
    private void setAlpha(float alpha) {
        for (GuiEventListener guiEventListener : this.children())
            if (guiEventListener instanceof AbstractWidget abstractWidget)
                abstractWidget.setAlpha(alpha);
    }
}
