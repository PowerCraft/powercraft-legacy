package net.minecraft.src;

public enum EnumFacing
{
    DOWN(0, 1, 0, -1, 0),
    UP(1, 0, 0, 1, 0),
    NORTH(2, 3, 0, 0, -1),
    SOUTH(3, 2, 0, 0, 1),
    EAST(4, 5, -1, 0, 0),
    WEST(5, 4, 1, 0, 0);
    private final int field_82603_g;
    private final int field_82613_h;
    private final int field_82614_i;
    private final int field_82611_j;
    private final int field_82612_k;
    private static final EnumFacing[] field_82609_l = new EnumFacing[6];

    private EnumFacing(int par3, int par4, int par5, int par6, int par7)
    {
        this.field_82603_g = par3;
        this.field_82613_h = par4;
        this.field_82614_i = par5;
        this.field_82611_j = par6;
        this.field_82612_k = par7;
    }

    public int func_82601_c()
    {
        return this.field_82614_i;
    }

    public int func_82599_e()
    {
        return this.field_82612_k;
    }

    public static EnumFacing func_82600_a(int par0)
    {
        return field_82609_l[par0 % field_82609_l.length];
    }

    static {
        EnumFacing[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2)
        {
            EnumFacing var3 = var0[var2];
            field_82609_l[var3.field_82603_g] = var3;
        }
    }
}
