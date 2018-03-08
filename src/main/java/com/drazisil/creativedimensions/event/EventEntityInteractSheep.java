package com.drazisil.creativedimensions.event;

import com.drazisil.creativedimensions.CreativeDimensions;
import com.drazisil.creativedimensions.world.TeleporterCreative;
import com.drazisil.creativedimensions.world.WorldServerMultiCreative;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static java.util.Objects.isNull;

@Mod.EventBusSubscriber
public class EventEntityInteractSheep {
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        Entity target = event.getTarget();
        if (target instanceof EntitySheep) {
            EntityPlayer source = event.getEntityPlayer();
            int dimensionIn = source.dimension;
            World worldIn = source.world;
            MinecraftServer server = event.getWorld().getMinecraftServer();
            assert server != null;
            System.out.println("Preparing to exit world " + server);
            PlayerList playerList = server.getPlayerList();
            if (isNull(playerList)) {
                System.out.println("Null Player list " + playerList);
            } else {
                ISaveHandler saveHandler = worldIn.getSaveHandler();
                Profiler profiler = server.profiler;
                WorldInfo worldInfo = worldIn.getWorldInfo();
                WorldServer worldServerIn = server.getWorld(dimensionIn);
                WorldServer worldOut = new WorldServerMultiCreative(server, saveHandler, worldInfo, CreativeDimensions.dimensionID, profiler);
                System.out.println("New world built, go!");
                playerList.transferEntityToWorld(source, dimensionIn, worldServerIn, worldOut, new TeleporterCreative(worldServerIn));

            }

        }
    }
}
