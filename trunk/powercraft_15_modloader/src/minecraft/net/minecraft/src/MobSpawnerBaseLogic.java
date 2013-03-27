package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class MobSpawnerBaseLogic
{
    public int field_98286_b = 20;
    private String mobID = "Pig";
    private List field_98285_e = null;
    private WeightedRandomMinecart field_98282_f = null;
    public double field_98287_c;
    public double field_98284_d = 0.0D;
    private int field_98283_g = 200;
    private int field_98293_h = 800;
    private int field_98294_i = 4;
    private Entity field_98291_j;
    private int field_98292_k = 6;
    private int field_98289_l = 16;
    private int field_98290_m = 4;

    public String func_98276_e()
    {
        if (this.func_98269_i() == null)
        {
            if (this.mobID.equals("Minecart"))
            {
                this.mobID = "MinecartRideable";
            }

            return this.mobID;
        }
        else
        {
            return this.func_98269_i().field_98223_c;
        }
    }

    public void setMobID(String par1Str)
    {
        this.mobID = par1Str;
    }

    /**
     * Returns true if there's a player close enough to this mob spawner to activate it.
     */
    public boolean canRun()
    {
        return this.getSpawnerWorld().getClosestPlayer((double)this.getSpawnerX() + 0.5D, (double)this.getSpawnerY() + 0.5D, (double)this.getSpawnerZ() + 0.5D, (double)this.field_98289_l) != null;
    }

    public void updateSpawner()
    {
        if (this.canRun())
        {
            double var5;

            if (this.getSpawnerWorld().isRemote)
            {
                double var1 = (double)((float)this.getSpawnerX() + this.getSpawnerWorld().rand.nextFloat());
                double var3 = (double)((float)this.getSpawnerY() + this.getSpawnerWorld().rand.nextFloat());
                var5 = (double)((float)this.getSpawnerZ() + this.getSpawnerWorld().rand.nextFloat());
                this.getSpawnerWorld().spawnParticle("smoke", var1, var3, var5, 0.0D, 0.0D, 0.0D);
                this.getSpawnerWorld().spawnParticle("flame", var1, var3, var5, 0.0D, 0.0D, 0.0D);

                if (this.field_98286_b > 0)
                {
                    --this.field_98286_b;
                }

                this.field_98284_d = this.field_98287_c;
                this.field_98287_c = (this.field_98287_c + (double)(1000.0F / ((float)this.field_98286_b + 200.0F))) % 360.0D;
            }
            else
            {
                if (this.field_98286_b == -1)
                {
                    this.func_98273_j();
                }

                if (this.field_98286_b > 0)
                {
                    --this.field_98286_b;
                    return;
                }

                boolean var12 = false;

                for (int var2 = 0; var2 < this.field_98294_i; ++var2)
                {
                    Entity var13 = EntityList.createEntityByName(this.func_98276_e(), this.getSpawnerWorld());

                    if (var13 == null)
                    {
                        return;
                    }

                    int var4 = this.getSpawnerWorld().getEntitiesWithinAABB(var13.getClass(), AxisAlignedBB.getAABBPool().getAABB((double)this.getSpawnerX(), (double)this.getSpawnerY(), (double)this.getSpawnerZ(), (double)(this.getSpawnerX() + 1), (double)(this.getSpawnerY() + 1), (double)(this.getSpawnerZ() + 1)).expand((double)(this.field_98290_m * 2), 4.0D, (double)(this.field_98290_m * 2))).size();

                    if (var4 >= this.field_98292_k)
                    {
                        this.func_98273_j();
                        return;
                    }

                    var5 = (double)this.getSpawnerX() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.field_98290_m;
                    double var7 = (double)(this.getSpawnerY() + this.getSpawnerWorld().rand.nextInt(3) - 1);
                    double var9 = (double)this.getSpawnerZ() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.field_98290_m;
                    EntityLiving var11 = var13 instanceof EntityLiving ? (EntityLiving)var13 : null;
                    var13.setLocationAndAngles(var5, var7, var9, this.getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);

                    if (var11 == null || var11.getCanSpawnHere())
                    {
                        this.func_98265_a(var13);
                        this.getSpawnerWorld().playAuxSFX(2004, this.getSpawnerX(), this.getSpawnerY(), this.getSpawnerZ(), 0);

                        if (var11 != null)
                        {
                            var11.spawnExplosionParticle();
                        }

                        var12 = true;
                    }
                }

                if (var12)
                {
                    this.func_98273_j();
                }
            }
        }
    }

    public Entity func_98265_a(Entity par1Entity)
    {
        if (this.func_98269_i() != null)
        {
            NBTTagCompound var2 = new NBTTagCompound();
            par1Entity.addEntityID(var2);
            Iterator var3 = this.func_98269_i().field_98222_b.getTags().iterator();

            while (var3.hasNext())
            {
                NBTBase var4 = (NBTBase)var3.next();
                var2.setTag(var4.getName(), var4.copy());
            }

            par1Entity.readFromNBT(var2);

            if (par1Entity.worldObj != null)
            {
                par1Entity.worldObj.spawnEntityInWorld(par1Entity);
            }

            NBTTagCompound var10;

            for (Entity var9 = par1Entity; var2.hasKey("Riding"); var2 = var10)
            {
                var10 = var2.getCompoundTag("Riding");
                Entity var5 = EntityList.createEntityByName(var10.getString("id"), this.getSpawnerWorld());

                if (var5 != null)
                {
                    NBTTagCompound var6 = new NBTTagCompound();
                    var5.addEntityID(var6);
                    Iterator var7 = var10.getTags().iterator();

                    while (var7.hasNext())
                    {
                        NBTBase var8 = (NBTBase)var7.next();
                        var6.setTag(var8.getName(), var8.copy());
                    }

                    var5.readFromNBT(var6);
                    var5.setLocationAndAngles(var9.posX, var9.posY, var9.posZ, var9.rotationYaw, var9.rotationPitch);
                    this.getSpawnerWorld().spawnEntityInWorld(var5);
                    var9.mountEntity(var5);
                }

                var9 = var5;
            }
        }
        else if (par1Entity instanceof EntityLiving && par1Entity.worldObj != null)
        {
            ((EntityLiving)par1Entity).initCreature();
            this.getSpawnerWorld().spawnEntityInWorld(par1Entity);
        }

        return par1Entity;
    }

    private void func_98273_j()
    {
        if (this.field_98293_h <= this.field_98283_g)
        {
            this.field_98286_b = this.field_98283_g;
        }
        else
        {
            int var10003 = this.field_98293_h - this.field_98283_g;
            this.field_98286_b = this.field_98283_g + this.getSpawnerWorld().rand.nextInt(var10003);
        }

        if (this.field_98285_e != null && this.field_98285_e.size() > 0)
        {
            this.func_98277_a((WeightedRandomMinecart)WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.field_98285_e));
        }

        this.func_98267_a(1);
    }

    public void func_98270_a(NBTTagCompound par1NBTTagCompound)
    {
        this.mobID = par1NBTTagCompound.getString("EntityId");
        this.field_98286_b = par1NBTTagCompound.getShort("Delay");

        if (par1NBTTagCompound.hasKey("SpawnPotentials"))
        {
            this.field_98285_e = new ArrayList();
            NBTTagList var2 = par1NBTTagCompound.getTagList("SpawnPotentials");

            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                this.field_98285_e.add(new WeightedRandomMinecart(this, (NBTTagCompound)var2.tagAt(var3)));
            }
        }
        else
        {
            this.field_98285_e = null;
        }

        if (par1NBTTagCompound.hasKey("SpawnData"))
        {
            this.func_98277_a(new WeightedRandomMinecart(this, par1NBTTagCompound.getCompoundTag("SpawnData"), this.mobID));
        }
        else
        {
            this.func_98277_a((WeightedRandomMinecart)null);
        }

        if (par1NBTTagCompound.hasKey("MinSpawnDelay"))
        {
            this.field_98283_g = par1NBTTagCompound.getShort("MinSpawnDelay");
            this.field_98293_h = par1NBTTagCompound.getShort("MaxSpawnDelay");
            this.field_98294_i = par1NBTTagCompound.getShort("SpawnCount");
        }

        if (par1NBTTagCompound.hasKey("MaxNearbyEntities"))
        {
            this.field_98292_k = par1NBTTagCompound.getShort("MaxNearbyEntities");
            this.field_98289_l = par1NBTTagCompound.getShort("RequiredPlayerRange");
        }

        if (par1NBTTagCompound.hasKey("SpawnRange"))
        {
            this.field_98290_m = par1NBTTagCompound.getShort("SpawnRange");
        }

        if (this.getSpawnerWorld() != null && this.getSpawnerWorld().isRemote)
        {
            this.field_98291_j = null;
        }
    }

    public void func_98280_b(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setString("EntityId", this.func_98276_e());
        par1NBTTagCompound.setShort("Delay", (short)this.field_98286_b);
        par1NBTTagCompound.setShort("MinSpawnDelay", (short)this.field_98283_g);
        par1NBTTagCompound.setShort("MaxSpawnDelay", (short)this.field_98293_h);
        par1NBTTagCompound.setShort("SpawnCount", (short)this.field_98294_i);
        par1NBTTagCompound.setShort("MaxNearbyEntities", (short)this.field_98292_k);
        par1NBTTagCompound.setShort("RequiredPlayerRange", (short)this.field_98289_l);
        par1NBTTagCompound.setShort("SpawnRange", (short)this.field_98290_m);

        if (this.func_98269_i() != null)
        {
            par1NBTTagCompound.setCompoundTag("SpawnData", (NBTTagCompound)this.func_98269_i().field_98222_b.copy());
        }

        if (this.func_98269_i() != null || this.field_98285_e != null && this.field_98285_e.size() > 0)
        {
            NBTTagList var2 = new NBTTagList();

            if (this.field_98285_e != null && this.field_98285_e.size() > 0)
            {
                Iterator var3 = this.field_98285_e.iterator();

                while (var3.hasNext())
                {
                    WeightedRandomMinecart var4 = (WeightedRandomMinecart)var3.next();
                    var2.appendTag(var4.func_98220_a());
                }
            }
            else
            {
                var2.appendTag(this.func_98269_i().func_98220_a());
            }

            par1NBTTagCompound.setTag("SpawnPotentials", var2);
        }
    }

    public Entity func_98281_h()
    {
        if (this.field_98291_j == null)
        {
            Entity var1 = EntityList.createEntityByName(this.func_98276_e(), (World)null);
            var1 = this.func_98265_a(var1);
            this.field_98291_j = var1;
        }

        return this.field_98291_j;
    }

    public boolean func_98268_b(int par1)
    {
        if (par1 == 1 && this.getSpawnerWorld().isRemote)
        {
            this.field_98286_b = this.field_98283_g;
            return true;
        }
        else
        {
            return false;
        }
    }

    public WeightedRandomMinecart func_98269_i()
    {
        return this.field_98282_f;
    }

    public void func_98277_a(WeightedRandomMinecart par1WeightedRandomMinecart)
    {
        this.field_98282_f = par1WeightedRandomMinecart;
    }

    public abstract void func_98267_a(int var1);

    public abstract World getSpawnerWorld();

    public abstract int getSpawnerX();

    public abstract int getSpawnerY();

    public abstract int getSpawnerZ();
}
