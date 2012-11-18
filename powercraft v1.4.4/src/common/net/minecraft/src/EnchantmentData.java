package net.minecraft.src;

public class EnchantmentData extends WeightedRandomItem
{
    public final Enchantment enchantmentobj;

    public final int enchantmentLevel;

    public EnchantmentData(Enchantment par1Enchantment, int par2)
    {
        super(par1Enchantment.getWeight());
        this.enchantmentobj = par1Enchantment;
        this.enchantmentLevel = par2;
    }
}
