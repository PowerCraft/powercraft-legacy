package net.minecraft.scoreboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ScoreObjective
{
    private final Scoreboard field_96686_a;
    private final String field_96684_b;
    private final ScoreObjectiveCriteria field_96685_c;
    private String field_96683_d;

    public ScoreObjective(Scoreboard par1Scoreboard, String par2Str, ScoreObjectiveCriteria par3ScoreObjectiveCriteria)
    {
        this.field_96686_a = par1Scoreboard;
        this.field_96684_b = par2Str;
        this.field_96685_c = par3ScoreObjectiveCriteria;
        this.field_96683_d = par2Str;
    }

    @SideOnly(Side.CLIENT)
    public Scoreboard func_96682_a()
    {
        return this.field_96686_a;
    }

    public String func_96679_b()
    {
        return this.field_96684_b;
    }

    public ScoreObjectiveCriteria func_96680_c()
    {
        return this.field_96685_c;
    }

    public String func_96678_d()
    {
        return this.field_96683_d;
    }

    public void func_96681_a(String par1Str)
    {
        this.field_96683_d = par1Str;
        this.field_96686_a.func_96532_b(this);
    }
}
