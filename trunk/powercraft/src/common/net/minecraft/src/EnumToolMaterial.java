package net.minecraft.src;

public enum EnumToolMaterial
{
    WOOD(0, 59, 2.0F, 0, 15),
    STONE(1, 131, 4.0F, 1, 5),
    IRON(2, 250, 6.0F, 2, 14),
    EMERALD(3, 1561, 8.0F, 3, 10),
    GOLD(0, 32, 12.0F, 0, 22);

    private final int harvestLevel;

    private final int maxUses;

    private final float efficiencyOnProperMaterial;

    private final int damageVsEntity;

    private final int enchantability;

    private EnumToolMaterial(int par3, int par4, float par5, int par6, int par7)
    {
        this.harvestLevel = par3;
        this.maxUses = par4;
        this.efficiencyOnProperMaterial = par5;
        this.damageVsEntity = par6;
        this.enchantability = par7;
    }

    public int getMaxUses()
    {
        return this.maxUses;
    }

    public float getEfficiencyOnProperMaterial()
    {
        return this.efficiencyOnProperMaterial;
    }

    public int getDamageVsEntity()
    {
        return this.damageVsEntity;
    }

    public int getHarvestLevel()
    {
        return this.harvestLevel;
    }

    public int getEnchantability()
    {
        return this.enchantability;
    }

    public int getToolCraftingMaterial()
    {
        return this == WOOD ? Block.planks.blockID : (this == STONE ? Block.cobblestone.blockID : (this == GOLD ? Item.ingotGold.shiftedIndex : (this == IRON ? Item.ingotIron.shiftedIndex : (this == EMERALD ? Item.diamond.shiftedIndex : 0))));
    }
}
