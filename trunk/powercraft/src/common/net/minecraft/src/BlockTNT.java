package net.minecraft.src;

import java.util.Random;

public class BlockTNT extends Block
{
    public BlockTNT(int par1, int par2)
    {
        super(par1, par2, Material.tnt);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public int getBlockTextureFromSide(int par1)
    {
        return par1 == 0 ? this.blockIndexInTexture + 2 : (par1 == 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
    }

    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);

        if (par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
        {
            this.onBlockDestroyedByPlayer(par1World, par2, par3, par4, 1);
            par1World.setBlockWithNotify(par2, par3, par4, 0);
        }
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (par5 > 0 && Block.blocksList[par5].canProvidePower() && par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
        {
            this.onBlockDestroyedByPlayer(par1World, par2, par3, par4, 1);
            par1World.setBlockWithNotify(par2, par3, par4, 0);
        }
    }

    public int quantityDropped(Random par1Random)
    {
        return 1;
    }

    public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            EntityTNTPrimed var5 = new EntityTNTPrimed(par1World, (double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F));
            var5.fuse = par1World.rand.nextInt(var5.fuse / 4) + var5.fuse / 8;
            par1World.spawnEntityInWorld(var5);
        }
    }

    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote)
        {
            if ((par5 & 1) == 1)
            {
                EntityTNTPrimed var6 = new EntityTNTPrimed(par1World, (double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F));
                par1World.spawnEntityInWorld(var6);
                par1World.playSoundAtEntity(var6, "random.fuse", 1.0F, 1.0F);
            }
        }
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par5EntityPlayer.getCurrentEquippedItem() != null && par5EntityPlayer.getCurrentEquippedItem().itemID == Item.flintAndSteel.shiftedIndex)
        {
            this.onBlockDestroyedByPlayer(par1World, par2, par3, par4, 1);
            par1World.setBlockWithNotify(par2, par3, par4, 0);
            return true;
        }
        else
        {
            return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
        }
    }

    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        if (par5Entity instanceof EntityArrow && !par1World.isRemote)
        {
            EntityArrow var6 = (EntityArrow)par5Entity;

            if (var6.isBurning())
            {
                this.onBlockDestroyedByPlayer(par1World, par2, par3, par4, 1);
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }
        }
    }

    public boolean func_85103_a(Explosion par1Explosion)
    {
        return false;
    }
}
