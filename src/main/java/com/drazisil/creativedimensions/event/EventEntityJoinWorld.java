package com.drazisil.creativedimensions.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.drazisil.creativedimensions.CreativeDimensions.logger;

@Mod.EventBusSubscriber
public class EventEntityJoinWorld {
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            logger.info(event.getEntity() + " joined world: " + event.getWorld());
        }
    }
}
