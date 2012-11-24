package net.minecraft.src;

public class EntityPig extends EntityAnimal
{
    private final EntityAIControlledByPlayer aiControlledByPlayer;

    public EntityPig(World par1World)
    {
        super(par1World);
        this.texture = "/mob/pig.png";
        this.setSize(0.9F, 0.9F);
        this.getNavigator().setAvoidsWater(true);
        float var2 = 0.25F;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
        this.tasks.addTask(2, this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.34F));
        this.tasks.addTask(3, new EntityAIMate(this, var2));
        this.tasks.addTask(4, new EntityAITempt(this, 0.3F, Item.carrotOnAStick.shiftedIndex, false));
        this.tasks.addTask(4, new EntityAITempt(this, 0.3F, Item.carrot.shiftedIndex, false));
        this.tasks.addTask(5, new EntityAIFollowParent(this, 0.28F));
        this.tasks.addTask(6, new EntityAIWander(this, var2));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    public boolean isAIEnabled()
    {
        return true;
    }

    public int getMaxHealth()
    {
        return 10;
    }

    protected void updateAITasks()
    {
        super.updateAITasks();
    }

    public boolean canBeSteered()
    {
        ItemStack var1 = ((EntityPlayer)this.riddenByEntity).getHeldItem();
        return var1 != null && var1.itemID == Item.carrotOnAStick.shiftedIndex;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("Saddle", this.getSaddled());
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.setSaddled(par1NBTTagCompound.getBoolean("Saddle"));
    }

    protected String getLivingSound()
    {
        return "mob.pig.say";
    }

    protected String getHurtSound()
    {
        return "mob.pig.say";
    }

    protected String getDeathSound()
    {
        return "mob.pig.death";
    }

    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.func_85030_a("mob.pig.step", 0.15F, 1.0F);
    }

    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        if (super.interact(par1EntityPlayer))
        {
            return true;
        }
        else if (this.getSaddled() && !this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == par1EntityPlayer))
        {
            par1EntityPlayer.mountEntity(this);
            return true;
        }
        else
        {
            return false;
        }
    }

    protected int getDropItemId()
    {
        return this.isBurning() ? Item.porkCooked.shiftedIndex : Item.porkRaw.shiftedIndex;
    }

    protected void dropFewItems(boolean par1, int par2)
    {
        int var3 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + par2);

        for (int var4 = 0; var4 < var3; ++var4)
        {
            if (this.isBurning())
            {
                this.dropItem(Item.porkCooked.shiftedIndex, 1);
            }
            else
            {
                this.dropItem(Item.porkRaw.shiftedIndex, 1);
            }
        }

        if (this.getSaddled())
        {
            this.dropItem(Item.saddle.shiftedIndex, 1);
        }
    }

    public boolean getSaddled()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setSaddled(boolean par1)
    {
        if (par1)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt)
    {
        if (!this.worldObj.isRemote)
        {
            EntityPigZombie var2 = new EntityPigZombie(this.worldObj);
            var2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.worldObj.spawnEntityInWorld(var2);
            this.setDead();
        }
    }

    protected void fall(float par1)
    {
        super.fall(par1);

        if (par1 > 5.0F && this.riddenByEntity instanceof EntityPlayer)
        {
            ((EntityPlayer)this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
        }
    }

    public EntityPig spawnBabyAnimal(EntityAgeable par1EntityAgeable)
    {
        return new EntityPig(this.worldObj);
    }

    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack != null && par1ItemStack.itemID == Item.carrot.shiftedIndex;
    }

    public EntityAIControlledByPlayer getAIControlledByPlayer()
    {
        return this.aiControlledByPlayer;
    }

    public EntityAgeable func_90011_a(EntityAgeable par1EntityAgeable)
    {
        return this.spawnBabyAnimal(par1EntityAgeable);
    }
}
