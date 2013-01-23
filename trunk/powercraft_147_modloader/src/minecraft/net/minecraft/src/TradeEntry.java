package net.minecraft.src;

public class TradeEntry
{
    public final int id;
    public float chance;
    public boolean buying;
    public int min;
    public int max;

    public TradeEntry(int var1, float var2, boolean var3, int var4, int var5)
    {
        this.min = 0;
        this.max = 0;
        this.id = var1;
        this.chance = var2;
        this.buying = var3;
        this.min = var4;
        this.max = var5;
    }

    public TradeEntry(int var1, float var2, boolean var3)
    {
        this(var1, var2, var3, 0, 0);
    }
}
