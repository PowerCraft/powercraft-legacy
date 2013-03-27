package net.minecraft.src;

class TileEntityMobSpawnerLogic extends MobSpawnerBaseLogic
{
    final TileEntityMobSpawner field_98295_a;

    TileEntityMobSpawnerLogic(TileEntityMobSpawner par1TileEntityMobSpawner)
    {
        this.field_98295_a = par1TileEntityMobSpawner;
    }

    public void func_98267_a(int par1)
    {
        this.field_98295_a.worldObj.addBlockEvent(this.field_98295_a.xCoord, this.field_98295_a.yCoord, this.field_98295_a.zCoord, Block.mobSpawner.blockID, par1, 0);
    }

    public World getSpawnerWorld()
    {
        return this.field_98295_a.worldObj;
    }

    public int getSpawnerX()
    {
        return this.field_98295_a.xCoord;
    }

    public int getSpawnerY()
    {
        return this.field_98295_a.yCoord;
    }

    public int getSpawnerZ()
    {
        return this.field_98295_a.zCoord;
    }

    public void func_98277_a(WeightedRandomMinecart par1WeightedRandomMinecart)
    {
        super.func_98277_a(par1WeightedRandomMinecart);

        if (this.getSpawnerWorld() != null)
        {
            this.getSpawnerWorld().markBlockForUpdate(this.field_98295_a.xCoord, this.field_98295_a.yCoord, this.field_98295_a.zCoord);
        }
    }
}
