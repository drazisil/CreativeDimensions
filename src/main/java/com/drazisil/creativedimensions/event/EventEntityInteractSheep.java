package com.drazisil.creativedimensions.event;

import com.drazisil.creativedimensions.CreativeDimensions;
import com.drazisil.creativedimensions.world.MinecraftServerCreative;
import com.drazisil.creativedimensions.world.TeleporterCreative;
import com.drazisil.creativedimensions.world.WorldServerMultiCreative;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.potion.PotionEffect;
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

import java.io.File;
import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.isNull;

@Mod.EventBusSubscriber
public class EventEntityInteractSheep {
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Entity target = event.getTarget();
        if (target instanceof EntitySheep && !event.getWorld().isRemote) {
            EntityPlayerMP source = (EntityPlayerMP) event.getEntityPlayer();
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
                File fileIn = new File(server.getDataDirectory(), "saves");
                MinecraftServerCreative serverOut = new MinecraftServerCreative(fileIn, server.getServerProxy(), server.getDataFixer(), new YggdrasilAuthenticationService(server.getServerProxy(), UUID.randomUUID().toString()), server.getMinecraftSessionService(), server.getGameProfileRepository(), server.getPlayerProfileCache());
                WorldServer worldOut = new WorldServerMultiCreative(server, saveHandler, CreativeDimensions.dimensionID, worldServerIn, profiler);
                System.out.println("New world built, go!");


                // Divider

                int i = source.dimension;
                source.connection.sendPacket(new SPacketRespawn(source.dimension, worldOut.getDifficulty(), worldOut.getWorldInfo().getTerrainType(), source.interactionManager.getGameType()));
                playerList.updatePermissionLevel(source);
//                worldServerIn.removeEntityDangerously(source);
                worldServerIn.removeEntityDangerously(source);
                worldServerIn.playerEntities.remove(source);

                source.isDead = true;
                playerList.transferEntityToWorld(source, i, worldServerIn, worldOut, new TeleporterCreative(worldServerIn));
                playerList.preparePlayer(source, worldServerIn);
                source.connection.setPlayerLocation(source.posX, source.posY, source.posZ, source.rotationYaw, source.rotationPitch);
                source.interactionManager.setWorld(worldOut);
                source.connection.sendPacket(new SPacketPlayerAbilities(source.capabilities));
                playerList.updateTimeAndWeatherForPlayer(source, worldOut);
                playerList.syncPlayerInventory(source);

                for (PotionEffect potioneffect : source.getActivePotionEffects())
                {
                    source.connection.sendPacket(new SPacketEntityEffect(source.getEntityId(), potioneffect));
                }
                net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerChangedDimensionEvent(source, i, dimensionIn);

                // ===========

                System.out.println("Player moved! " + source.dimension + " " + source.world);
            }
        }
    }
}
