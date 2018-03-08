package com.drazisil.creativedimensions.items;

import com.drazisil.creativedimensions.CreativeDimensions;
import com.drazisil.creativedimensions.block.BlockCreativePortal;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by drazisil on 2/10/2017.
 */
@Mod.EventBusSubscriber
public final class ModItems {

    public static Item itemWand;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        Item itemWand = new ItemWand().setRegistryName(CreativeDimensions.MODID, "wand");
        Item itemBlockCreativePortal = new ItemBlock(new BlockCreativePortal(Material.PORTAL)).setRegistryName(new BlockCreativePortal(Material.PORTAL).getUnlocalizedName());
        event.getRegistry().registerAll(itemWand, itemBlockCreativePortal);

        System.out.println("Registered items");
    }

}