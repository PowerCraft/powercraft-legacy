package net.minecraft.enchantment;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class EnchantmentThorns extends Enchantment
{
    public EnchantmentThorns(int par1, int par2)
    {
        super(par1, par2, EnumEnchantmentType.armor_torso);
        this.setName("thorns");
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int par1)
    {
        return 10 + 20 * (par1 - 1);
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int par1)
    {
        return super.getMinEnchantability(par1) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 3;
    }

    public boolean func_92037_a(ItemStack par1ItemStack)
    {
        return par1ItemStack.getItem() instanceof ItemArmor ? true : super.func_92037_a(par1ItemStack);
    }

    public static boolean func_92042_a(int par0, Random par1Random)
    {
        return par0 <= 0 ? false : par1Random.nextFloat() < 0.15F * (float)par0;
    }

    public static int func_92043_b(int par0, Random par1)
    {
        return par0 > 10 ? par0 - 10 : 1 + par1.nextInt(4);
    }

    public static void func_92044_a(Entity par0Entity, EntityLiving par1, Random par2Random)
    {
        int var3 = EnchantmentHelper.func_92046_i(par1);
        ItemStack var4 = EnchantmentHelper.func_92047_a(Enchantment.field_92039_k, par1);

        if (func_92042_a(var3, par2Random))
        {
            par0Entity.attackEntityFrom(DamageSource.func_92036_a(par1), func_92043_b(var3, par2Random));
            par0Entity.func_85030_a("damage.thorns", 0.5F, 1.0F);

            if (var4 != null)
            {
                var4.damageItem(3, par1);
            }
        }
        else if (var4 != null)
        {
            var4.damageItem(1, par1);
        }
    }
}
