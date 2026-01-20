package pl.olafcio.deathscreenframes.mixins;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.olafcio.deathscreenframes.Main;

import java.awt.*;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component component) {
        super(component);
    }

    @Inject(at = @At("TAIL"), method = "render")
    public void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (!Main.loaded)
            guiGraphics.drawString(
                    this.font, "DeathScreenFrames failed to load!",
                    2, this.height - 10 - font.lineHeight,
                    Color.WHITE.getRGB()
            );
    }
}
