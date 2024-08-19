/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.villager;

import com.google.gson.JsonObject;
import com.mclegoman.luminance.common.util.IdentifierHelper;
import com.mclegoman.perspective.client.entity.TexturedEntity;
import com.mclegoman.perspective.client.entity.TexturedEntityData;
import com.mclegoman.perspective.client.entity.TexturedEntityModels;
import com.mclegoman.perspective.client.entity.model.LivingEntityCapeModel;
import com.mclegoman.perspective.client.entity.renderer.feature.EntityCapeFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = VillagerEntityRenderer.class)
public abstract class VillagerEntityRendererMixin extends MobEntityRenderer<VillagerEntity, VillagerResemblingModel<VillagerEntity>> {
	public VillagerEntityRendererMixin(EntityRendererFactory.Context context, VillagerResemblingModel<VillagerEntity> entityModel, float f) {
		super(context, entityModel, f);
	}
	@Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)V", at = @At("TAIL"))
	private void perspective$init(EntityRendererFactory.Context context, CallbackInfo ci) {
		this.addFeature(new EntityCapeFeatureRenderer.Builder(this, new LivingEntityCapeModel(context.getPart(TexturedEntityModels.entityCape)), Identifier.of("perspective", "textures/entity/villager/villager_cape.png")).offsetZ(0.18F).offsetY(-0.025F).build());
	}
	// TODO: Add textured entity to villager clothes and levels.
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/VillagerEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(VillagerEntity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity != null) {
			boolean isTexturedEntity = true;
			TexturedEntityData entityData = TexturedEntity.getEntity(entity);
			if (entityData != null) {
				Identifier variantId = Registries.VILLAGER_TYPE.getId(entity.getVariant());
				String variant = variantId.toString().toLowerCase();
				Identifier professionId = Registries.VILLAGER_PROFESSION.getId(entity.getVillagerData().getProfession());
				String profession = professionId.toString().toLowerCase();
				JsonObject entitySpecific = entityData.getEntitySpecific();
				if (entitySpecific != null) {
					if (entitySpecific.has("variants")) {
						JsonObject variants = JsonHelper.getObject(entitySpecific, "variants");
						if (variants != null) {
							if (entitySpecific.has(variant)) {
								JsonObject typeRegistry = JsonHelper.getObject(variants, variant);
								if (typeRegistry != null) {
									isTexturedEntity = JsonHelper.getBoolean(typeRegistry, "enabled", true);
								}
							}
						}
					}
					if (entitySpecific.has("professions")) {
						JsonObject professions = JsonHelper.getObject(entitySpecific, "professions");
						if (professions != null) {
							if (entitySpecific.has(profession)) {
								JsonObject typeRegistry = JsonHelper.getObject(professions, profession);
								if (typeRegistry != null) {
									isTexturedEntity = JsonHelper.getBoolean(typeRegistry, "enabled", true);
								}
							}
						}
					}
				}
				if (isTexturedEntity) cir.setReturnValue(TexturedEntity.getTexture(entity, (entity.getVariant() != null ? IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, variant) : ""), "", (entity.getVariant() != null ? "_" + IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, variant) : ""), cir.getReturnValue()));
			}
		}
	}
}