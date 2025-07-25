/*
 * This file is part of ViaFabricPlus - https://github.com/ViaVersion/ViaFabricPlus
 * Copyright (C) 2021-2025 the original authors
 *                         - FlorianMichael/EnZaXD <florian.michael07@gmail.com>
 *                         - RK_01/RaphiMC
 * Copyright (C) 2023-2025 ViaVersion and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.viaversion.viafabricplus.injection.mixin.features.movement.jump;

import com.viaversion.viafabricplus.protocoltranslator.ProtocolTranslator;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.minecraft.entity.LivingEntity;
import net.raphimc.vialegacy.api.LegacyProtocolVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Shadow
    protected boolean jumping;

    @Shadow
    private int jumpingCooldown;

    @Redirect(method = "jump", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D"))
    private double dontLimitJumpVelocity(double a, double b) {
        if (ProtocolTranslator.getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_21)) {
            return a;
        } else {
            return Math.max(a, b);
        }
    }

    @Redirect(method = "applyMovementInput", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;jumping:Z"))
    private boolean disableJumpOnLadder(LivingEntity self) {
        return ProtocolTranslator.getTargetVersion().newerThan(ProtocolVersion.v1_13_2) && jumping;
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void removeJumpDelay(CallbackInfo ci) {
        if (ProtocolTranslator.getTargetVersion().olderThan(LegacyProtocolVersion.r1_0_0tor1_0_1)) {
            this.jumpingCooldown = 0;
        }
    }

}
