/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.information;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.PerspectiveLogo;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CreditsAttributionScreen extends Screen {
	private final Screen parentScreen;
	private float time;
	private List<OrderedText> credits;
	private final List<Integer> centeredLines = new ArrayList<>();
	private int creditsHeight;
	private float backgroundFade;
	private boolean isHoldingSpace;
	public CreditsAttributionScreen(Screen parentScreen) {
		super(NarratorManager.EMPTY);
		this.parentScreen = parentScreen;
		this.backgroundFade = 0.0F;
	}
	public void tick() {
		if (this.time > (this.creditsHeight + ClientData.CLIENT.getWindow().getScaledHeight() + 96)) this.close();
	}
	private float getSpeed() {
		return this.time > 0 ? ((isHoldingSpace ? 4.0F : 1.0F) * (hasControlDown() ? 4.0F : 1.0F)) : 1.0F;
	}
	public void close() {
		ClientData.CLIENT.setScreen(this.parentScreen);
	}
	protected void init() {
		if (this.credits == null) {
			this.credits = Lists.newArrayList();
			this.load(new Identifier("perspective", "texts/credits.json"), this::readCredits);
			this.creditsHeight = this.credits.size() * 12;
		}
	}
	private void load(Identifier id, CreditsAttributionReader reader1) {
		try {
			Reader reader2 = ClientData.CLIENT.getResourceManager().openAsReader(id);
			try {
				reader1.read(reader2);
			} catch (Exception error) {
				reader2.close();
				Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("An error occurred whilst trying to load credits! {}", error));
			}
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("An error occurred whilst trying to load credits! {}", error));
		}
	}
	private void readCredits(Reader reader1) {
		try {

			JsonObject credits = JsonHelper.deserialize(reader1);
			JsonArray sections = credits.getAsJsonArray("sections");
			for (JsonElement object1 : sections) {
				String title1 = JsonHelper.getString(object1.getAsJsonObject(), "title", "");
				if (!title1.equals("")) this.addText(Text.literal(title1).formatted(Formatting.GOLD), true);
				JsonArray subsections1 = JsonHelper.getArray(object1.getAsJsonObject(), "subsections", new JsonArray());
				for (JsonElement object2 : subsections1) {
					JsonObject subsection1 = object2.getAsJsonObject();
					String title2 = JsonHelper.getString(subsection1, "subtitle", "");
					if (!title2.equals(""))
						this.addText(Text.literal("").append(title2).formatted(Formatting.WHITE), false);
					JsonArray subsections2 = JsonHelper.getArray(subsection1, "subsections", new JsonArray());
					if (!subsections2.equals(new JsonArray())) {
						for (JsonElement object3 : subsections2) {
							JsonObject subsection2 = object3.getAsJsonObject();
							if (subsection2 != null) {
								String subtitle = JsonHelper.getString(subsection2, "subtitle", "");
								if (!subtitle.equals(""))
									this.addText(Text.literal("  ").append(subtitle).formatted(Formatting.GRAY), false);
								for (JsonElement subsection3 : JsonHelper.getArray(subsection2, "subsections", new JsonArray())) {
									this.addText(Text.literal("    ").append(subsection3.getAsString()).formatted(Formatting.GRAY), false);
								}
							}
						}
						this.addEmptyLine();
					}
				}
				this.addEmptyLine();
			}
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("An error occurred whilst trying to load credits! {}", error));
		}
	}
	private void addEmptyLine() {
		this.credits.add(OrderedText.EMPTY);
	}
	private void addText(Text text, boolean centered) {
		if (centered) {
			this.centeredLines.add(this.credits.size());
		}
		this.credits.addAll(ClientData.CLIENT.textRenderer.wrapLines(text, ClientData.CLIENT.getWindow().getScaledWidth() - ClientData.CLIENT.getWindow().getScaledWidth() / 2 - ClientData.CLIENT.getWindow().getScaledWidth() / 2 + 320));
	}
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.time = Math.max(0.0F, this.time + (delta * this.getSpeed()));
		super.render(context, mouseX, mouseY, delta);
		context.getMatrices().push();
		context.getMatrices().translate(0.0F, -this.time, 0.0F);
		context.drawTexture(PerspectiveLogo.getLogo(PerspectiveLogo.isPride() ? PerspectiveLogo.Logo.Type.PRIDE : PerspectiveLogo.Logo.Type.DEFAULT).getTexture(), ClientData.CLIENT.getWindow().getScaledWidth() / 2 - 128, ClientData.CLIENT.getWindow().getScaledHeight() + 2, 0.0F, 0.0F, 256, 44, 256, 64);
		int height = ClientData.CLIENT.getWindow().getScaledHeight() + 80;
		for(int l = 0; l < this.credits.size(); ++l) {
			if (l == this.credits.size() - 1) {
				float g = height - this.time - (float)(ClientData.CLIENT.getWindow().getScaledHeight() / 2 - 6);
				if (g < 0.0F) {
					context.getMatrices().translate(0.0F, -g, 0.0F);
				}
			}
			if (height - this.time + 12.0F + 8.0F > 0.0F && height - this.time < (float)ClientData.CLIENT.getWindow().getScaledHeight()) {
				OrderedText orderedText = this.credits.get(l);
				if (this.centeredLines.contains(l)) {
					context.drawCenteredTextWithShadow(this.textRenderer, orderedText, ClientData.CLIENT.getWindow().getScaledWidth() / 2, height, 16777215);
				} else {
					context.drawTextWithShadow(this.textRenderer, orderedText, ClientData.CLIENT.getWindow().getScaledWidth() / 2 - 160, height, 16777215);
				}
			}
			height += 12;
		}
		context.getMatrices().pop();
	}
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		if (this.time > (this.creditsHeight + ClientData.CLIENT.getWindow().getScaledHeight() + 48)) {
			float fadeStart = (this.creditsHeight + ClientData.CLIENT.getWindow().getScaledHeight() + 48);
			float fadeEnd = (this.creditsHeight + ClientData.CLIENT.getWindow().getScaledHeight() + 92);
			this.backgroundFade = MathHelper.lerp(((this.time - fadeStart) / (fadeEnd - fadeStart)) * 0.5F, this.backgroundFade, 0.0F);
		} else {
			this.backgroundFade = MathHelper.lerp((this.time / 48) * 0.25F, this.backgroundFade, 0.25F);
		}
		context.setShaderColor(this.backgroundFade, this.backgroundFade, this.backgroundFade, 1.0F);
		context.drawTexture(OPTIONS_BACKGROUND_TEXTURE, 0, 0, 0, 0.0F, this.time * 0.5F, ClientData.CLIENT.getWindow().getScaledWidth(), ClientData.CLIENT.getWindow().getScaledHeight(), 32, 32);
		context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_SPACE) {
			this.isHoldingSpace = true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_SPACE) {
			this.isHoldingSpace = false;
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	private interface CreditsAttributionReader {
		void read(Reader reader) throws IOException;
	}
}
