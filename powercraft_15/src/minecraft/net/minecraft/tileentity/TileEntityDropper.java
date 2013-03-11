package net.minecraft.tileentity;

public class TileEntityDropper extends TileEntityDispenser
{
    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return this.func_94042_c() ? this.field_94050_c : "container.dropper";
    }
}
