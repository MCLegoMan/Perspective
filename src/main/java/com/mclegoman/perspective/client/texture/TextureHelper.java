/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.texture;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.mixin.client.texture.NativeImageAccessor;
import com.mclegoman.perspective.mixin.client.texture.SpriteContentsAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.lwjgl.system.MemoryUtil;

import java.util.List;

public class TextureHelper {
	public static void init() {
		try {
			ResourcePacks.init();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to init texture helper: {}", error));
		}
	}
	public static Identifier getTexture(Identifier texture, Identifier current) {
		return texture.getPath().equals("none") ? current : Identifier.of(texture.getNamespace(), texture.getPath().endsWith(".png") ? texture.getPath() : texture.getPath() + ".png");
	}
	// TODO: Move the following functions into Luminance as they could be useful in other mods.
	public static double[] getAverageColorFromBlock(World world, BlockPos pos, BlockState state) {
		BakedModel model = ClientData.minecraft.getBlockRenderManager().getModel(state);
		List<BakedQuad> bakedQuads = model.getQuads(state, Direction.DOWN, world.getRandom());
		Sprite sprite;
		boolean useBlockColor;
		if (!bakedQuads.isEmpty()) {
			BakedQuad quad = bakedQuads.getFirst();
			sprite = quad.getSprite();
			useBlockColor = quad.hasColor();
		} else {
			sprite = model.getParticleSprite();
			useBlockColor = true;
		}
		double[] color = getAverageColorFromNativeImage(((SpriteContentsAccessor)sprite.getContents()).getMipmapLevelsImages()[0]);
		int blockColor = (useBlockColor ? ClientData.minecraft.getBlockColors().getColor(state, world, pos, 0) : -1);
		if (blockColor != -1) {
			color[0] *= (blockColor >> 16 & 255) / 255.0F;
			color[1] *= (blockColor >> 8 & 255) / 255.0F;
			color[2] *= (blockColor & 255) / 255.0F;
		}
		return color;
	}
	public static double[] getAverageColorFromNativeImage(NativeImage texture) {
		if (texture.getFormat() != NativeImage.Format.RGBA) {
			Data.version.sendToLog(LogType.WARN, "Invalid texture format, expected: RGBA, found: " + texture.getFormat());
			return new double[]{1.0F, 1.0F, 1.0F};
		}
		long pointer = ((NativeImageAccessor) (Object) texture).getPointer();
		if (pointer == 0) {
			Data.version.sendToLog(LogType.WARN, "Image was not allocated!");
			return new double[]{1.0F, 1.0F, 1.0F};
		} else {
			double red = 0;
			double green = 0;
			double blue = 0;
			int amount = 0;
			int width = texture.getWidth();
			int height = texture.getHeight();
			for (int pixel = 0; pixel < width * height; pixel++) {
				int color = MemoryUtil.memGetInt(pointer + 4L * pixel);
				int pRed = (color & 255);
				int pGreen = (color >> 8 & 255);
				int pBlue = (color >> 16 & 255);
				int pAlpha = (color >> 24 & 255);
				if (pAlpha != 0) {
					red += pRed;
					green += pGreen;
					blue += pBlue;
					amount++;
				}
			}
			return new double[]{(red / amount) / 255.0, (green / amount) / 255.0, (blue / amount) / 255.0};
		}
	}
}