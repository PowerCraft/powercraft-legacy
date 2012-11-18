package net.minecraft.src;

public class EntityAIOcelotSit extends EntityAIBase
{
    private final EntityOcelot theOcelot;
    private final float field_75404_b;

    private int currentTick = 0;
    private int field_75402_d = 0;

    private int maxSittingTicks = 0;

    private int sitableBlockX = 0;

    private int sitableBlockY = 0;

    private int sitableBlockZ = 0;

    public EntityAIOcelotSit(EntityOcelot par1EntityOcelot, float par2)
    {
        this.theOcelot = par1EntityOcelot;
        this.field_75404_b = par2;
        this.setMutexBits(5);
    }

    public boolean shouldExecute()
    {
        return this.theOcelot.isTamed() && !this.theOcelot.isSitting() && this.theOcelot.getRNG().nextDouble() <= 0.006500000134110451D && this.getNearbySitableBlockDistance();
    }

    public boolean continueExecuting()
    {
        return this.currentTick <= this.maxSittingTicks && this.field_75402_d <= 60 && this.isSittableBlock(this.theOcelot.worldObj, this.sitableBlockX, this.sitableBlockY, this.sitableBlockZ);
    }

    public void startExecuting()
    {
        this.theOcelot.getNavigator().tryMoveToXYZ((double)((float)this.sitableBlockX) + 0.5D, (double)(this.sitableBlockY + 1), (double)((float)this.sitableBlockZ) + 0.5D, this.field_75404_b);
        this.currentTick = 0;
        this.field_75402_d = 0;
        this.maxSittingTicks = this.theOcelot.getRNG().nextInt(this.theOcelot.getRNG().nextInt(1200) + 1200) + 1200;
        this.theOcelot.func_70907_r().setSitting(false);
    }

    public void resetTask()
    {
        this.theOcelot.setSitting(false);
    }

    public void updateTask()
    {
        ++this.currentTick;
        this.theOcelot.func_70907_r().setSitting(false);

        if (this.theOcelot.getDistanceSq((double)this.sitableBlockX, (double)(this.sitableBlockY + 1), (double)this.sitableBlockZ) > 1.0D)
        {
            this.theOcelot.setSitting(false);
            this.theOcelot.getNavigator().tryMoveToXYZ((double)((float)this.sitableBlockX) + 0.5D, (double)(this.sitableBlockY + 1), (double)((float)this.sitableBlockZ) + 0.5D, this.field_75404_b);
            ++this.field_75402_d;
        }
        else if (!this.theOcelot.isSitting())
        {
            this.theOcelot.setSitting(true);
        }
        else
        {
            --this.field_75402_d;
        }
    }

    protected boolean getNearbySitableBlockDistance()
    {
        int var1 = (int)this.theOcelot.posY;
        double var2 = 2.147483647E9D;

        for (int var4 = (int)this.theOcelot.posX - 8; (double)var4 < this.theOcelot.posX + 8.0D; ++var4)
        {
            for (int var5 = (int)this.theOcelot.posZ - 8; (double)var5 < this.theOcelot.posZ + 8.0D; ++var5)
            {
                if (this.isSittableBlock(this.theOcelot.worldObj, var4, var1, var5) && this.theOcelot.worldObj.isAirBlock(var4, var1 + 1, var5))
                {
                    double var6 = this.theOcelot.getDistanceSq((double)var4, (double)var1, (double)var5);

                    if (var6 < var2)
                    {
                        this.sitableBlockX = var4;
                        this.sitableBlockY = var1;
                        this.sitableBlockZ = var5;
                        var2 = var6;
                    }
                }
            }
        }

        return var2 < 2.147483647E9D;
    }

    protected boolean isSittableBlock(World par1World, int par2, int par3, int par4)
    {
        int var5 = par1World.getBlockId(par2, par3, par4);
        int var6 = par1World.getBlockMetadata(par2, par3, par4);

        if (var5 == Block.chest.blockID)
        {
            TileEntityChest var7 = (TileEntityChest)par1World.getBlockTileEntity(par2, par3, par4);

            if (var7.numUsingPlayers < 1)
            {
                return true;
            }
        }
        else
        {
            if (var5 == Block.stoneOvenActive.blockID)
            {
                return true;
            }

            if (var5 == Block.bed.blockID && !BlockBed.isBlockHeadOfBed(var6))
            {
                return true;
            }
        }

        return false;
    }
}
