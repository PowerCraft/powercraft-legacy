package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class EntityItemFrame extends EntityHanging
{
    private float field_82337_e = 1.0F;

    public EntityItemFrame(World par1World)
    {
        super(par1World);
    }

    public EntityItemFrame(World par1World, int par2, int par3, int par4, int par5)
    {
        super(par1World, par2, par3, par4, par5);
        this.func_82328_a(par5);
    }

    protected void entityInit()
    {
        this.getDataWatcher().func_82709_a(2, 5);
        this.getDataWatcher().addObject(3, Byte.valueOf((byte)0));
    }

    public int func_82329_d()
    {
        return 9;
    }

    public int func_82330_g()
    {
        return 9;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double par1)
    {
        double var3 = 16.0D;
        var3 *= 64.0D * this.renderDistanceWeight;
        return par1 < var3 * var3;
    }

    public void func_82331_h()
    {
        this.entityDropItem(new ItemStack(Item.field_82802_bI), 0.0F);

        if (this.func_82335_i() != null && this.rand.nextFloat() < this.field_82337_e)
        {
            this.func_82335_i().func_82842_a((EntityItemFrame)null);
            this.entityDropItem(this.func_82335_i(), 0.0F);
        }
    }

    public ItemStack func_82335_i()
    {
        return this.getDataWatcher().func_82710_f(2);
    }

    public void func_82334_a(ItemStack par1ItemStack)
    {
        par1ItemStack = par1ItemStack.copy();
        par1ItemStack.stackSize = 1;
        par1ItemStack.func_82842_a(this);
        this.getDataWatcher().updateObject(2, par1ItemStack);
        this.getDataWatcher().func_82708_h(2);
    }

    public int func_82333_j()
    {
        return this.getDataWatcher().getWatchableObjectByte(3);
    }

    public void func_82336_g(int par1)
    {
        this.getDataWatcher().updateObject(3, Byte.valueOf((byte)(par1 % 4)));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        if (this.func_82335_i() != null)
        {
            par1NBTTagCompound.setCompoundTag("Item", this.func_82335_i().writeToNBT(new NBTTagCompound()));
            par1NBTTagCompound.setByte("ItemRotation", (byte)this.func_82333_j());
            par1NBTTagCompound.setFloat("ItemDropChance", this.field_82337_e);
        }

        super.writeEntityToNBT(par1NBTTagCompound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        NBTTagCompound var2 = par1NBTTagCompound.getCompoundTag("Item");

        if (var2 != null && !var2.func_82582_d())
        {
            this.func_82334_a(ItemStack.loadItemStackFromNBT(var2));
            this.func_82336_g(par1NBTTagCompound.getByte("ItemRotation"));

            if (par1NBTTagCompound.hasKey("ItemDropChance"))
            {
                this.field_82337_e = par1NBTTagCompound.getFloat("ItemDropChance");
            }
        }

        super.readEntityFromNBT(par1NBTTagCompound);
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        if (this.func_82335_i() == null)
        {
            ItemStack var2 = par1EntityPlayer.getHeldItem();

            if (var2 != null && !this.worldObj.isRemote)
            {
                this.func_82334_a(var2);

                if (!par1EntityPlayer.capabilities.isCreativeMode && --var2.stackSize <= 0)
                {
                    par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
                }
            }
        }
        else if (!this.worldObj.isRemote)
        {
            this.func_82336_g(this.func_82333_j() + 1);
        }

        return true;
    }
}
