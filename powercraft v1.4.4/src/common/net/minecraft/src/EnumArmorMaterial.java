package net.minecraft.src;

public enum EnumArmorMaterial
{
    CLOTH(5, new int[]{1, 3, 2, 1}, 15),
    CHAIN(15, new int[]{2, 5, 4, 1}, 12),
    IRON(15, new int[]{2, 6, 5, 2}, 9),
    GOLD(7, new int[]{2, 5, 3, 1}, 25),
    DIAMOND(33, new int[]{3, 8, 6, 3}, 10);

    private int maxDamageFactor;

    private int[] damageReductionAmountArray;

    private int enchantability;

    private EnumArmorMaterial(int par3, int[] par4ArrayOfInteger, int par5)
    {
        this.maxDamageFactor = par3;
        this.damageReductionAmountArray = par4ArrayOfInteger;
        this.enchantability = par5;
    }

    public int getDurability(int par1)
    {
        return ItemArmor.getMaxDamageArray()[par1] * this.maxDamageFactor;
    }

    public int getDamageReductionAmount(int par1)
    {
        return this.damageReductionAmountArray[par1];
    }

    public int getEnchantability()
    {
        return this.enchantability;
    }

    public int getArmorCraftingMaterial()
    {
        return this == CLOTH ? Item.leather.shiftedIndex : (this == CHAIN ? Item.ingotIron.shiftedIndex : (this == GOLD ? Item.ingotGold.shiftedIndex : (this == IRON ? Item.ingotIron.shiftedIndex : (this == DIAMOND ? Item.diamond.shiftedIndex : 0))));
    }
}
