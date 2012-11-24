package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;

public class BlockAnvil extends BlockSand
{
    public static final String[] statuses = new String[] {"intact", "slightlyDamaged", "veryDamaged"};
    public int field_82521_b = 0;

    protected BlockAnvil(int par1)
    {
        super(par1, 215, Material.anvil);
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (this.field_82521_b == 3 && par1 == 1)
        {
            int var3 = par2 >> 2;

            switch (var3)
            {
                case 1:
                    return this.blockIndexInTexture + 1;

                case 2:
                    return this.blockIndexInTexture + 16 + 1;

                default:
                    return this.blockIndexInTexture + 16;
            }
        }
        else
        {
            return this.blockIndexInTexture;
        }
    }

    public int getBlockTextureFromSide(int par1)
    {
        return super.getBlockTextureFromSide(par1);
    }

    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
    {
        int var6 = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int var7 = par1World.getBlockMetadata(par2, par3, par4) >> 2;
        ++var6;
        var6 %= 4;

        if (var6 == 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2 | var7 << 2);
        }

        if (var6 == 1)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3 | var7 << 2);
        }

        if (var6 == 2)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 0 | var7 << 2);
        }

        if (var6 == 3)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1 | var7 << 2);
        }
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            par5EntityPlayer.displayGUIAnvil(par2, par3, par4);
            return true;
        }
    }

    public int getRenderType()
    {
        return 35;
    }

    public int damageDropped(int par1)
    {
        return par1 >> 2;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 3;

        if (var5 != 3 && var5 != 1)
        {
            this.setBlockBounds(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
        }
    }

    @SideOnly(Side.CLIENT)

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
    }

    protected void onStartFalling(EntityFallingSand par1EntityFallingSand)
    {
        par1EntityFallingSand.func_82154_e(true);
    }

    public void onFinishFalling(World par1World, int par2, int par3, int par4, int par5)
    {
        par1World.playAuxSFX(1022, par2, par3, par4, 0);
    }

    @SideOnly(Side.CLIENT)

    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return true;
    }
}
