package net.minecraft.src;

public class PotionHealth extends Potion
{
    public PotionHealth(int par1, boolean par2, int par3)
    {
        super(par1, par2, par3);
    }

    public boolean isInstant()
    {
        return true;
    }

    public boolean isReady(int par1, int par2)
    {
        return par1 >= 1;
    }
}
