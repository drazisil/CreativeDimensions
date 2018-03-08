package com.drazisil.creativedimensions.proxy;

import com.drazisil.creativedimensions.CreativeDimensions;
import com.drazisil.creativedimensions.world.CreativeDimensionsWorldGenerator;
import com.drazisil.creativedimensions.world.WorldProviderCreative;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by drazisil on 2/9/2017.
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) { }

    public void init(FMLInitializationEvent e) {


        GameRegistry.registerWorldGenerator(new CreativeDimensionsWorldGenerator(), 5);


        // Register Dimension
        DimensionType.register("creative", "_creative", CreativeDimensions.dimensionID, WorldProviderCreative.class, false);
        DimensionManager.registerDimension(CreativeDimensions.dimensionID, DimensionType.byName("creative"));

    }

    public void postInit(FMLPostInitializationEvent e) { }

}
