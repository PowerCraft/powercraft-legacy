package net.minecraft.src;

public class BlockOreStorage extends Block
{
    public BlockOreStorage(int par1, int par2)
    {
        super(par1, Material.iron);
        this.blockIndexInTexture = par2;
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int getBlockTextureFromSide(int par1)
    {
        return this.blockIndexInTexture;
    }
}
