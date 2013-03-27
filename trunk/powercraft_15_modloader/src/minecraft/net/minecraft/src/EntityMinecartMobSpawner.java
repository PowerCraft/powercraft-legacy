package net.minecraft.src;

public class EntityMinecartMobSpawner extends EntityMinecart
{
    private final MobSpawnerBaseLogic field_98040_a = new EntityMinecartMobSpawnerLogic(this);

    public EntityMinecartMobSpawner(World par1World)
    {
        super(par1World);
    }

    public EntityMinecartMobSpawner(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    public int func_94087_l()
    {
        return 4;
    }

    public Block func_94093_n()
    {
        return Block.mobSpawner;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.field_98040_a.func_98270_a(par1NBTTagCompound);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        this.field_98040_a.func_98280_b(par1NBTTagCompound);
    }

    public void handleHealthUpdate(byte par1)
    {
        this.field_98040_a.func_98268_b(par1);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        this.field_98040_a.updateSpawner();
    }

    public MobSpawnerBaseLogic func_98039_d()
    {
        return this.field_98040_a;
    }
}
