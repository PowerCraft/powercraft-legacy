package net.minecraft.src;

public class EntityMinecartFurnace extends EntityMinecart
{
    private int field_94110_c = 0;
    public double field_94111_a;
    public double field_94109_b;

    public EntityMinecartFurnace(World par1World)
    {
        super(par1World);
    }

    public EntityMinecartFurnace(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    public int func_94087_l()
    {
        return 2;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.field_94110_c > 0)
        {
            --this.field_94110_c;
        }

        if (this.field_94110_c <= 0)
        {
            this.field_94111_a = this.field_94109_b = 0.0D;
        }

        this.func_94107_f(this.field_94110_c > 0);

        if (this.func_94108_c() && this.rand.nextInt(4) == 0)
        {
            this.worldObj.spawnParticle("largesmoke", this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    public void func_94095_a(DamageSource par1DamageSource)
    {
        super.func_94095_a(par1DamageSource);

        if (!par1DamageSource.isExplosion())
        {
            this.entityDropItem(new ItemStack(Block.furnaceIdle, 1), 0.0F);
        }
    }

    protected void func_94091_a(int par1, int par2, int par3, double par4, double par6, int par8, int par9)
    {
        super.func_94091_a(par1, par2, par3, par4, par6, par8, par9);
        double var10 = this.field_94111_a * this.field_94111_a + this.field_94109_b * this.field_94109_b;

        if (var10 > 1.0E-4D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D)
        {
            var10 = (double)MathHelper.sqrt_double(var10);
            this.field_94111_a /= var10;
            this.field_94109_b /= var10;

            if (this.field_94111_a * this.motionX + this.field_94109_b * this.motionZ < 0.0D)
            {
                this.field_94111_a = 0.0D;
                this.field_94109_b = 0.0D;
            }
            else
            {
                this.field_94111_a = this.motionX;
                this.field_94109_b = this.motionZ;
            }
        }
    }

    protected void func_94101_h()
    {
        double var1 = this.field_94111_a * this.field_94111_a + this.field_94109_b * this.field_94109_b;

        if (var1 > 1.0E-4D)
        {
            var1 = (double)MathHelper.sqrt_double(var1);
            this.field_94111_a /= var1;
            this.field_94109_b /= var1;
            double var3 = 0.05D;
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.800000011920929D;
            this.motionX += this.field_94111_a * var3;
            this.motionZ += this.field_94109_b * var3;
        }
        else
        {
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.9800000190734863D;
        }

        super.func_94101_h();
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();

        if (var2 != null && var2.itemID == Item.coal.itemID)
        {
            if (--var2.stackSize == 0)
            {
                par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
            }

            this.field_94110_c += 3600;
        }

        this.field_94111_a = this.posX - par1EntityPlayer.posX;
        this.field_94109_b = this.posZ - par1EntityPlayer.posZ;
        return true;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setDouble("PushX", this.field_94111_a);
        par1NBTTagCompound.setDouble("PushZ", this.field_94109_b);
        par1NBTTagCompound.setShort("Fuel", (short)this.field_94110_c);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.field_94111_a = par1NBTTagCompound.getDouble("PushX");
        this.field_94109_b = par1NBTTagCompound.getDouble("PushZ");
        this.field_94110_c = par1NBTTagCompound.getShort("Fuel");
    }

    protected boolean func_94108_c()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    protected void func_94107_f(boolean par1)
    {
        if (par1)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) | 1)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) & -2)));
        }
    }

    public Block func_94093_n()
    {
        return Block.furnaceBurning;
    }

    public int func_94097_p()
    {
        return 2;
    }
}
