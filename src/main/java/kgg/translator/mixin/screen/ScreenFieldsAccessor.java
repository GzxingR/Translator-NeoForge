package kgg.translator.mixin.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Screen.class)
public interface ScreenFieldsAccessor {
    @Accessor("width")
    int translator$getWidth();

    @Accessor("font")
    Font translator$getFont();
}
