package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.common.IMinecartCollisionHandler;
import net.minecraftforge.common.MinecartRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;

public class EntityMinecart extends Entity implements IInventory
{
    protected ItemStack[] cargoItems;
    protected int fuel;
    protected boolean field_70499_f;

    public int minecartType;
    public double pushX;
    public double pushZ;
    protected final IUpdatePlayerListBox field_82344_g;
    protected boolean field_82345_h;
    protected static final int[][][] field_70500_g = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};

    protected int turnProgress;
    protected double minecartX;
    protected double minecartY;
    protected double minecartZ;
    protected double minecartYaw;
    protected double minecartPitch;
    @SideOnly(Side.CLIENT)
    protected double velocityX;
    @SideOnly(Side.CLIENT)
    protected double velocityY;
    @SideOnly(Side.CLIENT)
    protected double velocityZ;

    public static float defaultMaxSpeedRail = 0.4f;
    public static float defaultMaxSpeedGround = 0.4f;
    public static float defaultMaxSpeedAirLateral = 0.4f;
    public static float defaultMaxSpeedAirVertical = -1f;
    public static double defaultDragRidden = 0.996999979019165D;
    public static double defaultDragEmpty = 0.9599999785423279D;
    public static double defaultDragAir = 0.94999998807907104D;
    protected boolean canUseRail = true;
    protected boolean canBePushed = true;
    private static IMinecartCollisionHandler collisionHandler = null;

    protected float maxSpeedRail;
    protected float maxSpeedGround;
    protected float maxSpeedAirLateral;
    protected float maxSpeedAirVertical;
    protected double dragAir;

    public EntityMinecart(World par1World)
    {
        super(par1World);
        this.cargoItems = new ItemStack[36];
        this.fuel = 0;
        this.field_70499_f = false;
        this.field_82345_h = true;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.7F);
        this.yOffset = this.height / 2.0F;
        this.field_82344_g = par1World != null ? par1World.func_82735_a(this) : null;
        maxSpeedRail = defaultMaxSpeedRail;
        maxSpeedGround = defaultMaxSpeedGround;
        maxSpeedAirLateral = defaultMaxSpeedAirLateral;
        maxSpeedAirVertical = defaultMaxSpeedAirVertical;
        dragAir = defaultDragAir;
    }

    public EntityMinecart(World world, int type)
    {
        this(world);
        minecartType = type;
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(16, new Byte((byte)0));
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Integer(0));
    }

    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        if (getCollisionHandler() != null)
        {
            return getCollisionHandler().getCollisionBox(this, par1Entity);
        }

        return par1Entity.canBePushed() ? par1Entity.boundingBox : null;
    }

    public AxisAlignedBB getBoundingBox()
    {
        if (getCollisionHandler() != null)
        {
            return getCollisionHandler().getBoundingBox(this);
        }

        return null;
    }

    public boolean canBePushed()
    {
        return canBePushed;
    }

    public EntityMinecart(World par1World, double par2, double par4, double par6, int par8)
    {
        this(par1World);
        this.setPosition(par2, par4 + (double)this.yOffset, par6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
        this.minecartType = par8;
    }

    public double getMountedYOffset()
    {
        return (double)this.height * 0.0D - 0.30000001192092896D;
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (!this.worldObj.isRemote && !this.isDead)
        {
            if (this.func_85032_ar())
            {
                return false;
            }
            else
            {
                this.func_70494_i(-this.func_70493_k());
                this.func_70497_h(10);
                this.setBeenAttacked();
                this.setDamage(this.getDamage() + par2 * 10);

                if (par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode)
                {
                    this.setDamage(100);
                }

                if (this.getDamage() > 40)
                {
                    if (this.riddenByEntity != null)
                    {
                        this.riddenByEntity.mountEntity(this);
                    }

                    this.setDead();
                    dropCartAsItem();
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    @SideOnly(Side.CLIENT)

    public void performHurtAnimation()
    {
        this.func_70494_i(-this.func_70493_k());
        this.func_70497_h(10);
        this.setDamage(this.getDamage() + this.getDamage() * 10);
    }

    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    public void setDead()
    {
        if (this.field_82345_h)
        {
            for (int var1 = 0; var1 < this.getSizeInventory(); ++var1)
            {
                ItemStack var2 = this.getStackInSlot(var1);

                if (var2 != null)
                {
                    float var3 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var4 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var5 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (var2.stackSize > 0)
                    {
                        int var6 = this.rand.nextInt(21) + 10;

                        if (var6 > var2.stackSize)
                        {
                            var6 = var2.stackSize;
                        }

                        var2.stackSize -= var6;
                        EntityItem var7 = new EntityItem(this.worldObj, this.posX + (double)var3, this.posY + (double)var4, this.posZ + (double)var5, new ItemStack(var2.itemID, var6, var2.getItemDamage()));

                        if (var2.hasTagCompound())
                        {
                            var7.item.setTagCompound((NBTTagCompound)var2.getTagCompound().copy());
                        }

                        float var8 = 0.05F;
                        var7.motionX = (double)((float)this.rand.nextGaussian() * var8);
                        var7.motionY = (double)((float)this.rand.nextGaussian() * var8 + 0.2F);
                        var7.motionZ = (double)((float)this.rand.nextGaussian() * var8);
                        this.worldObj.spawnEntityInWorld(var7);
                    }
                }
            }
        }

        super.setDead();

        if (this.field_82344_g != null)
        {
            this.field_82344_g.update();
        }
    }

    public void travelToDimension(int par1)
    {
        this.field_82345_h = false;
        super.travelToDimension(par1);
    }

    public void onUpdate()
    {
        if (this.field_82344_g != null)
        {
            this.field_82344_g.update();
        }

        if (this.func_70496_j() > 0)
        {
            this.func_70497_h(this.func_70496_j() - 1);
        }

        if (this.getDamage() > 0)
        {
            this.setDamage(this.getDamage() - 1);
        }

        if (this.posY < -64.0D)
        {
            this.kill();
        }

        if (this.isMinecartPowered() && this.rand.nextInt(4) == 0 && minecartType == 2 && getClass() == EntityMinecart.class)
        {
            this.worldObj.spawnParticle("largesmoke", this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D);
        }

        if (this.worldObj.isRemote)
        {
            if (this.turnProgress > 0)
            {
                double var45 = this.posX + (this.minecartX - this.posX) / (double)this.turnProgress;
                double var46 = this.posY + (this.minecartY - this.posY) / (double)this.turnProgress;
                double var5 = this.posZ + (this.minecartZ - this.posZ) / (double)this.turnProgress;
                double var7 = MathHelper.wrapAngleTo180_double(this.minecartYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.turnProgress);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.turnProgress);
                --this.turnProgress;
                this.setPosition(var45, var46, var5);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033D;
            int var1 = MathHelper.floor_double(this.posX);
            int var2 = MathHelper.floor_double(this.posY);
            int var3 = MathHelper.floor_double(this.posZ);

            if (BlockRail.isRailBlockAt(this.worldObj, var1, var2 - 1, var3))
            {
                --var2;
            }

            double var4 = 0.4D;
            double var6 = 0.0078125D;
            int var8 = this.worldObj.getBlockId(var1, var2, var3);

            if (canUseRail() && BlockRail.isRailBlock(var8))
            {
                this.fallDistance = 0.0F;
                Vec3 var9 = this.func_70489_a(this.posX, this.posY, this.posZ);
                int var10 = ((BlockRail)Block.blocksList[var8]).getBasicRailMetadata(worldObj, this, var1, var2, var3);
                this.posY = (double)var2;
                boolean var11 = false;
                boolean var12 = false;

                if (var8 == Block.railPowered.blockID)
                {
                    var11 = (worldObj.getBlockMetadata(var1, var2, var3) & 8) != 0;
                    var12 = !var11;
                }

                if (((BlockRail)Block.blocksList[var8]).isPowered())
                {
                    var10 &= 7;
                }

                if (var10 >= 2 && var10 <= 5)
                {
                    this.posY = (double)(var2 + 1);
                }

                adjustSlopeVelocities(var10);
                int[][] var13 = field_70500_g[var10];
                double var14 = (double)(var13[1][0] - var13[0][0]);
                double var16 = (double)(var13[1][2] - var13[0][2]);
                double var18 = Math.sqrt(var14 * var14 + var16 * var16);
                double var20 = this.motionX * var14 + this.motionZ * var16;

                if (var20 < 0.0D)
                {
                    var14 = -var14;
                    var16 = -var16;
                }

                double var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.motionX = var22 * var14 / var18;
                this.motionZ = var22 * var16 / var18;
                double var24;
                double var26;

                if (this.riddenByEntity != null)
                {
                    var24 = this.riddenByEntity.motionX * this.riddenByEntity.motionX + this.riddenByEntity.motionZ * this.riddenByEntity.motionZ;
                    var26 = this.motionX * this.motionX + this.motionZ * this.motionZ;

                    if (var24 > 1.0E-4D && var26 < 0.01D)
                    {
                        this.motionX += this.riddenByEntity.motionX * 0.1D;
                        this.motionZ += this.riddenByEntity.motionZ * 0.1D;
                        var12 = false;
                    }
                }

                if (var12 && shouldDoRailFunctions())
                {
                    var24 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                    if (var24 < 0.03D)
                    {
                        this.motionX *= 0.0D;
                        this.motionY *= 0.0D;
                        this.motionZ *= 0.0D;
                    }
                    else
                    {
                        this.motionX *= 0.5D;
                        this.motionY *= 0.0D;
                        this.motionZ *= 0.5D;
                    }
                }

                var24 = 0.0D;
                var26 = (double)var1 + 0.5D + (double)var13[0][0] * 0.5D;
                double var28 = (double)var3 + 0.5D + (double)var13[0][2] * 0.5D;
                double var30 = (double)var1 + 0.5D + (double)var13[1][0] * 0.5D;
                double var32 = (double)var3 + 0.5D + (double)var13[1][2] * 0.5D;
                var14 = var30 - var26;
                var16 = var32 - var28;
                double var34;
                double var36;

                if (var14 == 0.0D)
                {
                    this.posX = (double)var1 + 0.5D;
                    var24 = this.posZ - (double)var3;
                }
                else if (var16 == 0.0D)
                {
                    this.posZ = (double)var3 + 0.5D;
                    var24 = this.posX - (double)var1;
                }
                else
                {
                    var34 = this.posX - var26;
                    var36 = this.posZ - var28;
                    var24 = (var34 * var14 + var36 * var16) * 2.0D;
                }

                this.posX = var26 + var14 * var24;
                this.posZ = var28 + var16 * var24;
                this.setPosition(this.posX, this.posY + (double)this.yOffset, this.posZ);
                moveMinecartOnRail(var1, var2, var3);

                if (var13[0][1] != 0 && MathHelper.floor_double(this.posX) - var1 == var13[0][0] && MathHelper.floor_double(this.posZ) - var3 == var13[0][2])
                {
                    this.setPosition(this.posX, this.posY + (double)var13[0][1], this.posZ);
                }
                else if (var13[1][1] != 0 && MathHelper.floor_double(this.posX) - var1 == var13[1][0] && MathHelper.floor_double(this.posZ) - var3 == var13[1][2])
                {
                    this.setPosition(this.posX, this.posY + (double)var13[1][1], this.posZ);
                }

                applyDragAndPushForces();
                Vec3 var52 = this.func_70489_a(this.posX, this.posY, this.posZ);

                if (var52 != null && var9 != null)
                {
                    double var39 = (var9.yCoord - var52.yCoord) * 0.05D;
                    var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                    if (var22 > 0.0D)
                    {
                        this.motionX = this.motionX / var22 * (var22 + var39);
                        this.motionZ = this.motionZ / var22 * (var22 + var39);
                    }

                    this.setPosition(this.posX, var52.yCoord, this.posZ);
                }

                int var51 = MathHelper.floor_double(this.posX);
                int var53 = MathHelper.floor_double(this.posZ);

                if (var51 != var1 || var53 != var3)
                {
                    var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    this.motionX = var22 * (double)(var51 - var1);
                    this.motionZ = var22 * (double)(var53 - var3);
                }

                double var41;
                updatePushForces();

                if (shouldDoRailFunctions())
                {
                    ((BlockRail)Block.blocksList[var8]).onMinecartPass(worldObj, this, var1, var2, var3);
                }

                if (var11 && shouldDoRailFunctions())
                {
                    var41 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                    if (var41 > 0.01D)
                    {
                        double var43 = 0.06D;
                        this.motionX += this.motionX / var41 * var43;
                        this.motionZ += this.motionZ / var41 * var43;
                    }
                    else if (var10 == 1)
                    {
                        if (this.worldObj.isBlockNormalCube(var1 - 1, var2, var3))
                        {
                            this.motionX = 0.02D;
                        }
                        else if (this.worldObj.isBlockNormalCube(var1 + 1, var2, var3))
                        {
                            this.motionX = -0.02D;
                        }
                    }
                    else if (var10 == 0)
                    {
                        if (this.worldObj.isBlockNormalCube(var1, var2, var3 - 1))
                        {
                            this.motionZ = 0.02D;
                        }
                        else if (this.worldObj.isBlockNormalCube(var1, var2, var3 + 1))
                        {
                            this.motionZ = -0.02D;
                        }
                    }
                }
            }
            else
            {
                moveMinecartOffRail(var1, var2, var3);
            }

            this.doBlockCollisions();
            this.rotationPitch = 0.0F;
            double var47 = this.prevPosX - this.posX;
            double var48 = this.prevPosZ - this.posZ;

            if (var47 * var47 + var48 * var48 > 0.001D)
            {
                this.rotationYaw = (float)(Math.atan2(var48, var47) * 180.0D / Math.PI);

                if (this.field_70499_f)
                {
                    this.rotationYaw += 180.0F;
                }
            }

            double var49 = (double)MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw);

            if (var49 < -170.0D || var49 >= 170.0D)
            {
                this.rotationYaw += 180.0F;
                this.field_70499_f = !this.field_70499_f;
            }

            this.setRotation(this.rotationYaw, this.rotationPitch);
            AxisAlignedBB box = null;

            if (getCollisionHandler() != null)
            {
                box = getCollisionHandler().getMinecartCollisionBox(this);
            }
            else
            {
                box = boundingBox.expand(0.2D, 0.0D, 0.2D);
            }

            List var15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box);

            if (var15 != null && !var15.isEmpty())
            {
                for (int var50 = 0; var50 < var15.size(); ++var50)
                {
                    Entity var17 = (Entity)var15.get(var50);

                    if (var17 != this.riddenByEntity && var17.canBePushed() && var17 instanceof EntityMinecart)
                    {
                        var17.applyEntityCollision(this);
                    }
                }
            }

            if (this.riddenByEntity != null && this.riddenByEntity.isDead)
            {
                if (this.riddenByEntity.ridingEntity == this)
                {
                    this.riddenByEntity.ridingEntity = null;
                }

                this.riddenByEntity = null;
            }

            updateFuel();
            MinecraftForge.EVENT_BUS.post(new MinecartUpdateEvent(this, var1, var2, var3));
        }
    }

    @SideOnly(Side.CLIENT)
    public Vec3 func_70495_a(double par1, double par3, double par5, double par7)
    {
        int var9 = MathHelper.floor_double(par1);
        int var10 = MathHelper.floor_double(par3);
        int var11 = MathHelper.floor_double(par5);

        if (BlockRail.isRailBlockAt(this.worldObj, var9, var10 - 1, var11))
        {
            --var10;
        }

        int var12 = this.worldObj.getBlockId(var9, var10, var11);

        if (!BlockRail.isRailBlock(var12))
        {
            return null;
        }
        else
        {
            int var13 = ((BlockRail)Block.blocksList[var12]).getBasicRailMetadata(worldObj, this, var9, var10, var11);
            par3 = (double)var10;

            if (var13 >= 2 && var13 <= 5)
            {
                par3 = (double)(var10 + 1);
            }

            int[][] var14 = field_70500_g[var13];
            double var15 = (double)(var14[1][0] - var14[0][0]);
            double var17 = (double)(var14[1][2] - var14[0][2]);
            double var19 = Math.sqrt(var15 * var15 + var17 * var17);
            var15 /= var19;
            var17 /= var19;
            par1 += var15 * par7;
            par5 += var17 * par7;

            if (var14[0][1] != 0 && MathHelper.floor_double(par1) - var9 == var14[0][0] && MathHelper.floor_double(par5) - var11 == var14[0][2])
            {
                par3 += (double)var14[0][1];
            }
            else if (var14[1][1] != 0 && MathHelper.floor_double(par1) - var9 == var14[1][0] && MathHelper.floor_double(par5) - var11 == var14[1][2])
            {
                par3 += (double)var14[1][1];
            }

            return this.func_70489_a(par1, par3, par5);
        }
    }

    public Vec3 func_70489_a(double par1, double par3, double par5)
    {
        int var7 = MathHelper.floor_double(par1);
        int var8 = MathHelper.floor_double(par3);
        int var9 = MathHelper.floor_double(par5);

        if (BlockRail.isRailBlockAt(this.worldObj, var7, var8 - 1, var9))
        {
            --var8;
        }

        int var10 = this.worldObj.getBlockId(var7, var8, var9);

        if (BlockRail.isRailBlock(var10))
        {
            int var11 = ((BlockRail)Block.blocksList[var10]).getBasicRailMetadata(worldObj, this, var7, var8, var9);
            par3 = (double)var8;

            if (var11 >= 2 && var11 <= 5)
            {
                par3 = (double)(var8 + 1);
            }

            int[][] var12 = field_70500_g[var11];
            double var13 = 0.0D;
            double var15 = (double)var7 + 0.5D + (double)var12[0][0] * 0.5D;
            double var17 = (double)var8 + 0.5D + (double)var12[0][1] * 0.5D;
            double var19 = (double)var9 + 0.5D + (double)var12[0][2] * 0.5D;
            double var21 = (double)var7 + 0.5D + (double)var12[1][0] * 0.5D;
            double var23 = (double)var8 + 0.5D + (double)var12[1][1] * 0.5D;
            double var25 = (double)var9 + 0.5D + (double)var12[1][2] * 0.5D;
            double var27 = var21 - var15;
            double var29 = (var23 - var17) * 2.0D;
            double var31 = var25 - var19;

            if (var27 == 0.0D)
            {
                par1 = (double)var7 + 0.5D;
                var13 = par5 - (double)var9;
            }
            else if (var31 == 0.0D)
            {
                par5 = (double)var9 + 0.5D;
                var13 = par1 - (double)var7;
            }
            else
            {
                double var33 = par1 - var15;
                double var35 = par5 - var19;
                var13 = (var33 * var27 + var35 * var31) * 2.0D;
            }

            par1 = var15 + var27 * var13;
            par3 = var17 + var29 * var13;
            par5 = var19 + var31 * var13;

            if (var29 < 0.0D)
            {
                ++par3;
            }

            if (var29 > 0.0D)
            {
                par3 += 0.5D;
            }

            return this.worldObj.getWorldVec3Pool().getVecFromPool(par1, par3, par5);
        }
        else
        {
            return null;
        }
    }

    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setInteger("Type", this.minecartType);

        if (isPoweredCart())
        {
            par1NBTTagCompound.setDouble("PushX", this.pushX);
            par1NBTTagCompound.setDouble("PushZ", this.pushZ);
            par1NBTTagCompound.setInteger("Fuel", this.fuel);
        }

        if (getSizeInventory() > 0)
        {
            NBTTagList var2 = new NBTTagList();

            for (int var3 = 0; var3 < this.cargoItems.length; ++var3)
            {
                if (this.cargoItems[var3] != null)
                {
                    NBTTagCompound var4 = new NBTTagCompound();
                    var4.setByte("Slot", (byte)var3);
                    this.cargoItems[var3].writeToNBT(var4);
                    var2.appendTag(var4);
                }
            }

            par1NBTTagCompound.setTag("Items", var2);
        }
    }

    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.minecartType = par1NBTTagCompound.getInteger("Type");

        if (isPoweredCart())
        {
            this.pushX = par1NBTTagCompound.getDouble("PushX");
            this.pushZ = par1NBTTagCompound.getDouble("PushZ");

            try
            {
                this.fuel = par1NBTTagCompound.getInteger("Fuel");
            }
            catch (ClassCastException e)
            {
                this.fuel = par1NBTTagCompound.getShort("Fuel");
            }
        }

        if (getSizeInventory() > 0)
        {
            NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
            this.cargoItems = new ItemStack[this.getSizeInventory()];

            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
                int var5 = var4.getByte("Slot") & 255;

                if (var5 >= 0 && var5 < this.cargoItems.length)
                {
                    this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    public void applyEntityCollision(Entity par1Entity)
    {
        MinecraftForge.EVENT_BUS.post(new MinecartCollisionEvent(this, par1Entity));

        if (getCollisionHandler() != null)
        {
            getCollisionHandler().onEntityCollision(this, par1Entity);
            return;
        }

        if (!this.worldObj.isRemote)
        {
            if (par1Entity != this.riddenByEntity)
            {
                if (par1Entity instanceof EntityLiving && !(par1Entity instanceof EntityPlayer) && !(par1Entity instanceof EntityIronGolem) && canBeRidden() && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D && this.riddenByEntity == null && par1Entity.ridingEntity == null)
                {
                    par1Entity.mountEntity(this);
                }

                double var2 = par1Entity.posX - this.posX;
                double var4 = par1Entity.posZ - this.posZ;
                double var6 = var2 * var2 + var4 * var4;

                if (var6 >= 9.999999747378752E-5D)
                {
                    var6 = (double)MathHelper.sqrt_double(var6);
                    var2 /= var6;
                    var4 /= var6;
                    double var8 = 1.0D / var6;

                    if (var8 > 1.0D)
                    {
                        var8 = 1.0D;
                    }

                    var2 *= var8;
                    var4 *= var8;
                    var2 *= 0.10000000149011612D;
                    var4 *= 0.10000000149011612D;
                    var2 *= (double)(1.0F - this.entityCollisionReduction);
                    var4 *= (double)(1.0F - this.entityCollisionReduction);
                    var2 *= 0.5D;
                    var4 *= 0.5D;

                    if (par1Entity instanceof EntityMinecart)
                    {
                        double var10 = par1Entity.posX - this.posX;
                        double var12 = par1Entity.posZ - this.posZ;
                        Vec3 var14 = this.worldObj.getWorldVec3Pool().getVecFromPool(var10, 0.0D, var12).normalize();
                        Vec3 var15 = this.worldObj.getWorldVec3Pool().getVecFromPool((double)MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F), 0.0D, (double)MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F)).normalize();
                        double var16 = Math.abs(var14.dotProduct(var15));

                        if (var16 < 0.800000011920929D)
                        {
                            return;
                        }

                        double var18 = par1Entity.motionX + this.motionX;
                        double var20 = par1Entity.motionZ + this.motionZ;

                        if (((EntityMinecart)par1Entity).isPoweredCart() && !isPoweredCart())
                        {
                            this.motionX *= 0.20000000298023224D;
                            this.motionZ *= 0.20000000298023224D;
                            this.addVelocity(par1Entity.motionX - var2, 0.0D, par1Entity.motionZ - var4);
                            par1Entity.motionX *= 0.949999988079071D;
                            par1Entity.motionZ *= 0.949999988079071D;
                        }
                        else if (!((EntityMinecart)par1Entity).isPoweredCart() && isPoweredCart())
                        {
                            par1Entity.motionX *= 0.20000000298023224D;
                            par1Entity.motionZ *= 0.20000000298023224D;
                            par1Entity.addVelocity(this.motionX + var2, 0.0D, this.motionZ + var4);
                            this.motionX *= 0.949999988079071D;
                            this.motionZ *= 0.949999988079071D;
                        }
                        else
                        {
                            var18 /= 2.0D;
                            var20 /= 2.0D;
                            this.motionX *= 0.20000000298023224D;
                            this.motionZ *= 0.20000000298023224D;
                            this.addVelocity(var18 - var2, 0.0D, var20 - var4);
                            par1Entity.motionX *= 0.20000000298023224D;
                            par1Entity.motionZ *= 0.20000000298023224D;
                            par1Entity.addVelocity(var18 + var2, 0.0D, var20 + var4);
                        }
                    }
                    else
                    {
                        this.addVelocity(-var2, 0.0D, -var4);
                        par1Entity.addVelocity(var2 / 4.0D, 0.0D, var4 / 4.0D);
                    }
                }
            }
        }
    }

    public int getSizeInventory()
    {
        return (minecartType == 1 && getClass() == EntityMinecart.class ? 27 : 0);
    }

    public ItemStack getStackInSlot(int par1)
    {
        return this.cargoItems[par1];
    }

    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.cargoItems[par1] != null)
        {
            ItemStack var3;

            if (this.cargoItems[par1].stackSize <= par2)
            {
                var3 = this.cargoItems[par1];
                this.cargoItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.cargoItems[par1].splitStack(par2);

                if (this.cargoItems[par1].stackSize == 0)
                {
                    this.cargoItems[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.cargoItems[par1] != null)
        {
            ItemStack var2 = this.cargoItems[par1];
            this.cargoItems[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.cargoItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    public String getInvName()
    {
        return "container.minecart";
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void onInventoryChanged() {}

    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, par1EntityPlayer)))
        {
            return true;
        }

        if (canBeRidden())
        {
            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != par1EntityPlayer)
            {
                return true;
            }

            if (!this.worldObj.isRemote)
            {
                par1EntityPlayer.mountEntity(this);
            }
        }
        else if (getSizeInventory() > 0)
        {
            if (!this.worldObj.isRemote)
            {
                par1EntityPlayer.displayGUIChest(this);
            }
        }
        else if (this.minecartType == 2 && getClass() == EntityMinecart.class)
        {
            ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();

            if (var2 != null && var2.itemID == Item.coal.shiftedIndex)
            {
                if (--var2.stackSize == 0)
                {
                    par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
                }

                this.fuel += 3600;
            }

            this.pushX = this.posX - par1EntityPlayer.posX;
            this.pushZ = this.posZ - par1EntityPlayer.posZ;
        }

        return true;
    }

    @SideOnly(Side.CLIENT)

    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        this.minecartX = par1;
        this.minecartY = par3;
        this.minecartZ = par5;
        this.minecartYaw = (double)par7;
        this.minecartPitch = (double)par8;
        this.turnProgress = par9 + 2;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    @SideOnly(Side.CLIENT)

    public void setVelocity(double par1, double par3, double par5)
    {
        this.velocityX = this.motionX = par1;
        this.velocityY = this.motionY = par3;
        this.velocityZ = this.motionZ = par5;
    }

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.isDead ? false : par1EntityPlayer.getDistanceSqToEntity(this) <= 64.0D;
    }

    public boolean isMinecartPowered()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    protected void setMinecartPowered(boolean par1)
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

    public void openChest() {}

    public void closeChest() {}

    public void setDamage(int par1)
    {
        this.dataWatcher.updateObject(19, Integer.valueOf(par1));
    }

    public int getDamage()
    {
        return this.dataWatcher.getWatchableObjectInt(19);
    }

    public void func_70497_h(int par1)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(par1));
    }

    public int func_70496_j()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void func_70494_i(int par1)
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(par1));
    }

    public int func_70493_k()
    {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    public void dropCartAsItem()
    {
        for (ItemStack item : getItemsDropped())
        {
            entityDropItem(item, 0);
        }
    }

    public List<ItemStack> getItemsDropped()
    {
        List<ItemStack> items = new ArrayList<ItemStack>();
        items.add(new ItemStack(Item.minecartEmpty));

        switch (minecartType)
        {
            case 1:
                items.add(new ItemStack(Block.chest));
                break;

            case 2:
                items.add(new ItemStack(Block.stoneOvenIdle));
                break;
        }

        return items;
    }

    public ItemStack getCartItem()
    {
        return MinecartRegistry.getItemForCart(this);
    }

    public boolean isPoweredCart()
    {
        return minecartType == 2 && getClass() == EntityMinecart.class;
    }

    public boolean isStorageCart()
    {
        return minecartType == 1 && getClass() == EntityMinecart.class;
    }

    public boolean canBeRidden()
    {
        if (minecartType == 0 && getClass() == EntityMinecart.class)
        {
            return true;
        }

        return false;
    }

    public boolean canUseRail()
    {
        return canUseRail;
    }

    public void setCanUseRail(boolean use)
    {
        canUseRail = use;
    }

    public boolean shouldDoRailFunctions()
    {
        return true;
    }

    public int getMinecartType()
    {
        return minecartType;
    }

    public static IMinecartCollisionHandler getCollisionHandler()
    {
        return collisionHandler;
    }

    public static void setCollisionHandler(IMinecartCollisionHandler handler)
    {
        collisionHandler = handler;
    }

    protected double getDrag()
    {
        return riddenByEntity != null ? defaultDragRidden : defaultDragEmpty;
    }

    protected void applyDragAndPushForces()
    {
        if (isPoweredCart())
        {
            double d27 = MathHelper.sqrt_double(pushX * pushX + pushZ * pushZ);

            if (d27 > 0.01D)
            {
                pushX /= d27;
                pushZ /= d27;
                double d29 = 0.04;
                motionX *= 0.8D;
                motionY *= 0.0D;
                motionZ *= 0.8D;
                motionX += pushX * d29;
                motionZ += pushZ * d29;
            }
            else
            {
                motionX *= 0.9D;
                motionY *= 0.0D;
                motionZ *= 0.9D;
            }
        }

        motionX *= getDrag();
        motionY *= 0.0D;
        motionZ *= getDrag();
    }

    protected void updatePushForces()
    {
        if (isPoweredCart())
        {
            double push = MathHelper.sqrt_double(pushX * pushX + pushZ * pushZ);

            if (push > 0.01D && motionX * motionX + motionZ * motionZ > 0.001D)
            {
                pushX /= push;
                pushZ /= push;

                if (pushX * motionX + pushZ * motionZ < 0.0D)
                {
                    pushX = 0.0D;
                    pushZ = 0.0D;
                }
                else
                {
                    pushX = motionX;
                    pushZ = motionZ;
                }
            }
        }
    }

    protected void moveMinecartOnRail(int i, int j, int k)
    {
        int id = worldObj.getBlockId(i, j, k);

        if (!BlockRail.isRailBlock(id))
        {
            return;
        }

        float railMaxSpeed = ((BlockRail)Block.blocksList[id]).getRailMaxSpeed(worldObj, this, i, j, k);
        double maxSpeed = Math.min(railMaxSpeed, getMaxSpeedRail());
        double mX = motionX;
        double mZ = motionZ;

        if (riddenByEntity != null)
        {
            mX *= 0.75D;
            mZ *= 0.75D;
        }

        if (mX < -maxSpeed)
        {
            mX = -maxSpeed;
        }

        if (mX >  maxSpeed)
        {
            mX =  maxSpeed;
        }

        if (mZ < -maxSpeed)
        {
            mZ = -maxSpeed;
        }

        if (mZ >  maxSpeed)
        {
            mZ =  maxSpeed;
        }

        moveEntity(mX, 0.0D, mZ);
    }

    protected void moveMinecartOffRail(int i, int j, int k)
    {
        double d2 = getMaxSpeedGround();

        if (!onGround)
        {
            d2 = getMaxSpeedAirLateral();
        }

        if (motionX < -d2)
        {
            motionX = -d2;
        }

        if (motionX >  d2)
        {
            motionX =  d2;
        }

        if (motionZ < -d2)
        {
            motionZ = -d2;
        }

        if (motionZ >  d2)
        {
            motionZ =  d2;
        }

        double moveY = motionY;

        if (getMaxSpeedAirVertical() > 0 && motionY > getMaxSpeedAirVertical())
        {
            moveY = getMaxSpeedAirVertical();

            if (Math.abs(motionX) < 0.3f && Math.abs(motionZ) < 0.3f)
            {
                moveY = 0.15f;
                motionY = moveY;
            }
        }

        if (onGround)
        {
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }

        moveEntity(motionX, moveY, motionZ);

        if (!onGround)
        {
            motionX *= getDragAir();
            motionY *= getDragAir();
            motionZ *= getDragAir();
        }
    }

    protected void updateFuel()
    {
        if (fuel > 0)
        {
            fuel--;
        }

        if (fuel <= 0)
        {
            pushX = pushZ = 0.0D;
        }

        setMinecartPowered(fuel > 0);
    }

    protected void adjustSlopeVelocities(int metadata)
    {
        double acceleration = 0.0078125D;

        if (metadata == 2)
        {
            motionX -= acceleration;
        }
        else if (metadata == 3)
        {
            motionX += acceleration;
        }
        else if (metadata == 4)
        {
            motionZ += acceleration;
        }
        else if (metadata == 5)
        {
            motionZ -= acceleration;
        }
    }

    public float getMaxSpeedRail()
    {
        return maxSpeedRail;
    }

    public void setMaxSpeedRail(float value)
    {
        maxSpeedRail = value;
    }

    public float getMaxSpeedGround()
    {
        return maxSpeedGround;
    }

    public void setMaxSpeedGround(float value)
    {
        maxSpeedGround = value;
    }

    public float getMaxSpeedAirLateral()
    {
        return maxSpeedAirLateral;
    }

    public void setMaxSpeedAirLateral(float value)
    {
        maxSpeedAirLateral = value;
    }

    public float getMaxSpeedAirVertical()
    {
        return maxSpeedAirVertical;
    }

    public void setMaxSpeedAirVertical(float value)
    {
        maxSpeedAirVertical = value;
    }

    public double getDragAir()
    {
        return dragAir;
    }

    public void setDragAir(double value)
    {
        dragAir = value;
    }
}
