package powercraft.core;

public interface PC_ICoord extends PC_INBT
{
    public abstract PC_ICoord getInverted();

    public abstract PC_ICoord offset(PC_CoordI added);

    public abstract PC_ICoord offset(PC_CoordD added);

    public abstract PC_ICoord offset(PC_CoordF added);

    public abstract PC_ICoord offset(int x, int y, int z);

    public abstract PC_ICoord offset(long x, long y, long z);

    public abstract PC_ICoord offset(double x, double y, double z);

    public abstract PC_ICoord offset(float x, float y, float z);

    public abstract PC_CoordI round();

    public abstract PC_CoordI floor();

    public abstract PC_CoordI ceil();
}
