package net.minecraft.src;

import java.util.Random;

public class WeightedRandomChestContent extends WeightedRandomItem
{
    /** The Item/Block ID to generate in the Chest. */
    private int theItemId;

    /** The Item damage/metadata. */
    private int theItemDamage;

    /** The minimum chance of item generating. */
    private int theMinimumChanceToGenerateItem;

    /** The maximum chance of item generating. */
    private int theMaximumChanceToGenerateItem;

    public WeightedRandomChestContent(int par1, int par2, int par3, int par4, int par5)
    {
        super(par5);
        this.theItemId = par1;
        this.theItemDamage = par2;
        this.theMinimumChanceToGenerateItem = par3;
        this.theMaximumChanceToGenerateItem = par4;
    }

    /**
     * Generates the Chest contents.
     */
    public static void generateChestContents(Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, TileEntityChest par2TileEntityChest, int par3)
    {
        for (int var4 = 0; var4 < par3; ++var4)
        {
            WeightedRandomChestContent var5 = (WeightedRandomChestContent)WeightedRandom.getRandomItem(par0Random, par1ArrayOfWeightedRandomChestContent);
            int var6 = var5.theMinimumChanceToGenerateItem + par0Random.nextInt(var5.theMaximumChanceToGenerateItem - var5.theMinimumChanceToGenerateItem + 1);

            if (Item.itemsList[var5.theItemId].getItemStackLimit() >= var6)
            {
                par2TileEntityChest.setInventorySlotContents(par0Random.nextInt(par2TileEntityChest.getSizeInventory()), new ItemStack(var5.theItemId, var6, var5.theItemDamage));
            }
            else
            {
                for (int var7 = 0; var7 < var6; ++var7)
                {
                    par2TileEntityChest.setInventorySlotContents(par0Random.nextInt(par2TileEntityChest.getSizeInventory()), new ItemStack(var5.theItemId, 1, var5.theItemDamage));
                }
            }
        }
    }

    /**
     * Generates the Dispenser contents.
     */
    public static void generateDispenserContents(Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, TileEntityDispenser par2TileEntityDispenser, int par3)
    {
        for (int var4 = 0; var4 < par3; ++var4)
        {
            WeightedRandomChestContent var5 = (WeightedRandomChestContent)WeightedRandom.getRandomItem(par0Random, par1ArrayOfWeightedRandomChestContent);
            int var6 = var5.theMinimumChanceToGenerateItem + par0Random.nextInt(var5.theMaximumChanceToGenerateItem - var5.theMinimumChanceToGenerateItem + 1);

            if (Item.itemsList[var5.theItemId].getItemStackLimit() >= var6)
            {
                par2TileEntityDispenser.setInventorySlotContents(par0Random.nextInt(par2TileEntityDispenser.getSizeInventory()), new ItemStack(var5.theItemId, var6, var5.theItemDamage));
            }
            else
            {
                for (int var7 = 0; var7 < var6; ++var7)
                {
                    par2TileEntityDispenser.setInventorySlotContents(par0Random.nextInt(par2TileEntityDispenser.getSizeInventory()), new ItemStack(var5.theItemId, 1, var5.theItemDamage));
                }
            }
        }
    }
}
