package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import java.util.Random;

public class EmptyChunk extends Chunk
{
    public EmptyChunk(World par1World, int par2, int par3)
    {
        super(par1World, par2, par3);
    }

    public boolean isAtLocation(int par1, int par2)
    {
        return par1 == this.xPosition && par2 == this.zPosition;
    }

    public int getHeightValue(int par1, int par2)
    {
        return 0;
    }

    public void generateSkylightMap() {}

    @SideOnly(Side.CLIENT)

    public void generateHeightMap() {}

    public int getBlockID(int par1, int par2, int par3)
    {
        return 0;
    }

    public int getBlockLightOpacity(int par1, int par2, int par3)
    {
        return 255;
    }

    public boolean setBlockIDWithMetadata(int par1, int par2, int par3, int par4, int par5)
    {
        return true;
    }

    public boolean setBlockID(int par1, int par2, int par3, int par4)
    {
        return true;
    }

    public int getBlockMetadata(int par1, int par2, int par3)
    {
        return 0;
    }

    public boolean setBlockMetadata(int par1, int par2, int par3, int par4)
    {
        return false;
    }

    public int getSavedLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4)
    {
        return 0;
    }

    public void setLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4, int par5) {}

    public int getBlockLightValue(int par1, int par2, int par3, int par4)
    {
        return 0;
    }

    public void addEntity(Entity par1Entity) {}

    public void removeEntity(Entity par1Entity) {}

    public void removeEntityAtIndex(Entity par1Entity, int par2) {}

    public boolean canBlockSeeTheSky(int par1, int par2, int par3)
    {
        return false;
    }

    public TileEntity getChunkBlockTileEntity(int par1, int par2, int par3)
    {
        return null;
    }

    public void addTileEntity(TileEntity par1TileEntity) {}

    public void setChunkBlockTileEntity(int par1, int par2, int par3, TileEntity par4TileEntity) {}

    public void removeChunkBlockTileEntity(int par1, int par2, int par3) {}

    public void onChunkLoad() {}

    public void onChunkUnload() {}

    public void setChunkModified() {}

    public void getEntitiesWithinAABBForEntity(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB, List par3List) {}

    public void getEntitiesOfTypeWithinAAAB(Class par1Class, AxisAlignedBB par2AxisAlignedBB, List par3List, IEntitySelector par4IEntitySelector) {}

    public boolean needsSaving(boolean par1)
    {
        return false;
    }

    public Random getRandomWithSeed(long par1)
    {
        return new Random(this.worldObj.getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ par1);
    }

    public boolean isEmpty()
    {
        return true;
    }

    public boolean getAreLevelsEmpty(int par1, int par2)
    {
        return true;
    }
}
