package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorPotionDispense implements IBehaviorDispenseItem
{
    private final BehaviorDefaultDispenseItem defaultItemDispenseBehavior;

    final MinecraftServer mcServer;

    public BehaviorPotionDispense(MinecraftServer par1)
    {
        this.mcServer = par1;
        this.defaultItemDispenseBehavior = new BehaviorDefaultDispenseItem();
    }

    public ItemStack dispense(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        return ItemPotion.isSplash(par2ItemStack.getItemDamage()) ? (new BehaviorPotionDispenseLogic(this, par2ItemStack)).dispense(par1IBlockSource, par2ItemStack) : this.defaultItemDispenseBehavior.dispense(par1IBlockSource, par2ItemStack);
    }
}
