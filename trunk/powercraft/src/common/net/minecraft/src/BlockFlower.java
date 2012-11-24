package net.minecraft.src;

import java.util.Random;

import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import static net.minecraftforge.common.EnumPlantType.*;

public class BlockFlower extends Block implements IPlantable
{
    protected BlockFlower(int par1, int par2, Material par3Material)
    {
        super(par1, par3Material);
        this.blockIndexInTexture = par2;
        this.setTickRandomly(true);
        float var4 = 0.2F;
        this.setBlockBounds(0.5F - var4, 0.0F, 0.5F - var4, 0.5F + var4, var4 * 3.0F, 0.5F + var4);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    protected BlockFlower(int par1, int par2)
    {
        this(par1, par2, Material.plants);
    }

    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && canBlockStay(par1World, par2, par3, par4);
    }

    protected boolean canThisPlantGrowOnThisBlockID(int par1)
    {
        return par1 == Block.grass.blockID || par1 == Block.dirt.blockID || par1 == Block.tilledField.blockID;
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
        this.checkFlowerChange(par1World, par2, par3, par4);
    }

    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        this.checkFlowerChange(par1World, par2, par3, par4);
    }

    protected final void checkFlowerChange(World par1World, int par2, int par3, int par4)
    {
        if (!this.canBlockStay(par1World, par2, par3, par4))
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockWithNotify(par2, par3, par4, 0);
        }
    }

    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        Block soil = blocksList[par1World.getBlockId(par2, par3 - 1, par4)];
        return (par1World.getFullBlockLightValue(par2, par3, par4) >= 8 || par1World.canBlockSeeTheSky(par2, par3, par4)) &&
                (soil != null && soil.canSustainPlant(par1World, par2, par3 - 1, par4, ForgeDirection.UP, this));
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return 1;
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z)
    {
        if (blockID == crops.blockID)
        {
            return Crop;
        }

        if (blockID == deadBush.blockID)
        {
            return Desert;
        }

        if (blockID == waterlily.blockID)
        {
            return Water;
        }

        if (blockID == mushroomRed.blockID)
        {
            return Cave;
        }

        if (blockID == mushroomBrown.blockID)
        {
            return Cave;
        }

        if (blockID == netherStalk.blockID)
        {
            return Nether;
        }

        if (blockID == sapling.blockID)
        {
            return Plains;
        }

        if (blockID == melonStem.blockID)
        {
            return Crop;
        }

        if (blockID == pumpkinStem.blockID)
        {
            return Crop;
        }

        if (blockID == tallGrass.blockID)
        {
            return Plains;
        }

        return Plains;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z)
    {
        return blockID;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z)
    {
        return world.getBlockMetadata(x, y, z);
    }
}
