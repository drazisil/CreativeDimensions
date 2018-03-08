package com.drazisil.creativedimensions.event;

import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventTravelDimension {

    @SubscribeEvent
    public static void onTravelToDimension(EntityTravelToDimensionEvent event) {
        System.out.println(event.getEntity() + " is traveling to dimension " + event.getDimension());
        System.out.println(event.getEntity().world);
    }
}
