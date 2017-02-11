package com.drazisil.creativedimensions;

import com.drazisil.creativedimensions.client.renderer.ItemRenderRegister;
import com.drazisil.creativedimensions.items.ModItems;
import com.drazisil.creativedimensions.proxy.CommonProxy;
import net.minecraft.block.Block;
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

import static com.drazisil.creativedimensions.items.ModItems.itemWand;

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
        ModItems.createItems();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        ItemRenderRegister.registerItemRenderer();

        System.out.println("ItemWand1 >> " + itemWand.getUnlocalizedName());
        System.out.println("ItemWand2 >> " + itemWand.getRegistryName());

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
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.createBlocks();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
//        itemWand = new ItemWand();
//        GameRegistry.register(itemWand);
//        GameRegistry.register(itemWand, new ResourceLocation(CreativeDimensions.MODID, "wand"));

//        ModelManager modelManager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager();
    }


}
