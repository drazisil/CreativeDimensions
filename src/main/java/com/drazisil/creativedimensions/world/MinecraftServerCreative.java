package com.drazisil.creativedimensions.world;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;

public class MinecraftServerCreative extends MinecraftServer {
    public MinecraftServerCreative(File anvilFileIn, Proxy proxyIn, DataFixer dataFixerIn, YggdrasilAuthenticationService authServiceIn, MinecraftSessionService sessionServiceIn, GameProfileRepository profileRepoIn, PlayerProfileCache profileCacheIn) {
        super(anvilFileIn, proxyIn, dataFixerIn, authServiceIn, sessionServiceIn, profileRepoIn, profileCacheIn);
    }

    @Override
    public boolean init() throws IOException {
        return false;
    }

    @Override
    public boolean canStructuresSpawn() {
        return false;
    }

    @Override
    public GameType getGameType() {
        return null;
    }

    @Override
    public EnumDifficulty getDifficulty() {
        return null;
    }

    @Override
    public boolean isHardcore() {
        return false;
    }

    @Override
    public int getOpPermissionLevel() {
        return 0;
    }

    @Override
    public boolean shouldBroadcastRconToOps() {
        return false;
    }

    @Override
    public boolean shouldBroadcastConsoleToOps() {
        return false;
    }

    @Override
    public boolean isDedicatedServer() {
        return false;
    }

    @Override
    public boolean shouldUseNativeTransport() {
        return false;
    }

    @Override
    public boolean isCommandBlockEnabled() {
        return false;
    }

    @Override
    public String shareToLAN(GameType type, boolean allowCheats) {
        return null;
    }
}
