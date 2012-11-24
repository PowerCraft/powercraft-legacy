package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;

public class EntityPigZombie extends EntityZombie
{
    private int angerLevel = 0;

    private int randomSoundDelay = 0;

    public EntityPigZombie(World par1World)
    {
        super(par1World);
        this.texture = "/mob/pigzombie.png";
        this.moveSpeed = 0.5F;
        this.isImmuneToFire = true;
    }

    protected boolean isAIEnabled()
    {
        return false;
    }

    public void onUpdate()
    {
        this.moveSpeed = this.entityToAttack != null ? 0.95F : 0.5F;

        if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0)
        {
            this.func_85030_a("mob.zombiepig.zpigangry", this.getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }

        super.onUpdate();
    }

    @SideOnly(Side.CLIENT)

    public String getTexture()
    {
        return "/mob/pigzombie.png";
    }

    public boolean getCanSpawnHere()
    {
        return this.worldObj.difficultySetting > 0 && this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox);
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("Anger", (short)this.angerLevel);
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.angerLevel = par1NBTTagCompound.getShort("Anger");
    }

    protected Entity findPlayerToAttack()
    {
        return this.angerLevel == 0 ? null : super.findPlayerToAttack();
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (this.func_85032_ar())
        {
            return false;
        }
        else
        {
            Entity var3 = par1DamageSource.getEntity();

            if (var3 instanceof EntityPlayer)
            {
                List var4 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0D, 32.0D, 32.0D));

                for (int var5 = 0; var5 < var4.size(); ++var5)
                {
                    Entity var6 = (Entity)var4.get(var5);

                    if (var6 instanceof EntityPigZombie)
                    {
                        EntityPigZombie var7 = (EntityPigZombie)var6;
                        var7.becomeAngryAt(var3);
                    }
                }

                this.becomeAngryAt(var3);
            }

            return super.attackEntityFrom(par1DamageSource, par2);
        }
    }

    private void becomeAngryAt(Entity par1Entity)
    {
        this.entityToAttack = par1Entity;
        this.angerLevel = 400 + this.rand.nextInt(400);
        this.randomSoundDelay = this.rand.nextInt(40);
    }

    protected String getLivingSound()
    {
        return "mob.zombiepig.zpig";
    }

    protected String getHurtSound()
    {
        return "mob.zombiepig.zpighurt";
    }

    protected String getDeathSound()
    {
        return "mob.zombiepig.zpigdeath";
    }

    protected void dropFewItems(boolean par1, int par2)
    {
        int var3 = this.rand.nextInt(2 + par2);
        int var4;

        for (var4 = 0; var4 < var3; ++var4)
        {
            this.dropItem(Item.rottenFlesh.shiftedIndex, 1);
        }

        var3 = this.rand.nextInt(2 + par2);

        for (var4 = 0; var4 < var3; ++var4)
        {
            this.dropItem(Item.goldNugget.shiftedIndex, 1);
        }
    }

    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        return false;
    }

    protected void dropRareDrop(int par1)
    {
        this.dropItem(Item.ingotGold.shiftedIndex, 1);
    }

    protected int getDropItemId()
    {
        return Item.rottenFlesh.shiftedIndex;
    }

    protected void func_82164_bB()
    {
        this.setCurrentItemOrArmor(0, new ItemStack(Item.swordGold));
    }

    public void initCreature()
    {
        super.initCreature();
        this.setVillager(false);
    }

    public int getAttackStrength(Entity par1Entity)
    {
        ItemStack var2 = this.getHeldItem();
        int var3 = 5;

        if (var2 != null)
        {
            var3 += var2.getDamageVsEntity(this);
        }

        return var3;
    }
}
