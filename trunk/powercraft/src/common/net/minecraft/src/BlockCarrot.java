package net.minecraft.src;

public class BlockCarrot extends BlockCrops
{
    public BlockCarrot(int par1)
    {
        super(par1, 200);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (par2 < 7)
        {
            if (par2 == 6)
            {
                par2 = 5;
            }

            return this.blockIndexInTexture + (par2 >> 1);
        }
        else
        {
            return this.blockIndexInTexture + 3;
        }
    }

    protected int func_82532_h()
    {
        return Item.field_82797_bK.shiftedIndex;
    }

    protected int func_82533_j()
    {
        return Item.field_82797_bK.shiftedIndex;
    }
}
