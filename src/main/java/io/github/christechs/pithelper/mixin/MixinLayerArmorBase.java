/*
 * PitHelper - A Hypixel The Pit Helper Mod.
 * Copyright (C) 2026 Christian Steenkamp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.christechs.pithelper.mixin;

import io.github.christechs.pithelper.utils.PlayerHighlightUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase {

    @Inject(
            method = "renderLayer",
            at =
            @At(
                    value = "INVOKE",
                    target =
                            "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void onBeforeRenderArmor(
            EntityLivingBase entitylivingbaseIn,
            float p_177182_2_,
            float p_177182_3_,
            float partialTicks,
            float p_177182_5_,
            float p_177182_6_,
            float p_177182_7_,
            float scale,
            int armorSlot,
            CallbackInfo ci) {
        if (!(entitylivingbaseIn instanceof AbstractClientPlayer)) return;

        int color = PlayerHighlightUtil.getHighlightColor((AbstractClientPlayer) entitylivingbaseIn);

        if (color != -1) {
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;

            GlStateManager.color(r, g, b, 1.0F);
        }
    }

    @Inject(
            method = "renderLayer",
            at =
            @At(
                    value = "INVOKE",
                    target =
                            "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
                    shift = At.Shift.AFTER))
    private void onAfterRenderArmor(
            EntityLivingBase entitylivingbaseIn,
            float p_177182_2_,
            float p_177182_3_,
            float partialTicks,
            float p_177182_5_,
            float p_177182_6_,
            float p_177182_7_,
            float scale,
            int armorSlot,
            CallbackInfo ci) {
        if (!(entitylivingbaseIn instanceof AbstractClientPlayer)) return;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
