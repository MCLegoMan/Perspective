/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.zoom;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Inject(at = @At("HEAD"), method = "scrollInHotbar", cancellable = true)
    private void perspective$scrollInHotbar(double scrollAmount, CallbackInfo ci) {
        try {
            if (scrollAmount != 0 && Zoom.isZooming()) {
                Zoom.zoom(scrollAmount > 0, (int) ConfigHelper.getConfig("zoom_increment_size"));
                ci.cancel();
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to Mouse$onMouseButton: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
}