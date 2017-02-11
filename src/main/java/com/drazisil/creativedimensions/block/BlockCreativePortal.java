package com.drazisil.creativedimensions.block;

import com.drazisil.creativedimensions.CreativeDimensions;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Created by drazisil on 2/10/2017.
 */
public class BlockCreativePortal extends BlockContainer {
    protected static final AxisAlignedBB END_PORTAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    public BlockCreativePortal(Material materialIn)
    {
        super(materialIn);
        this.setLightLevel(1.0F);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityEndPortal();
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return END_PORTAL_AABB;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.DOWN ? super.shouldSideBeRendered(blockState, blockAccess, pos, side) : false;
    }

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
    {
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn.isNonBoss() && !worldIn.isRemote && entityIn.getEntityBoundingBox().intersectsWith(state.getBoundingBox(worldIn, pos).offset(pos)))
        {
            if (entityIn.dimension != CreativeDimensions.dimensionID) {
                changeDimension(entityIn, CreativeDimensions.dimensionID);
            } else {
                changeDimension(entityIn, 0);
            }
        }
    }

    @Nullable
    public Entity changeDimension(Entity entityIn, int dimensionIn)
    {
        if (!entityIn.worldObj.isRemote && !entityIn.isDead)
        {
            if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(entityIn, dimensionIn)) return null;
            entityIn.worldObj.theProfiler.startSection("changeDimension");
            MinecraftServer minecraftserver = entityIn.getServer();
            int i = entityIn.dimension;
            WorldServer worldserver = minecraftserver.worldServerForDimension(i);
            System.out.println("WorldServer: " + worldserver.toString());
            WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimensionIn);
            System.out.println("WorldServer1: " + worldserver1.toString());
            entityIn.dimension = dimensionIn;

            if (i == 1 && dimensionIn == CreativeDimensions.dimensionID)
            {
                System.out.println("Changing Dimension...");
                worldserver1 = minecraftserver.worldServerForDimension(CreativeDimensions.dimensionID);
                entityIn.dimension = CreativeDimensions.dimensionID;
            }

            entityIn.worldObj.removeEntity(entityIn);
            entityIn.isDead = false;
            entityIn.worldObj.theProfiler.startSection("reposition");
            BlockPos blockpos;

            if (dimensionIn != 1)
            {
                blockpos = worldserver1.getSpawnCoordinate();
            }
            else
            {
                double d0 = entityIn.posX;
                double d1 = entityIn.posZ;
                double d2 = 8.0D;

                if (dimensionIn == -1)
                {
                    d0 = MathHelper.clamp_double(d0 / 8.0D, worldserver1.getWorldBorder().minX() + 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
                    d1 = MathHelper.clamp_double(d1 / 8.0D, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);
                }
                else if (dimensionIn == 0)
                {
                    d0 = MathHelper.clamp_double(d0 * 8.0D, worldserver1.getWorldBorder().minX() + 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
                    d1 = MathHelper.clamp_double(d1 * 8.0D, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);
                }

                d0 = (double)MathHelper.clamp_int((int)d0, -29999872, 29999872);
                d1 = (double)MathHelper.clamp_int((int)d1, -29999872, 29999872);
                float f = entityIn.rotationYaw;
                entityIn.setLocationAndAngles(d0, entityIn.posY, d1, 90.0F, 0.0F);
                Teleporter teleporter = worldserver1.getDefaultTeleporter();
                System.out.println("PrePortal");
                teleporter.placeInExistingPortal(entityIn, f);
                System.out.println("PostPortal");
                blockpos = new BlockPos(entityIn);
            }

            worldserver.updateEntityWithOptionalForce(entityIn, false);
            entityIn.worldObj.theProfiler.endStartSection("reloading");
            Entity entity = EntityList.createEntityByName(EntityList.getEntityString(entityIn), worldserver1);

            if (entity != null)
            {
                copyDataFromOld(entity, entityIn);

                if (i == CreativeDimensions.dimensionID && dimensionIn == CreativeDimensions.dimensionID)
                {
                    BlockPos blockpos1 = worldserver1.getTopSolidOrLiquidBlock(worldserver1.getSpawnPoint());
                    entity.moveToBlockPosAndAngles(blockpos1, entity.rotationYaw, entity.rotationPitch);
                }
                else
                {
                    entity.moveToBlockPosAndAngles(blockpos, entity.rotationYaw, entity.rotationPitch);
                }

                boolean flag = entity.forceSpawn;
                entity.forceSpawn = true;
                worldserver1.spawnEntityInWorld(entity);
                entity.forceSpawn = flag;
                worldserver1.updateEntityWithOptionalForce(entity, false);
            }

            entityIn.isDead = true;
            entityIn.worldObj.theProfiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            entityIn.worldObj.theProfiler.endSection();
            return entity;
        }
        else
        {
            return null;
        }
    }

    /**
     * Prepares this entity in new dimension by copying NBT data from entity in old dimension
     */
    private void copyDataFromOld(Entity entityNew, Entity entityIn)
    {
        NBTTagCompound nbttagcompound = entityIn.writeToNBT(new NBTTagCompound());
        nbttagcompound.removeTag("Dimension");
        entityNew.readFromNBT(nbttagcompound);
        entityNew.timeUntilPortal = entityIn.timeUntilPortal;
        // entityNew.lastPortalPos = entityIn.lastPortalPos;
        // entityNew.lastPortalVec = entityIn.lastPortalVec;
        // entityNew.teleportDirection = entityIn.teleportDirection;
    }


    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        double d0 = (double)((float)pos.getX() + rand.nextFloat());
        double d1 = (double)((float)pos.getY() + 0.8F);
        double d2 = (double)((float)pos.getZ() + rand.nextFloat());
        double d3 = 0.0D;
        double d4 = 0.0D;
        double d5 = 0.0D;
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
    }

    @Nullable
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    public MapColor getMapColor(IBlockState state)
    {
        return MapColor.BLACK;
    }
}
