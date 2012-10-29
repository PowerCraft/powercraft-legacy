package net.minecraft.src;

public class BlockSourceImpl implements IBlockSource
{
    private final World field_82627_a;
    private final int field_82625_b;
    private final int field_82626_c;
    private final int field_82624_d;

    public BlockSourceImpl(World par1World, int par2, int par3, int par4)
    {
        this.field_82627_a = par1World;
        this.field_82625_b = par2;
        this.field_82626_c = par3;
        this.field_82624_d = par4;
    }

    public World func_82618_k()
    {
        return this.field_82627_a;
    }

    public double func_82615_a()
    {
        return (double)this.field_82625_b + 0.5D;
    }

    public double func_82617_b()
    {
        return (double)this.field_82626_c + 0.5D;
    }

    public double func_82616_c()
    {
        return (double)this.field_82624_d + 0.5D;
    }

    public int func_82623_d()
    {
        return this.field_82625_b;
    }

    public int func_82622_e()
    {
        return this.field_82626_c;
    }

    public int func_82621_f()
    {
        return this.field_82624_d;
    }

    public int func_82620_h()
    {
        return this.field_82627_a.getBlockMetadata(this.field_82625_b, this.field_82626_c, this.field_82624_d);
    }

    public TileEntity func_82619_j()
    {
        return this.field_82627_a.getBlockTileEntity(this.field_82625_b, this.field_82626_c, this.field_82624_d);
    }
}
