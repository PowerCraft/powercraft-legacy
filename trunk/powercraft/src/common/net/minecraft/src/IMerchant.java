package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public interface IMerchant
{
    void setCustomer(EntityPlayer var1);

    EntityPlayer getCustomer();

    MerchantRecipeList getRecipes(EntityPlayer var1);

    @SideOnly(Side.CLIENT)
    void setRecipes(MerchantRecipeList var1);

    void useRecipe(MerchantRecipe var1);
}
