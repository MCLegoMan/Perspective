/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.toasts;

import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class PerspectiveToast implements Toast {
	private static final Identifier TEXTURE = new Identifier(PerspectiveData.PERSPECTIVE_VERSION.getID(), "toast/warning");
	private final Text title;
	private final List<OrderedText> lines;
	private long startTime;
	private boolean justUpdated;
	private final int width;
	private final long display_time;
	public PerspectiveToast(Text title, Text description, int width, Type type) {
		this(title, PerspectiveClientData.CLIENT.textRenderer.wrapLines(description, width - 26), width, type.display_time);
	}

	private PerspectiveToast(Text title, List<OrderedText> lines, int width, long display_time) {
		this.title = title;
		this.lines = lines;
		this.width = width;
		this.display_time = display_time;
	}

	public int getWidth() {
		return PerspectiveClientData.CLIENT.textRenderer.getWidth(this.title) > this.width ? PerspectiveClientData.CLIENT.textRenderer.getWidth(this.title) + 32 : this.width;
	}
	public int getHeight() {
		return 20 + Math.max(this.lines.size(), 1) * 12;
	}
	public Toast.Visibility draw(DrawContext context, ToastManager manager, long startTime) {
		if (this.justUpdated) {
			this.startTime = startTime;
			this.justUpdated = false;
		}

		int i = this.getWidth();
		int j;
		if (i == 160 && this.lines.size() <= 1) {
			context.drawGuiTexture(TEXTURE, 0, 0, i, this.getHeight());
		} else {
			j = this.getHeight();
			int l = Math.min(4, j - 28);
			this.drawPart(context, i, 0, 0, 28);

			for(int m = 28; m < j - l; m += 10) {
				this.drawPart(context, i, 16, m, Math.min(16, j - m - l));
			}

			this.drawPart(context, i, 32 - l, j - l, l);
		}

		if (this.lines == null) {
			context.drawText(manager.getClient().textRenderer, this.title, 26, 12, 0xFFA239, false);
		} else {
			context.drawText(manager.getClient().textRenderer, this.title, 26, 7, 0xFFA239, false);

			for(j = 0; j < this.lines.size(); ++j) {
				context.drawText(manager.getClient().textRenderer, this.lines.get(j), 26, 18 + j * 12, 0xFFCC00, false);
			}
		}

		return (double)(startTime - this.startTime) < (double)this.display_time * manager.getNotificationDisplayTimeMultiplier() ? Visibility.SHOW : Visibility.HIDE;
	}
	private void drawPart(DrawContext context, int i, int j, int k, int l) {
		int m = j == 0 ? 20 : 5;
		int n = Math.min(60, i - m);
		context.drawGuiTexture(TEXTURE, 160, 32, 0, j, 0, k, m, l);

		for(int o = m; o < i - n; o += 64) {
			context.drawGuiTexture(TEXTURE, 160, 32, 32, j, o, k, Math.min(64, i - o - n), l);
		}

		context.drawGuiTexture(TEXTURE, 160, 32, 160 - n, j, i - n, k, n, l);
	}

	public enum Type {
		INFO(4000L),
		WARNING(6000L),
		TUTORIAL(8000L);

		final long display_time;

		Type(long display_time) {
			this.display_time = display_time;
		}
	}
}