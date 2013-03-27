package net.minecraft.src;

public class ScoreObjective
{
    private final Scoreboard theScoreboard;
    private final String field_96684_b;
    private final ScoreObjectiveCriteria field_96685_c;
    private String field_96683_d;

    public ScoreObjective(Scoreboard par1Scoreboard, String par2Str, ScoreObjectiveCriteria par3ScoreObjectiveCriteria)
    {
        this.theScoreboard = par1Scoreboard;
        this.field_96684_b = par2Str;
        this.field_96685_c = par3ScoreObjectiveCriteria;
        this.field_96683_d = par2Str;
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
        this.theScoreboard.func_96532_b(this);
    }
}
