package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorBucketEmptyDispense extends BehaviorDefaultDispenseItem
{
    private final BehaviorDefaultDispenseItem field_82497_c;

    final MinecraftServer field_82496_b;

    public BehaviorBucketEmptyDispense(MinecraftServer par1)
    {
        this.field_82496_b = par1;
        this.field_82497_c = new BehaviorDefaultDispenseItem();
    }

    public ItemStack func_82487_b(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        EnumFacing var3 = EnumFacing.func_82600_a(par1IBlockSource.func_82620_h());
        World var4 = par1IBlockSource.func_82618_k();
        int var5 = par1IBlockSource.func_82623_d() + var3.func_82601_c();
        int var6 = par1IBlockSource.func_82622_e();
        int var7 = par1IBlockSource.func_82621_f() + var3.func_82599_e();
        Material var8 = var4.getBlockMaterial(var5, var6, var7);
        int var9 = var4.getBlockMetadata(var5, var6, var7);
        Item var10;

        if (Material.water.equals(var8) && var9 == 0)
        {
            var10 = Item.bucketWater;
        }
        else
        {
            if (!Material.lava.equals(var8) || var9 != 0)
            {
                return super.func_82487_b(par1IBlockSource, par2ItemStack);
            }

            var10 = Item.bucketLava;
        }

        var4.setBlockWithNotify(var5, var6, var7, 0);

        if (--par2ItemStack.stackSize == 0)
        {
            par2ItemStack.itemID = var10.shiftedIndex;
            par2ItemStack.stackSize = 1;
        }
        else if (((TileEntityDispenser)par1IBlockSource.func_82619_j()).func_70360_a(new ItemStack(var10)) < 0)
        {
            this.field_82497_c.func_82482_a(par1IBlockSource, new ItemStack(var10));
        }

        return par2ItemStack;
    }
}
