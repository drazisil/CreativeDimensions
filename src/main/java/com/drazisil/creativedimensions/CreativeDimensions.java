package com.drazisil.creativedimensions;

import com.drazisil.creativedimensions.proxy.CommonProxy;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = CreativeDimensions.MODID, version = CreativeDimensions.VERSION)
@Mod.EventBusSubscriber
public class CreativeDimensions
{
    public static final String MODID = "creativedimensions";
    public static final String VERSION = "2.0";
    public static final String NAME = "Creative Dimensions";

    public static int dimensionID = DimensionManager.getNextFreeDimId();

    // dimension provider
    // DimensionManager.registerProviderType(CreativeDimensions.dimensionProviderID, WorldProviderCreativeDimensions.class, false);

    @SidedProxy(clientSide="com.drazisil.creativedimensions.proxy.ClientProxy", serverSide="com.drazisil.creativedimensions.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance("creativedimensions")
    public static CreativeDimensions instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // load config
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        proxy.postInit(event);
    }


    @SubscribeEvent
    public static void onTravelToDimension(EntityTravelToDimensionEvent event) {
        System.out.println(event.getEntity() + " is traveling to dimension " + event.getDimension());
    }


}
