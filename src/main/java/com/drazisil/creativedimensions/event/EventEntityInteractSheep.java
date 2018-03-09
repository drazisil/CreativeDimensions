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

import java.util.Objects;

import static java.util.Objects.isNull;

@Mod.EventBusSubscriber
public class EventEntityInteractSheep {
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Entity target = event.getTarget();
        if (target instanceof EntitySheep && !event.getWorld().isRemote) {
            EntityPlayer source = event.getEntityPlayer();
            int dimensionIn = source.dimension;
            World worldIn = source.world;
            System.out.println(source.toString());
            MinecraftServer server = source.getServer();
            System.out.println(Objects.requireNonNull(server).toString());
            assert server != null;
            if (isNull(server)) {
                System.out.println("server is null, don't crash");
            } else {
                System.out.println("Preparing to exit world " + server);
                PlayerList playerList = server.getPlayerList();
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
