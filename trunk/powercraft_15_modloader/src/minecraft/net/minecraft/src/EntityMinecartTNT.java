package net.minecraft.src;

public class EntityMinecartTNT extends EntityMinecart
{
    private int minecartTNTFuse = -1;

    public EntityMinecartTNT(World par1)
    {
        super(par1);
    }

    public EntityMinecartTNT(World par1, double par2, double par4, double par6)
    {
        super(par1, par2, par4, par6);
    }

    public int func_94087_l()
    {
        return 3;
    }

    public Block func_94093_n()
    {
        return Block.tnt;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.minecartTNTFuse > 0)
        {
            --this.minecartTNTFuse;
            this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
        else if (this.minecartTNTFuse == 0)
        {
            this.func_94103_c(this.motionX * this.motionX + this.motionZ * this.motionZ);
        }

        if (this.isCollidedHorizontally)
        {
            double var1 = this.motionX * this.motionX + this.motionZ * this.motionZ;

            if (var1 >= 0.009999999776482582D)
            {
                this.func_94103_c(var1);
            }
        }
    }

    public void func_94095_a(DamageSource par1DamageSource)
    {
        super.func_94095_a(par1DamageSource);
        double var2 = this.motionX * this.motionX + this.motionZ * this.motionZ;

        if (!par1DamageSource.isExplosion())
        {
            this.entityDropItem(new ItemStack(Block.tnt, 1), 0.0F);
        }

        if (par1DamageSource.isFireDamage() || par1DamageSource.isExplosion() || var2 >= 0.009999999776482582D)
        {
            this.func_94103_c(var2);
        }
    }

    protected void func_94103_c(double par1)
    {
        if (!this.worldObj.isRemote)
        {
            double var3 = Math.sqrt(par1);

            if (var3 > 5.0D)
            {
                var3 = 5.0D;
            }

            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(4.0D + this.rand.nextDouble() * 1.5D * var3), true);
            this.setDead();
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1)
    {
        if (par1 >= 3.0F)
        {
            float var2 = par1 / 10.0F;
            this.func_94103_c((double)(var2 * var2));
        }

        super.fall(par1);
    }

    public void func_96095_a(int par1, int par2, int par3, boolean par4)
    {
        if (par4 && this.minecartTNTFuse < 0)
        {
            this.func_94105_c();
        }
    }

    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 10)
        {
            this.func_94105_c();
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }

    public void func_94105_c()
    {
        this.minecartTNTFuse = 80;

        if (!this.worldObj.isRemote)
        {
            this.worldObj.setEntityState(this, (byte)10);
            this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 1.0F);
        }
    }

    public int func_94104_d()
    {
        return this.minecartTNTFuse;
    }

    public boolean func_96096_ay()
    {
        return this.minecartTNTFuse > -1;
    }

    public float func_82146_a(Explosion par1Explosion, World par2World, int par3, int par4, int par5, Block par6Block)
    {
        return this.func_96096_ay() && (BlockRailBase.isRailBlock(par6Block.blockID) || BlockRailBase.isRailBlockAt(par2World, par3, par4 + 1, par5)) ? 0.0F : super.func_82146_a(par1Explosion, par2World, par3, par4, par5, par6Block);
    }

    public boolean func_96091_a(Explosion par1Explosion, World par2World, int par3, int par4, int par5, int par6, float par7)
    {
        return this.func_96096_ay() && (BlockRailBase.isRailBlock(par6) || BlockRailBase.isRailBlockAt(par2World, par3, par4 + 1, par5)) ? false : super.func_96091_a(par1Explosion, par2World, par3, par4, par5, par6, par7);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("TNTFuse"))
        {
            this.minecartTNTFuse = par1NBTTagCompound.getInteger("TNTFuse");
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("TNTFuse", this.minecartTNTFuse);
    }
}
