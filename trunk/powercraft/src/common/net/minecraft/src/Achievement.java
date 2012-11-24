package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class Achievement extends StatBase
{
    public final int displayColumn;

    public final int displayRow;

    public final Achievement parentAchievement;

    private final String achievementDescription;
    @SideOnly(Side.CLIENT)

    private IStatStringFormat statStringFormatter;

    public final ItemStack theItemStack;

    private boolean isSpecial;

    public Achievement(int par1, String par2Str, int par3, int par4, Item par5Item, Achievement par6Achievement)
    {
        this(par1, par2Str, par3, par4, new ItemStack(par5Item), par6Achievement);
    }

    public Achievement(int par1, String par2Str, int par3, int par4, Block par5Block, Achievement par6Achievement)
    {
        this(par1, par2Str, par3, par4, new ItemStack(par5Block), par6Achievement);
    }

    public Achievement(int par1, String par2Str, int par3, int par4, ItemStack par5ItemStack, Achievement par6Achievement)
    {
        super(5242880 + par1, "achievement." + par2Str);
        this.theItemStack = par5ItemStack;
        this.achievementDescription = "achievement." + par2Str + ".desc";
        this.displayColumn = par3;
        this.displayRow = par4;

        if (par3 < AchievementList.minDisplayColumn)
        {
            AchievementList.minDisplayColumn = par3;
        }

        if (par4 < AchievementList.minDisplayRow)
        {
            AchievementList.minDisplayRow = par4;
        }

        if (par3 > AchievementList.maxDisplayColumn)
        {
            AchievementList.maxDisplayColumn = par3;
        }

        if (par4 > AchievementList.maxDisplayRow)
        {
            AchievementList.maxDisplayRow = par4;
        }

        this.parentAchievement = par6Achievement;
    }

    public Achievement setIndependent()
    {
        this.isIndependent = true;
        return this;
    }

    public Achievement setSpecial()
    {
        this.isSpecial = true;
        return this;
    }

    public Achievement registerAchievement()
    {
        super.registerStat();
        AchievementList.achievementList.add(this);
        return this;
    }

    @SideOnly(Side.CLIENT)

    public boolean isAchievement()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)

    public String getDescription()
    {
        return this.statStringFormatter != null ? this.statStringFormatter.formatString(StatCollector.translateToLocal(this.achievementDescription)) : StatCollector.translateToLocal(this.achievementDescription);
    }

    @SideOnly(Side.CLIENT)

    public Achievement setStatStringFormatter(IStatStringFormat par1IStatStringFormat)
    {
        this.statStringFormatter = par1IStatStringFormat;
        return this;
    }

    @SideOnly(Side.CLIENT)

    public boolean getSpecial()
    {
        return this.isSpecial;
    }

    public StatBase registerStat()
    {
        return this.registerAchievement();
    }

    public StatBase initIndependentStat()
    {
        return this.setIndependent();
    }
}
