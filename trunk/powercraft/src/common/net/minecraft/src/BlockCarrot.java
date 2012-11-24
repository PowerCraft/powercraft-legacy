package net.minecraft.src;

public class BlockCarrot extends BlockCrops
{
    public BlockCarrot(int par1)
    {
        super(par1, 200);
    }

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

    protected int getSeedItem()
    {
        return Item.carrot.shiftedIndex;
    }

    protected int getCropItem()
    {
        return Item.carrot.shiftedIndex;
    }
}
