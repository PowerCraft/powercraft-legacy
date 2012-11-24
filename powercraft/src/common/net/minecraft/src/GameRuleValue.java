package net.minecraft.src;

class GameRuleValue
{
    private String valueString;
    private boolean valueBoolean;
    private int valueInteger;
    private double valueDouble;

    public GameRuleValue(String par1Str)
    {
        this.setValue(par1Str);
    }

    public void setValue(String par1Str)
    {
        this.valueString = par1Str;
        this.valueBoolean = Boolean.parseBoolean(par1Str);

        try
        {
            this.valueInteger = Integer.parseInt(par1Str);
        }
        catch (NumberFormatException var4)
        {
            ;
        }

        try
        {
            this.valueDouble = Double.parseDouble(par1Str);
        }
        catch (NumberFormatException var3)
        {
            ;
        }
    }

    public String getGameRuleStringValue()
    {
        return this.valueString;
    }

    public boolean getGameRuleBooleanValue()
    {
        return this.valueBoolean;
    }
}
