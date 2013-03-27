package net.minecraft.src;

public class EntityMinecartChest extends EntityMinecartContainer
{
    public EntityMinecartChest(World par1World)
    {
        super(par1World);
    }

    public EntityMinecartChest(World par1, double par2, double par4, double par6)
    {
        super(par1, par2, par4, par6);
    }

    public void func_94095_a(DamageSource par1DamageSource)
    {
        super.func_94095_a(par1DamageSource);
        this.dropItemWithOffset(Block.chest.blockID, 1, 0.0F);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 27;
    }

    public int func_94087_l()
    {
        return 1;
    }

    public Block func_94093_n()
    {
        return Block.chest;
    }

    public int func_94085_r()
    {
        return 8;
    }
}
