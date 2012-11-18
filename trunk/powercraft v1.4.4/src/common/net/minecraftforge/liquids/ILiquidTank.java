package net.minecraftforge.liquids;

public interface ILiquidTank
{
    LiquidStack getLiquid();
    void setLiquid(LiquidStack liquid);
    void setCapacity(int capacity);
    int getCapacity();

    int fill(LiquidStack resource, boolean doFill);

    LiquidStack drain(int maxDrain, boolean doDrain);

    public int getTankPressure();
}
