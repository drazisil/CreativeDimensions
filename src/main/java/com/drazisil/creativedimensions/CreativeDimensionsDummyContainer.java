package com.drazisil.creativedimensions;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;

public class CreativeDimensionsDummyContainer extends DummyModContainer {

    public CreativeDimensionsDummyContainer() {

        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "CreativeDimensions";
        meta.name = "CreativeDimensions";
        meta.version = "@VERSION@"; //String.format("%d.%d.%d.%d", majorVersion, minorVersion, revisionVersion, buildVersion);
        meta.credits = "Roll Credits ...";
        meta.authorList = Arrays.asList("drazisil");
        meta.description = "";
        meta.url = "http://www.minecraftforum.net/topic/1884425-";
        meta.updateUrl = "";
        meta.screenshots = new String[0];
        meta.logoFile = "";

    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }


    @Subscribe
    public void modConstruction(FMLConstructionEvent evt){

    }

    @Subscribe
    public void init(FMLInitializationEvent evt) {

    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent evt) {

    }

    @Subscribe
    public void postInit(FMLPostInitializationEvent evt) {

    }

}