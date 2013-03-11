package net.minecraft.entity.player;

import java.util.concurrent.Callable;
import net.minecraft.item.ItemStack;

class CallableItemName implements Callable
{
    final ItemStack field_96634_a;

    final InventoryPlayer field_96633_b;

    CallableItemName(InventoryPlayer par1InventoryPlayer, ItemStack par2ItemStack)
    {
        this.field_96633_b = par1InventoryPlayer;
        this.field_96634_a = par2ItemStack;
    }

    public String func_96632_a()
    {
        return this.field_96634_a.getDisplayName();
    }

    public Object call()
    {
        return this.func_96632_a();
    }
}
