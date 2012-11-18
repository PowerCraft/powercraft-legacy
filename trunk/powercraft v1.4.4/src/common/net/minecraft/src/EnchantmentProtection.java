package net.minecraft.src;

public class EnchantmentProtection extends Enchantment
{
    private static final String[] protectionName = new String[] {"all", "fire", "fall", "explosion", "projectile"};

    private static final int[] baseEnchantability = new int[] {1, 10, 5, 5, 3};

    private static final int[] levelEnchantability = new int[] {11, 8, 6, 8, 6};

    private static final int[] thresholdEnchantability = new int[] {20, 12, 10, 12, 15};

    public final int protectionType;

    public EnchantmentProtection(int par1, int par2, int par3)
    {
        super(par1, par2, EnumEnchantmentType.armor);
        this.protectionType = par3;

        if (par3 == 2)
        {
            this.type = EnumEnchantmentType.armor_feet;
        }
    }

    public int getMinEnchantability(int par1)
    {
        return baseEnchantability[this.protectionType] + (par1 - 1) * levelEnchantability[this.protectionType];
    }

    public int getMaxEnchantability(int par1)
    {
        return this.getMinEnchantability(par1) + thresholdEnchantability[this.protectionType];
    }

    public int getMaxLevel()
    {
        return 4;
    }

    public int calcModifierDamage(int par1, DamageSource par2DamageSource)
    {
        if (par2DamageSource.canHarmInCreative())
        {
            return 0;
        }
        else
        {
            int var3 = (6 + par1 * par1) / 2;
            return this.protectionType == 0 ? var3 : (this.protectionType == 1 && par2DamageSource.isFireDamage() ? var3 : (this.protectionType == 2 && par2DamageSource == DamageSource.fall ? var3 * 2 : ((this.protectionType != 3 || par2DamageSource != DamageSource.explosion) && par2DamageSource != DamageSource.field_76375_l ? (this.protectionType == 4 && par2DamageSource.isProjectile() ? var3 : 0) : var3)));
        }
    }

    public String getName()
    {
        return "enchantment.protect." + protectionName[this.protectionType];
    }

    public boolean canApplyTogether(Enchantment par1Enchantment)
    {
        if (par1Enchantment instanceof EnchantmentProtection)
        {
            EnchantmentProtection var2 = (EnchantmentProtection)par1Enchantment;
            return var2.protectionType == this.protectionType ? false : this.protectionType == 2 || var2.protectionType == 2;
        }
        else
        {
            return super.canApplyTogether(par1Enchantment);
        }
    }
}
