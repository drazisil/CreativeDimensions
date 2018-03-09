package com.drazisil.creativedimensions.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import net.minecraft.crash.CrashReport;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ITickable;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Util;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.FutureTask;

public class WorldServerMultiCreative extends WorldServer {
    private static final Logger LOGGER = LogManager.getLogger();
    private final WorldServer delegate;
    private IBorderListener borderListener;
    public final Queue< FutureTask<? >> futureTaskQueue = Queues. < FutureTask<? >> newArrayDeque();
    /** Incremented every tick. */
    private int tickCounter;
    /** The player list for this server */
    private PlayerList playerList;
    public java.util.Hashtable<Integer, long[]> worldTickTimes = new java.util.Hashtable<Integer, long[]>();
    private final NetworkSystem networkSystem;
    /** List of names of players who are online. */
    private final List<ITickable> tickables = Lists.<ITickable>newArrayList();


    public WorldServerMultiCreative(MinecraftServer server, ISaveHandler saveHandlerIn, int dimensionId, WorldServer delegate, Profiler profilerIn)
    {
        super(server, saveHandlerIn, new DerivedWorldInfo(delegate.getWorldInfo()), dimensionId, profilerIn);
        this.delegate = delegate;
        this.networkSystem = new NetworkSystem(server);
        this.borderListener = new IBorderListener()
        {
            public void onSizeChanged(WorldBorder border, double newSize)
            {
                WorldServerMultiCreative.this.getWorldBorder().setTransition(newSize);
            }
            public void onTransitionStarted(WorldBorder border, double oldSize, double newSize, long time)
            {
                WorldServerMultiCreative.this.getWorldBorder().setTransition(oldSize, newSize, time);
            }
            public void onCenterChanged(WorldBorder border, double x, double z)
            {
                WorldServerMultiCreative.this.getWorldBorder().setCenter(x, z);
            }
            public void onWarningTimeChanged(WorldBorder border, int newTime)
            {
                WorldServerMultiCreative.this.getWorldBorder().setWarningTime(newTime);
            }
            public void onWarningDistanceChanged(WorldBorder border, int newDistance)
            {
                WorldServerMultiCreative.this.getWorldBorder().setWarningDistance(newDistance);
            }
            public void onDamageAmountChanged(WorldBorder border, double newAmount)
            {
                WorldServerMultiCreative.this.getWorldBorder().setDamageAmount(newAmount);
            }
            public void onDamageBufferChanged(WorldBorder border, double newSize)
            {
                WorldServerMultiCreative.this.getWorldBorder().setDamageBuffer(newSize);
            }
        };
        this.delegate.getWorldBorder().addListener(this.borderListener);
    }

    /**
     * Saves the chunks to disk.
     */
    protected void saveLevel() throws MinecraftException
    {
        this.perWorldStorage.saveAllData();
    }

    public World init()
    {
        this.mapStorage = this.delegate.getMapStorage();
        this.worldScoreboard = this.delegate.getScoreboard();
        this.lootTable = this.delegate.getLootTableManager();
        this.advancementManager = this.delegate.getAdvancementManager();
        String s = VillageCollection.fileNameForProvider(this.provider);
        VillageCollection villagecollection = (VillageCollection)this.perWorldStorage.getOrLoadData(VillageCollection.class, s);

        if (villagecollection == null)
        {
            this.villageCollection = new VillageCollection(this);
            this.perWorldStorage.setData(s, this.villageCollection);
        }
        else
        {
            this.villageCollection = villagecollection;
            this.villageCollection.setWorldsForAll(this);
        }

        this.initCapabilities();
        return this;
    }


    /**
     * Syncs all changes to disk and wait for completion.
     */
    @Override
    public void flush()
    {
        super.flush();
        this.delegate.getWorldBorder().removeListener(this.borderListener); // Unlink ourselves, to prevent world leak.
    }

    /**
     * Called during saving of a world to give children worlds a chance to save additional data. Only used to save
     * WorldProviderEnd's data in Vanilla.
     */
    public void saveAdditionalData()
    {
        this.provider.onWorldSave();
    }

    public void updateTimeLightAndEntities()
    {
        this.profiler.startSection("jobs");

        synchronized (this.futureTaskQueue)
        {
            while (!this.futureTaskQueue.isEmpty())
            {
                Util.runTask(this.futureTaskQueue.poll(), LOGGER);
            }
        }

        this.profiler.endStartSection("levels");
        net.minecraftforge.common.chunkio.ChunkIOExecutor.tick();

        Integer[] ids = net.minecraftforge.common.DimensionManager.getIDs(this.tickCounter % 200 == 0);
        for (int x = 0; x < ids.length; x++)
        {
            int id = ids[x];
            long i = System.nanoTime();

            if (id == 0 || this.getAllowNether())
            {
                WorldServer worldserver = net.minecraftforge.common.DimensionManager.getWorld(id);
                this.profiler.func_194340_a(() ->
                {
                    return worldserver.getWorldInfo().getWorldName();
                });

                if (this.tickCounter % 20 == 0)
                {
                    this.profiler.startSection("timeSync");
                    this.playerList.sendPacketToAllPlayersInDimension(new SPacketTimeUpdate(worldserver.getTotalWorldTime(), worldserver.getWorldTime(), worldserver.getGameRules().getBoolean("doDaylightCycle")), worldserver.provider.getDimension());
                    this.profiler.endSection();
                }

                this.profiler.startSection("tick");
                net.minecraftforge.fml.common.FMLCommonHandler.instance().onPreWorldTick(worldserver);

                try
                {
                    worldserver.tick();
                }
                catch (Throwable throwable1)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Exception ticking world");
                    worldserver.addWorldInfoToCrashReport(crashreport);
                    throw new ReportedException(crashreport);
                }

                try
                {
                    worldserver.updateEntities();
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Exception ticking world entities");
                    worldserver.addWorldInfoToCrashReport(crashreport1);
                    throw new ReportedException(crashreport1);
                }

                net.minecraftforge.fml.common.FMLCommonHandler.instance().onPostWorldTick(worldserver);
                this.profiler.endSection();
                this.profiler.startSection("tracker");
                worldserver.getEntityTracker().tick();
                this.profiler.endSection();
                this.profiler.endSection();
            }

            worldTickTimes.get(id)[this.tickCounter % 100] = System.nanoTime() - i;
        }

        this.profiler.endStartSection("dim_unloading");
        net.minecraftforge.common.DimensionManager.unloadWorlds(worldTickTimes);
        this.profiler.endStartSection("connection");
        this.getNetworkSystem().networkTick();
        this.profiler.endStartSection("players");
        this.playerList.onTick();
        this.profiler.endStartSection("commandFunctions");
        this.getFunctionManager().update();
        this.profiler.endStartSection("tickables");

        for (int k = 0; k < this.tickables.size(); ++k)
        {
            ((ITickable)this.tickables.get(k)).update();
        }

        this.profiler.endSection();
    }

    public boolean getAllowNether()
    {
        return true;
    }

    public NetworkSystem getNetworkSystem()
    {
        return this.networkSystem;
    }
}
