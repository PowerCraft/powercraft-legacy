package net.minecraft.src;

public interface IMerchant
{
    void setCustomer(EntityPlayer var1);

    EntityPlayer getCustomer();

    MerchantRecipeList func_70934_b(EntityPlayer var1);

    void func_70933_a(MerchantRecipe var1);
}
