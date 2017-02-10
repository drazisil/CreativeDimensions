package com.drazisil.creativedimensions.item;

import com.drazisil.creativedimensions.CreativeDimensions;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLLog;

/**
 * Created by drazisil on 2/10/2017.
 */
public class ItemWand extends Item {
    public ItemWand() {

        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setUnlocalizedName(CreativeDimensions.MODID + ":block.wand");
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player, EnumHand hand)
    {
        if (player.dimension != CreativeDimensions.dimensionID) {
            sendEntityToDimension(player, CreativeDimensions.dimensionID);

        }
        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }

        /**
         * This copy of the entity.travelToDimension method exists so that we can use our own teleporter
         */

    public void sendEntityToDimension(Entity entity, int par1) {
        // transfer a random entity?
        if (!entity.worldObj.isRemote && !entity.isDead) {
            entity.worldObj.theProfiler.startSection("changeDimension");
            MinecraftServer minecraftserver = entity.getServer();
            int dim = entity.dimension;
            WorldServer worldserver = minecraftserver.worldServerForDimension(dim);
            WorldServer worldserver1 = minecraftserver.worldServerForDimension(par1);
            entity.dimension = par1;
            entity.worldObj.removeEntity(entity);
            entity.isDead = false;
            entity.worldObj.theProfiler.startSection("reposition");
            // minecraftserver.getConfigurationManager().transferEntityToWorld(entity, dim, worldserver, worldserver1, new TFTeleporter(worldserver1));
            entity.worldObj.theProfiler.endStartSection("reloading");
            Entity transferEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);

            if (transferEntity != null) {
                copyDataFromOld(entity);
                worldserver1.spawnEntityInWorld(transferEntity);
            }

            entity.isDead = true;
            entity.worldObj.theProfiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            entity.worldObj.theProfiler.endSection();
        }
    }

    /**
     * Prepares this entity in new dimension by copying NBT data from entity in old dimension
     */
    private void copyDataFromOld(Entity entityIn)
    {
        NBTTagCompound nbttagcompound = entityIn.writeToNBT(new NBTTagCompound());
        nbttagcompound.removeTag("Dimension");
        entityIn.readFromNBT(nbttagcompound);
    }

}

