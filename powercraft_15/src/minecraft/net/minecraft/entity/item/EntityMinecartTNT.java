package net.minecraft.entity.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityMinecartTNT extends EntityMinecart
{
    private int field_94106_a = -1;

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

        if (this.field_94106_a > 0)
        {
            --this.field_94106_a;
            this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
        else if (this.field_94106_a == 0)
        {
            this.func_94103_c(this.motionX * this.motionX + this.motionZ * this.motionZ);
        }

        if (this.isCollidedHorizontally)
        {
            double d0 = this.motionX * this.motionX + this.motionZ * this.motionZ;

            if (d0 >= 0.009999999776482582D)
            {
                this.func_94103_c(d0);
            }
        }
    }

    public void func_94095_a(DamageSource par1DamageSource)
    {
        super.func_94095_a(par1DamageSource);
        double d0 = this.motionX * this.motionX + this.motionZ * this.motionZ;

        if (!par1DamageSource.func_94541_c())
        {
            this.entityDropItem(new ItemStack(Block.tnt, 1), 0.0F);
        }

        if (par1DamageSource.isFireDamage() || par1DamageSource.func_94541_c() || d0 >= 0.009999999776482582D)
        {
            this.func_94103_c(d0);
        }
    }

    protected void func_94103_c(double par1)
    {
        if (!this.worldObj.isRemote)
        {
            double d1 = Math.sqrt(par1);

            if (d1 > 5.0D)
            {
                d1 = 5.0D;
            }

            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(4.0D + this.rand.nextDouble() * 1.5D * d1), true);
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
            float f1 = par1 / 10.0F;
            this.func_94103_c((double)(f1 * f1));
        }

        super.fall(par1);
    }

    public void func_96095_a(int par1, int par2, int par3, boolean par4)
    {
        if (par4 && this.field_94106_a < 0)
        {
            this.func_94105_c();
        }
    }

    @SideOnly(Side.CLIENT)
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
        this.field_94106_a = 80;

        if (!this.worldObj.isRemote)
        {
            this.worldObj.setEntityState(this, (byte)10);
            this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 1.0F);
        }
    }

    @SideOnly(Side.CLIENT)
    public int func_94104_d()
    {
        return this.field_94106_a;
    }

    public boolean func_96096_ay()
    {
        return this.field_94106_a > -1;
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
            this.field_94106_a = par1NBTTagCompound.getInteger("TNTFuse");
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("TNTFuse", this.field_94106_a);
    }
}
