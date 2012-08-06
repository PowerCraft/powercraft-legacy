package net.minecraft.src;

import java.util.Random;

public class WeightedRandomChestContent extends WeightedRandomItem
{
    private int field_76297_b;
    private int field_76298_c;
    private int field_76295_d;
    private int field_76296_e;

    public WeightedRandomChestContent(int par1, int par2, int par3, int par4, int par5)
    {
        super(par5);
        this.field_76297_b = par1;
        this.field_76298_c = par2;
        this.field_76295_d = par3;
        this.field_76296_e = par4;
    }

    public static void func_76293_a(Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, TileEntityChest par2TileEntityChest, int par3)
    {
        for (int var4 = 0; var4 < par3; ++var4)
        {
            WeightedRandomChestContent var5 = (WeightedRandomChestContent)WeightedRandom.getRandomItem(par0Random, par1ArrayOfWeightedRandomChestContent);
            int var6 = var5.field_76295_d + par0Random.nextInt(var5.field_76296_e - var5.field_76295_d + 1);

            if (Item.itemsList[var5.field_76297_b].getItemStackLimit() >= var6)
            {
                par2TileEntityChest.setInventorySlotContents(par0Random.nextInt(par2TileEntityChest.getSizeInventory()), new ItemStack(var5.field_76297_b, var6, var5.field_76298_c));
            }
            else
            {
                for (int var7 = 0; var7 < var6; ++var7)
                {
                    par2TileEntityChest.setInventorySlotContents(par0Random.nextInt(par2TileEntityChest.getSizeInventory()), new ItemStack(var5.field_76297_b, 1, var5.field_76298_c));
                }
            }
        }
    }

    public static void func_76294_a(Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, TileEntityDispenser par2TileEntityDispenser, int par3)
    {
        for (int var4 = 0; var4 < par3; ++var4)
        {
            WeightedRandomChestContent var5 = (WeightedRandomChestContent)WeightedRandom.getRandomItem(par0Random, par1ArrayOfWeightedRandomChestContent);
            int var6 = var5.field_76295_d + par0Random.nextInt(var5.field_76296_e - var5.field_76295_d + 1);

            if (Item.itemsList[var5.field_76297_b].getItemStackLimit() >= var6)
            {
                par2TileEntityDispenser.setInventorySlotContents(par0Random.nextInt(par2TileEntityDispenser.getSizeInventory()), new ItemStack(var5.field_76297_b, var6, var5.field_76298_c));
            }
            else
            {
                for (int var7 = 0; var7 < var6; ++var7)
                {
                    par2TileEntityDispenser.setInventorySlotContents(par0Random.nextInt(par2TileEntityDispenser.getSizeInventory()), new ItemStack(var5.field_76297_b, 1, var5.field_76298_c));
                }
            }
        }
    }
}
