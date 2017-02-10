package com.drazisil.creativedimensions;

import com.drazisil.creativedimensions.item.ItemWand;
import com.drazisil.creativedimensions.proxy.CommonProxy;
import com.drazisil.creativedimensions.world.CreativeDimensionsWorldGenerator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLLog;
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
    public static final String VERSION = "1.0";
    public static final String NAME = "Creative Dimensions";

    public static int dimensionID = 42;
    public static int backupdimensionID = -42;
    public static int dimensionProviderID = -42;

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
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        System.out.println("I'm a Creative Dimension >> "+Blocks.DIRT.getUnlocalizedName());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // register dimension with Forge
        if (!DimensionManager.isDimensionRegistered(CreativeDimensions.dimensionID))
        {
            DimensionManager.registerDimension(CreativeDimensions.dimensionID, DimensionType.OVERWORLD);
        }
        else
        {
            FMLLog.warning("[CreativeDimensions] Creative Dimensions detected that the configured dimension id '%d' is being used.  Using backup ID.  It is recommended that you configure this mod to use a unique dimension ID.", dimensionID);
            DimensionManager.registerDimension(CreativeDimensions.backupdimensionID, DimensionType.OVERWORLD);
            CreativeDimensions.dimensionID = CreativeDimensions.backupdimensionID;
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
                new ItemWand().setRegistryName(MODID, "wand").setCreativeTab(CreativeTabs.TOOLS)
        );
    }
}
