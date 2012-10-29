package net.minecraft.src;

class GameRuleValue
{
    private String field_82762_a;
    private boolean field_82760_b;
    private int field_82761_c;
    private double field_82759_d;

    public GameRuleValue(String par1Str)
    {
        this.func_82757_a(par1Str);
    }

    public void func_82757_a(String par1Str)
    {
        this.field_82762_a = par1Str;
        this.field_82760_b = Boolean.parseBoolean(par1Str);

        try
        {
            this.field_82761_c = Integer.parseInt(par1Str);
        }
        catch (NumberFormatException var4)
        {
            ;
        }

        try
        {
            this.field_82759_d = Double.parseDouble(par1Str);
        }
        catch (NumberFormatException var3)
        {
            ;
        }
    }

    public String func_82756_a()
    {
        return this.field_82762_a;
    }

    public boolean func_82758_b()
    {
        return this.field_82760_b;
    }
}
