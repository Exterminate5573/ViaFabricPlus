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

package com.viaversion.viafabricplus.injection.mixin.features.block.shape;

import com.viaversion.viafabricplus.injection.access.block.shape.IHorizontalConnectingBlock;
import com.viaversion.viafabricplus.protocoltranslator.ProtocolTranslator;
import com.viaversion.viafabricplus.settings.impl.DebugSettings;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalConnectingBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PaneBlock.class)
public abstract class MixinPaneBlock extends HorizontalConnectingBlock implements IHorizontalConnectingBlock {

    @Unique
    private VoxelShape[] viaFabricPlus$shape_r1_12_2;

    @Unique
    private VoxelShape[] viaFabricPlus$shape_r1_8;

    protected MixinPaneBlock(float radius1, float radius2, float boundingHeight1, float boundingHeight2, float collisionHeight, Settings settings) {
        super(radius1, radius2, boundingHeight1, boundingHeight2, collisionHeight, settings);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initShapes1_8(Settings settings, CallbackInfo ci) {
        final float f = 7.0F;
        final float g = 9.0F;
        final float h = 7.0F;
        final float i = 9.0F;

        final VoxelShape baseShape = Block.createCuboidShape(f, 0.0, f, g, (float) 16.0, g);

        viaFabricPlus$shape_r1_12_2 = new VoxelShape[]{
            baseShape,
            Block.createCuboidShape(h, 0.0, h, i, 16.0, 16.0), // south
            Block.createCuboidShape(0.0, 0.0, h, i, 16.0, i), // west
            Block.createCuboidShape(0.0, 0.0, h, i, 16.0, 16.0), // south-west corner
            Block.createCuboidShape(h, 0.0, 0.0, i, 16.0, i), // north
            Block.createCuboidShape(h, 0.0, 0.0, i, 16.0, 16.0), // south-north line
            Block.createCuboidShape(0.0, 0.0, 0.0, i, 16.0, i), // west-north corner
            Block.createCuboidShape(0.0, 0.0, 0.0, i, 16.0, 16.0), // south-west-north T
            Block.createCuboidShape(h, 0.0, h, 16.0, 16.0, i), // east
            Block.createCuboidShape(h, 0.0, h, 16.0, 16.0, 16.0), // south-east corner
            Block.createCuboidShape(0.0, 0.0, h, 16.0, 16.0, i), // west-east line
            Block.createCuboidShape(0.0, 0.0, h, 16.0, 16.0, 16.0), // south-west-east T
            Block.createCuboidShape(h, 0.0, 0.0, 16.0, 16.0, i), // north-east corner
            Block.createCuboidShape(h, 0.0, 0.0, 16.0, 16.0, 16.0), // south-north-east T
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, i), // west-north-east T
            VoxelShapes.fullCube() // cross
        };

        final VoxelShape northShape = Block.createCuboidShape(h, (float) 0.0, 0.0, i, (float) 16.0, i - 1);
        final VoxelShape southShape = Block.createCuboidShape(h, (float) 0.0, h + 1, i, (float) 16.0, 16.0);
        final VoxelShape westShape = Block.createCuboidShape(0.0, (float) 0.0, h, i - 1, (float) 16.0, i);
        final VoxelShape eastShape = Block.createCuboidShape(h + 1, (float) 0.0, h, 16.0, (float) 16.0, i);

        final VoxelShape northEastCornerShape = VoxelShapes.union(northShape, eastShape);
        final VoxelShape southWestCornerShape = VoxelShapes.union(southShape, westShape);

        viaFabricPlus$shape_r1_8 = new VoxelShape[]{
                baseShape,
                southShape,
                westShape,
                southWestCornerShape,
                northShape,
                VoxelShapes.union(southShape, northShape),
                VoxelShapes.union(westShape, northShape),
                VoxelShapes.union(southWestCornerShape, northShape),
                eastShape,
                VoxelShapes.union(southShape, eastShape),
                VoxelShapes.union(westShape, eastShape),
                VoxelShapes.union(southWestCornerShape, eastShape),
                northEastCornerShape,
                VoxelShapes.union(southShape, northEastCornerShape),
                VoxelShapes.union(westShape, northEastCornerShape),
                VoxelShapes.union(southWestCornerShape, northEastCornerShape)
        };
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (DebugSettings.INSTANCE.legacyPaneOutlines.isEnabled()) {
            return this.viaFabricPlus$shape_r1_12_2[this.viaFabricPlus$getShapeIndex(state)];
        } else {
            return super.getOutlineShape(state, world, pos, context);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (ProtocolTranslator.getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_8)) {
            return this.viaFabricPlus$shape_r1_8[this.viaFabricPlus$getShapeIndex(state)];
        } else {
            return super.getCollisionShape(state, world, pos, context);
        }
    }

}
