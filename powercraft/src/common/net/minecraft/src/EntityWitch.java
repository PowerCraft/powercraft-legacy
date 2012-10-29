package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Iterator;
import java.util.List;

public class EntityWitch extends EntityMob implements IRangedAttackMob
{
    private static final int[] field_82199_d = new int[] {Item.lightStoneDust.shiftedIndex, Item.sugar.shiftedIndex, Item.redstone.shiftedIndex, Item.spiderEye.shiftedIndex, Item.glassBottle.shiftedIndex, Item.gunpowder.shiftedIndex, Item.stick.shiftedIndex, Item.stick.shiftedIndex};
    private int field_82200_e = 0;

    public EntityWitch(World par1World)
    {
        super(par1World);
        this.texture = "/mob/villager/witch.png";
        this.moveSpeed = 0.25F;
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, this.moveSpeed, 60, 10.0F));
        this.tasks.addTask(2, new EntityAIWander(this, this.moveSpeed));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
    }

    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(21, Byte.valueOf((byte)0));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.witch.idle";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.witch.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.witch.death";
    }

    public void func_82197_f(boolean par1)
    {
        this.getDataWatcher().updateObject(21, Byte.valueOf((byte)(par1 ? 1 : 0)));
    }

    public boolean func_82198_m()
    {
        return this.getDataWatcher().getWatchableObjectByte(21) == 1;
    }

    public int getMaxHealth()
    {
        return 26;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (!this.worldObj.isRemote)
        {
            if (this.func_82198_m())
            {
                if (this.field_82200_e-- <= 0)
                {
                    this.func_82197_f(false);
                    ItemStack var1 = this.getHeldItem();
                    this.func_70062_b(0, (ItemStack)null);

                    if (var1 != null && var1.itemID == Item.potion.shiftedIndex)
                    {
                        List var2 = Item.potion.getEffects(var1);

                        if (var2 != null)
                        {
                            Iterator var3 = var2.iterator();

                            while (var3.hasNext())
                            {
                                PotionEffect var4 = (PotionEffect)var3.next();
                                this.addPotionEffect(new PotionEffect(var4));
                            }
                        }
                    }
                }
            }
            else
            {
                short var5 = -1;

                if (this.rand.nextFloat() < 0.15F && this.isBurning() && !this.isPotionActive(Potion.fireResistance))
                {
                    var5 = 16307;
                }
                else if (this.rand.nextFloat() < 0.05F && this.health < this.getMaxHealth())
                {
                    var5 = 16341;
                }
                else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D)
                {
                    var5 = 16274;
                }
                else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D)
                {
                    var5 = 16274;
                }

                if (var5 > -1)
                {
                    this.func_70062_b(0, new ItemStack(Item.potion, 1, var5));
                    this.field_82200_e = this.getHeldItem().getMaxItemUseDuration();
                    this.func_82197_f(true);
                }
            }

            if (this.rand.nextFloat() < 7.5E-4F)
            {
                this.worldObj.setEntityState(this, (byte)15);
            }
        }

        super.onLivingUpdate();
    }

    /**
     * Reduces damage, depending on potions
     */
    protected int applyPotionDamageCalculations(DamageSource par1DamageSource, int par2)
    {
        par2 = super.applyPotionDamageCalculations(par1DamageSource, par2);

        if (par1DamageSource.getEntity() == this)
        {
            par2 = 0;
        }

        if (par1DamageSource.func_82725_o())
        {
            par2 = (int)((double)par2 * 0.15D);
        }

        return par2;
    }

    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 15)
        {
            for (int var2 = 0; var2 < this.rand.nextInt(35) + 10; ++var2)
            {
                this.worldObj.spawnParticle("witchMagic", this.posX + this.rand.nextGaussian() * 0.12999999523162842D, this.boundingBox.maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D);
            }
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }

    /**
     * This method returns a value to be applied directly to entity speed, this factor is less than 1 when a slowdown
     * potion effect is applied, more than 1 when a haste potion effect is applied and 2 for fleeing entities.
     */
    public float getSpeedModifier()
    {
        float var1 = super.getSpeedModifier();

        if (this.func_82198_m())
        {
            var1 *= 0.75F;
        }

        return var1;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        int var3 = this.rand.nextInt(3) + 1;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            int var5 = this.rand.nextInt(3);
            int var6 = field_82199_d[this.rand.nextInt(field_82199_d.length)];

            if (par2 > 0)
            {
                var5 += this.rand.nextInt(par2 + 1);
            }

            for (int var7 = 0; var7 < var5; ++var7)
            {
                this.dropItem(var6, 1);
            }
        }
    }

    public void func_82196_d(EntityLiving par1EntityLiving)
    {
        if (!this.func_82198_m())
        {
            EntityPotion var2 = new EntityPotion(this.worldObj, this, 32732);
            var2.rotationPitch -= -20.0F;
            double var3 = par1EntityLiving.posX + par1EntityLiving.motionX - this.posX;
            double var5 = par1EntityLiving.posY + (double)par1EntityLiving.getEyeHeight() - 1.100000023841858D - this.posY;
            double var7 = par1EntityLiving.posZ + par1EntityLiving.motionZ - this.posZ;
            float var9 = MathHelper.sqrt_double(var3 * var3 + var7 * var7);

            if (var9 >= 8.0F && !par1EntityLiving.isPotionActive(Potion.moveSlowdown))
            {
                var2.func_82340_a(32698);
            }
            else if (par1EntityLiving.getHealth() >= 8 && !par1EntityLiving.isPotionActive(Potion.poison))
            {
                var2.func_82340_a(32660);
            }
            else if (var9 <= 3.0F && !par1EntityLiving.isPotionActive(Potion.weakness) && this.rand.nextFloat() < 0.25F)
            {
                var2.func_82340_a(32696);
            }

            var2.setThrowableHeading(var3, var5 + (double)(var9 * 0.2F), var7, 0.75F, 8.0F);
            this.worldObj.spawnEntityInWorld(var2);
        }
    }
}
