package com.drazisil.creativedimensions.world;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

/**
 * Created by drazisil on 2/11/2017.
 */
public class WorldProviderCreative extends WorldProvider
{

    @Override
    public DimensionType getDimensionType() {
        return DimensionType.byName("creative");
    }
}