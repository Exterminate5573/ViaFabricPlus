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

package com.viaversion.viafabricplus.features.font;

import com.viaversion.viafabricplus.ViaFabricPlusImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GlyphSource;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.EffectGlyph;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class LegacyFontProvider implements Font.Provider {

    private final FontSet customFontSet;

    public LegacyFontProvider() {
        ViaFabricPlusImpl.INSTANCE.getLogger().warn("Reloading font");

        this.customFontSet = Minecraft.getInstance().fontManager.fontSets.get(Identifier.fromNamespaceAndPath("viafabricplus", "legacy"));
    }

    @Override
    public @NonNull GlyphSource glyphs(final @NonNull FontDescription font) {
        // Always use the custom font set regardless of the requested font
        return this.customFontSet.source(false);
    }

    @Override
    public @NonNull EffectGlyph effect() {
        return this.customFontSet.whiteGlyph();
    }

}
