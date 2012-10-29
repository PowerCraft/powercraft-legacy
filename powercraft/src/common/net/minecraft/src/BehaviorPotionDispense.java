package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorPotionDispense implements IBehaviorDispenseItem
{
    private final BehaviorDefaultDispenseItem field_82484_c;

    /** Gets Minecraft Server profile. */
    final MinecraftServer minecraftServerProfiler;

    public BehaviorPotionDispense(MinecraftServer par1)
    {
        this.minecraftServerProfiler = par1;
        this.field_82484_c = new BehaviorDefaultDispenseItem();
    }

    public ItemStack func_82482_a(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        return ItemPotion.isSplash(par2ItemStack.getItemDamage()) ? (new BehaviorPotionDispenseLogic(this, par2ItemStack)).func_82482_a(par1IBlockSource, par2ItemStack) : this.field_82484_c.func_82482_a(par1IBlockSource, par2ItemStack);
    }
}
