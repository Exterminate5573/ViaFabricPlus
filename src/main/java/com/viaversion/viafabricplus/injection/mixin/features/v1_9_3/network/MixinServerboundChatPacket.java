/*
 * This file is part of ViaFabricPlus - https://github.com/ViaVersion/ViaFabricPlus
 * Copyright (C) 2021-2026 the original authors
 *                         - Florian Reuth <git@florianreuth.de>
 *                         - RK_01/RaphiMC
 * Copyright (C) 2023-2026 ViaVersion and contributors
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

package com.viaversion.viafabricplus.injection.mixin.features.v1_9_3.network;

import com.viaversion.viafabricplus.ViaFabricPlus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerboundChatPacket.class)
public abstract class MixinServerboundChatPacket {

    @Mutable
    @Shadow
    @Final
    public static StreamCodec<FriendlyByteBuf, ServerboundChatPacket> STREAM_CODEC;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyStreamCodec(CallbackInfo ci) {
        // TODO: I have no idea what im doing
        STREAM_CODEC = viaFabricPlus$getCodec();
    }

    @Unique
    private static StreamCodec<FriendlyByteBuf, ServerboundChatPacket> viaFabricPlus$getCodec() {
        return StreamCodec.composite(ByteBufCodecs.stringUtf8(ViaFabricPlus.api().limitations().maxChatLength()), ServerboundChatPacket::message, ByteBufCodecs.INSTANT, ServerboundChatPacket::timeStamp, ByteBufCodecs.LONG, ServerboundChatPacket::salt, MessageSignature.STREAM_CODEC.apply(ByteBufCodecs::optional), ServerboundChatPacket::signature, LastSeenMessages.Update.STREAM_CODEC, ServerboundChatPacket::lastSeenMessages, ServerboundChatPacket::new);
    }

}
