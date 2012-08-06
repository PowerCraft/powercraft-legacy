package net.minecraft.src;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MerchantRecipeList extends ArrayList
{
    public MerchantRecipeList() {}

    public MerchantRecipeList(NBTTagCompound par1NBTTagCompound)
    {
        this.func_77201_a(par1NBTTagCompound);
    }

    public MerchantRecipe func_77203_a(ItemStack par1ItemStack, ItemStack par2ItemStack, int par3)
    {
        if (par3 > 0 && par3 < this.size())
        {
            MerchantRecipe var4 = (MerchantRecipe)this.get(par3);

            if (par1ItemStack.itemID == var4.getItemToBuy().itemID && (par2ItemStack == null && !var4.hasSecondItemToBuy() || var4.hasSecondItemToBuy() && par2ItemStack != null && var4.getSecondItemToBuy().itemID == par2ItemStack.itemID))
            {
                if (par1ItemStack.stackSize >= var4.getItemToBuy().stackSize && (!var4.hasSecondItemToBuy() || par2ItemStack.stackSize >= var4.getSecondItemToBuy().stackSize))
                {
                    return var4;
                }

                return null;
            }
        }

        for (int var6 = 0; var6 < this.size(); ++var6)
        {
            MerchantRecipe var5 = (MerchantRecipe)this.get(var6);

            if (par1ItemStack.itemID == var5.getItemToBuy().itemID && par1ItemStack.stackSize >= var5.getItemToBuy().stackSize && (!var5.hasSecondItemToBuy() && par2ItemStack == null || var5.hasSecondItemToBuy() && par2ItemStack != null && var5.getSecondItemToBuy().itemID == par2ItemStack.itemID && par2ItemStack.stackSize >= var5.getSecondItemToBuy().stackSize))
            {
                return var5;
            }
        }

        return null;
    }

    public void func_77205_a(MerchantRecipe par1MerchantRecipe)
    {
        for (int var2 = 0; var2 < this.size(); ++var2)
        {
            MerchantRecipe var3 = (MerchantRecipe)this.get(var2);

            if (par1MerchantRecipe.func_77393_a(var3))
            {
                if (par1MerchantRecipe.func_77391_b(var3))
                {
                    this.set(var2, par1MerchantRecipe);
                }

                return;
            }
        }

        this.add(par1MerchantRecipe);
    }

    public void func_77200_a(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeByte((byte)(this.size() & 255));

        for (int var2 = 0; var2 < this.size(); ++var2)
        {
            MerchantRecipe var3 = (MerchantRecipe)this.get(var2);
            Packet.writeItemStack(var3.getItemToBuy(), par1DataOutputStream);
            Packet.writeItemStack(var3.getItemToSell(), par1DataOutputStream);
            ItemStack var4 = var3.getSecondItemToBuy();
            par1DataOutputStream.writeBoolean(var4 != null);

            if (var4 != null)
            {
                Packet.writeItemStack(var4, par1DataOutputStream);
            }
        }
    }

    public void func_77201_a(NBTTagCompound par1NBTTagCompound)
    {
        NBTTagList var2 = par1NBTTagCompound.getTagList("Recipes");

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            this.add(new MerchantRecipe(var4));
        }
    }

    public NBTTagCompound func_77202_a()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        NBTTagList var2 = new NBTTagList("Recipes");

        for (int var3 = 0; var3 < this.size(); ++var3)
        {
            MerchantRecipe var4 = (MerchantRecipe)this.get(var3);
            var2.appendTag(var4.func_77395_g());
        }

        var1.setTag("Recipes", var2);
        return var1;
    }
}
