package net.minecraftforge.liquids;

import net.minecraftforge.common.ForgeDirection;

public interface ITankContainer
{
    int fill(ForgeDirection from, LiquidStack resource, boolean doFill);

    int fill(int tankIndex, LiquidStack resource, boolean doFill);

    LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain);

    LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain);

    ILiquidTank[] getTanks(ForgeDirection direction);

    ILiquidTank getTank(ForgeDirection direction, LiquidStack type);
}
