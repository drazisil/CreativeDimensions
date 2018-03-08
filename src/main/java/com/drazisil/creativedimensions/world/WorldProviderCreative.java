package com.drazisil.creativedimensions.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * Created by drazisil on 2/11/2017.
 */
public class WorldProviderCreative extends WorldProvider
{

    private WorldType terrainType = WorldType.DEFAULT;

    @Override
    public DimensionType getDimensionType() {
        return DimensionType.byName("creative");
    }

    public void init()
    {
        this.biomeProvider = this.terrainType.getBiomeProvider(world);
        NBTTagCompound nbttagcompound = this.world.getWorldInfo().getDimensionData(this.world.provider.getDimension());
    }

    public IChunkGenerator createChunkGenerator()
    {
        return new ChunkGeneratorCreative(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled());
    }
}