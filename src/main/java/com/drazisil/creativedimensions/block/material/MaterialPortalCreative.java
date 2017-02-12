package com.drazisil.creativedimensions.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * Created by drazisil on 2/11/2017.
 */
public class MaterialPortalCreative  extends Material
{
    public MaterialPortalCreative(MapColor color)
    {
        super(color);
    }

    /**
     * Returns true if the block is a considered solid. This is true by default.
     */
    public boolean isSolid()
    {
        return false;
    }

    /**
     * Will prevent grass from growing on dirt underneath and kill any grass below it if it returns true
     */
    public boolean blocksLight()
    {
        return false;
    }

    /**
     * Returns if this material is considered solid or not
     */
    public boolean blocksMovement()
    {
        return false;
    }
}