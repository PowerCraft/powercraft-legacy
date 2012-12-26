package net.minecraft.src;

public class PositionImpl implements IPosition
{
    protected final double field_82630_a;
    protected final double field_82628_b;
    protected final double field_82629_c;

    public PositionImpl(double par1, double par3, double par5)
    {
        this.field_82630_a = par1;
        this.field_82628_b = par3;
        this.field_82629_c = par5;
    }

    public double getX()
    {
        return this.field_82630_a;
    }

    public double getY()
    {
        return this.field_82628_b;
    }

    public double getZ()
    {
        return this.field_82629_c;
    }
}
