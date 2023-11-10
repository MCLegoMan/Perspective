/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.toasts;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class Toast implements net.minecraft.client.toast.Toast {
	private final Text title;
	private final List<OrderedText> lines;
	private long startTime;
	private boolean justUpdated;
	private final int width;
	private final long display_time;
	private final int title_color;
	private final int description_color;
	private final Identifier texture;
	public Toast(Text title, Text description, int width, Type type) {
		this(title, ClientData.CLIENT.textRenderer.wrapLines(description, width - 26), width, type);
	}

	private Toast(Text title, List<OrderedText> lines, int width, Type type) {
		this.title = title;
		this.lines = lines;
		this.width = width;
		this.display_time = type.display_time;
		this.title_color = type.title_color;
		this.description_color = type.description_color;
		this.texture = type.texture;
	}

	public int getWidth() {
		return ClientData.CLIENT.textRenderer.getWidth(this.title) > this.width ? ClientData.CLIENT.textRenderer.getWidth(this.title) + 32 : this.width;
	}
	public int getHeight() {
		return 20 + Math.max(this.lines.size(), 1) * 12;
	}
	public net.minecraft.client.toast.Toast.Visibility draw(DrawContext context, ToastManager manager, long startTime) {
		if (this.justUpdated) {
			this.startTime = startTime;
			this.justUpdated = false;
		}

		int i = this.getWidth();
		int j;
		if (i == 160 && this.lines.size() <= 1) {
			context.drawGuiTexture(this.texture, 0, 0, i, this.getHeight());
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
			context.drawText(manager.getClient().textRenderer, this.title, 26, 12, this.title_color, false);
		} else {
			context.drawText(manager.getClient().textRenderer, this.title, 26, 7, this.title_color, false);

			for(j = 0; j < this.lines.size(); ++j) {
				context.drawText(manager.getClient().textRenderer, this.lines.get(j), 26, 18 + j * 12, this.description_color, false);
			}
		}

		return (double)(startTime - this.startTime) < (double)this.display_time * manager.getNotificationDisplayTimeMultiplier() ? Visibility.SHOW : Visibility.HIDE;
	}
	private void drawPart(DrawContext context, int i, int j, int k, int l) {
		int m = j == 0 ? 20 : 5;
		int n = Math.min(60, i - m);
		context.drawGuiTexture(this.texture, 160, 32, 0, j, 0, k, m, l);

		for(int o = m; o < i - n; o += 64) {
			context.drawGuiTexture(this.texture, 160, 32, 32, j, o, k, Math.min(64, i - o - n), l);
		}

		context.drawGuiTexture(this.texture, 160, 32, 160 - n, j, i - n, k, n, l);
	}

	public enum Type {
		INFO(0xFFAA00, 0x00AAAA, 4000L, new Identifier(Data.PERSPECTIVE_VERSION.getID(), "toast/warning")),
		WARNING(0xFFAA00, 0x00AAAA, 8000L, new Identifier(Data.PERSPECTIVE_VERSION.getID(), "toast/warning")),
		TUTORIAL(0xFFAA00, 0x00AAAA, 8000L, new Identifier(Data.PERSPECTIVE_VERSION.getID(), "toast/warning"));

		final int title_color;
		final int description_color;
		final long display_time;
		final Identifier texture;

		Type(int title_color, int description_color, long display_time, Identifier texture) {
			this.title_color = title_color;
			this.description_color = description_color;
			this.display_time = display_time;
			this.texture = texture;
		}
	}
}