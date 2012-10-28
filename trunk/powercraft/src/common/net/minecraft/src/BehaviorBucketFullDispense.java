package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorBucketFullDispense extends BehaviorDefaultDispenseItem
{
    private final BehaviorDefaultDispenseItem field_82495_c;

    final MinecraftServer field_82494_b;

    public BehaviorBucketFullDispense(MinecraftServer par1)
    {
        this.field_82494_b = par1;
        this.field_82495_c = new BehaviorDefaultDispenseItem();
    }

    public ItemStack func_82487_b(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        ItemBucket var3 = (ItemBucket)par2ItemStack.getItem();
        int var4 = par1IBlockSource.func_82623_d();
        int var5 = par1IBlockSource.func_82622_e();
        int var6 = par1IBlockSource.func_82621_f();
        EnumFacing var7 = EnumFacing.func_82600_a(par1IBlockSource.func_82620_h());

        if (var3.tryPlaceContainedLiquid(par1IBlockSource.func_82618_k(), (double)var4, (double)var5, (double)var6, var4 + var7.func_82601_c(), var5, var6 + var7.func_82599_e()))
        {
            par2ItemStack.itemID = Item.bucketEmpty.shiftedIndex;
            par2ItemStack.stackSize = 1;
            return par2ItemStack;
        }
        else
        {
            return this.field_82495_c.func_82482_a(par1IBlockSource, par2ItemStack);
        }
    }
}
