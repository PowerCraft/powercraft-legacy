package net.minecraft.src;

import java.util.List;

public class EntityMinecartHopper extends EntityMinecartContainer implements Hopper
{
    private boolean field_96113_a = true;
    private int field_98044_b = -1;

    public EntityMinecartHopper(World par1World)
    {
        super(par1World);
    }

    public EntityMinecartHopper(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    public int func_94087_l()
    {
        return 5;
    }

    public Block func_94093_n()
    {
        return Block.hopperBlock;
    }

    public int func_94085_r()
    {
        return 1;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 5;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        if (!this.worldObj.isRemote)
        {
            par1EntityPlayer.func_96125_a(this);
        }

        return true;
    }

    public void func_96095_a(int par1, int par2, int par3, boolean par4)
    {
        boolean var5 = !par4;

        if (var5 != this.func_96111_ay())
        {
            this.func_96110_f(var5);
        }
    }

    public boolean func_96111_ay()
    {
        return this.field_96113_a;
    }

    public void func_96110_f(boolean par1)
    {
        this.field_96113_a = par1;
    }

    /**
     * Returns the worldObj for this tileEntity.
     */
    public World getWorldObj()
    {
        return this.worldObj;
    }

    public double func_96107_aA()
    {
        return this.posX;
    }

    public double func_96109_aB()
    {
        return this.posY;
    }

    public double func_96108_aC()
    {
        return this.posZ;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote && this.isEntityAlive() && this.func_96111_ay())
        {
            --this.field_98044_b;

            if (!this.func_98043_aE())
            {
                this.func_98042_n(0);

                if (this.func_96112_aD())
                {
                    this.func_98042_n(4);
                    this.onInventoryChanged();
                }
            }
        }
    }

    public boolean func_96112_aD()
    {
        if (TileEntityHopper.func_96116_a(this))
        {
            return true;
        }
        else
        {
            List var1 = this.worldObj.selectEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(0.25D, 0.0D, 0.25D), IEntitySelector.field_94557_a);

            if (var1.size() > 0)
            {
                TileEntityHopper.func_96114_a(this, (EntityItem)var1.get(0));
            }

            return false;
        }
    }

    public void func_94095_a(DamageSource par1DamageSource)
    {
        super.func_94095_a(par1DamageSource);
        this.dropItemWithOffset(Block.hopperBlock.blockID, 1, 0.0F);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("TransferCooldown", this.field_98044_b);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.field_98044_b = par1NBTTagCompound.getInteger("TransferCooldown");
    }

    public void func_98042_n(int par1)
    {
        this.field_98044_b = par1;
    }

    public boolean func_98043_aE()
    {
        return this.field_98044_b > 0;
    }
}
