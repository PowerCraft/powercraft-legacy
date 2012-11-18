package net.minecraft.src;

public abstract class EntityAgeable extends EntityCreature
{
    public EntityAgeable(World par1World)
    {
        super(par1World);
    }

    public abstract EntityAgeable func_90011_a(EntityAgeable var1);

    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();

        if (var2 != null && var2.itemID == Item.monsterPlacer.shiftedIndex && !this.worldObj.isRemote)
        {
            Class var3 = EntityList.func_90035_a(var2.getItemDamage());

            if (var3 != null && var3.isAssignableFrom(this.getClass()))
            {
                EntityAgeable var4 = this.func_90011_a(this);

                if (var4 != null)
                {
                    var4.setGrowingAge(-24000);
                    var4.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
                    this.worldObj.spawnEntityInWorld(var4);
                }
            }
        }

        return super.interact(par1EntityPlayer);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(12, new Integer(0));
    }

    public int getGrowingAge()
    {
        return this.dataWatcher.getWatchableObjectInt(12);
    }

    public void setGrowingAge(int par1)
    {
        this.dataWatcher.updateObject(12, Integer.valueOf(par1));
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("Age", this.getGrowingAge());
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.setGrowingAge(par1NBTTagCompound.getInteger("Age"));
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        int var1 = this.getGrowingAge();

        if (var1 < 0)
        {
            ++var1;
            this.setGrowingAge(var1);
        }
        else if (var1 > 0)
        {
            --var1;
            this.setGrowingAge(var1);
        }
    }

    public boolean isChild()
    {
        return this.getGrowingAge() < 0;
    }
}
