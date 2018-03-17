package com.drazisil.creativedimensions;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion(value = "1.12.2")
public class CreativeDimensionsFMLLoadingPlugin implements net.minecraftforge.fml.relauncher.IFMLLoadingPlugin {


    public static File location;

    @Override
    public String[] getASMTransformerClass() {
        //This will return the name of the class "mod.culegooner.CreeperBurnCore.CBClassTransformer"
        return new String[]{CreativeDimensionsClassTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        //This is the name of our dummy container "mod.culegooner.CreeperBurnCore.CBDummyContainer"
        return CreativeDimensionsDummyContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        //This will return the jar file of this mod CreeperBurnCore_dummy.jar"
        location = (File) data.get("coremodLocation");
        //System.out.println("*** Transformer jar location location.getName: " +location.getName());
    }

    @Override
    public String getAccessTransformerClass() {
        return CreativeDimensionsClassTransformer.class.getName();    }

}