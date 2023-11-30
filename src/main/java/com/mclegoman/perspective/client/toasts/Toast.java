/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.toasts;

import com.mclegoman.perspective.client.data.ClientData;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class Toast {
	public static net.minecraft.client.toast.Toast create(Text title, @Nullable Text description, SystemToast.Type type) {
		return SystemToast.create(ClientData.CLIENT, type, title, description);
	}
}
