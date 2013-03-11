package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockRail extends BlockRailBase
{
    @SideOnly(Side.CLIENT)
    private Icon field_94359_b;

    protected BlockRail(int par1)
    {
        super(par1, false);
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return par2 >= 6 ? this.field_94359_b : this.field_94336_cN;
    }

    @SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
        super.func_94332_a(par1IconRegister);
        this.field_94359_b = par1IconRegister.func_94245_a("rail_turn");
    }

    protected void func_94358_a(World par1World, int par2, int par3, int par4, int par5, int par6, int par7)
    {
        if (par7 > 0 && Block.blocksList[par7].canProvidePower() && (new BlockBaseRailLogic(this, par1World, par2, par3, par4)).func_94505_a() == 3)
        {
            this.refreshTrackShape(par1World, par2, par3, par4, false);
        }
    }
}
