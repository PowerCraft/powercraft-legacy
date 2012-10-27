package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MerchantRecipeList extends ArrayList
{
    public MerchantRecipeList() {}

    public MerchantRecipeList(NBTTagCompound par1NBTTagCompound)
    {
        this.readRecipiesFromTags(par1NBTTagCompound);
    }

    /**
     * can par1,par2 be used to in crafting recipe par3
     */
    public MerchantRecipe canRecipeBeUsed(ItemStack par1ItemStack, ItemStack par2ItemStack, int par3)
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

    /**
     * checks if there is a recipie for the same ingredients already on the list, and replaces it. otherwise, adds it
     */
    public void addToListWithCheck(MerchantRecipe par1MerchantRecipe)
    {
        for (int var2 = 0; var2 < this.size(); ++var2)
        {
            MerchantRecipe var3 = (MerchantRecipe)this.get(var2);

            if (par1MerchantRecipe.hasSameIDsAs(var3))
            {
                if (par1MerchantRecipe.hasSameItemsAs(var3))
                {
                    this.set(var2, par1MerchantRecipe);
                }

                return;
            }
        }

        this.add(par1MerchantRecipe);
    }

    public void writeRecipiesToStream(DataOutputStream par1DataOutputStream) throws IOException
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

    @SideOnly(Side.CLIENT)
    public static MerchantRecipeList readRecipiesFromStream(DataInputStream par0DataInputStream) throws IOException
    {
        MerchantRecipeList var1 = new MerchantRecipeList();
        int var2 = par0DataInputStream.readByte() & 255;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            ItemStack var4 = Packet.readItemStack(par0DataInputStream);
            ItemStack var5 = Packet.readItemStack(par0DataInputStream);
            ItemStack var6 = null;

            if (par0DataInputStream.readBoolean())
            {
                var6 = Packet.readItemStack(par0DataInputStream);
            }

            var1.add(new MerchantRecipe(var4, var6, var5));
        }

        return var1;
    }

    public void readRecipiesFromTags(NBTTagCompound par1NBTTagCompound)
    {
        NBTTagList var2 = par1NBTTagCompound.getTagList("Recipes");

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            this.add(new MerchantRecipe(var4));
        }
    }

    public NBTTagCompound getRecipiesAsTags()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        NBTTagList var2 = new NBTTagList("Recipes");

        for (int var3 = 0; var3 < this.size(); ++var3)
        {
            MerchantRecipe var4 = (MerchantRecipe)this.get(var3);
            var2.appendTag(var4.writeToTags());
        }

        var1.setTag("Recipes", var2);
        return var1;
    }
}
