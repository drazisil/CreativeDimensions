package com.drazisil.creativedimensions.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by drazisil on 2/10/2017.
 */
@Mod.EventBusSubscriber
public class ModBlocks {

    public static final BlockCreativePortal BLOCK_CREATIVE_PORTAL = null;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        Block creativePortal = new BlockCreativePortal(Material.PORTAL);
        event.getRegistry().registerAll(creativePortal.setRegistryName(creativePortal.getUnlocalizedName()));
        System.out.println("Registered blocks");
    }
}