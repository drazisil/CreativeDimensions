package com.drazisil.creativedimensions.proxy;

import com.drazisil.creativedimensions.world.WorldGen;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by drazisil on 2/9/2017.
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {

        WorldGen.init();

    }

    public void init(FMLInitializationEvent e) {


    }

}
