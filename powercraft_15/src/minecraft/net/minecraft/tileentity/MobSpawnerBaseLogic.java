package net.minecraft.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public abstract class MobSpawnerBaseLogic
{
    public int field_98286_b = 20;
    private String field_98288_a = "Pig";
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
            if (this.field_98288_a.equals("Minecart"))
            {
                this.field_98288_a = "MinecartRideable";
            }

            return this.field_98288_a;
        }
        else
        {
            return this.func_98269_i().field_98223_c;
        }
    }

    public void func_98272_a(String par1Str)
    {
        this.field_98288_a = par1Str;
    }

    public boolean func_98279_f()
    {
        return this.func_98271_a().getClosestPlayer((double)this.func_98275_b() + 0.5D, (double)this.func_98274_c() + 0.5D, (double)this.func_98266_d() + 0.5D, (double)this.field_98289_l) != null;
    }

    public void func_98278_g()
    {
        if (this.func_98279_f())
        {
            double d0;

            if (this.func_98271_a().isRemote)
            {
                double d1 = (double)((float)this.func_98275_b() + this.func_98271_a().rand.nextFloat());
                double d2 = (double)((float)this.func_98274_c() + this.func_98271_a().rand.nextFloat());
                d0 = (double)((float)this.func_98266_d() + this.func_98271_a().rand.nextFloat());
                this.func_98271_a().spawnParticle("smoke", d1, d2, d0, 0.0D, 0.0D, 0.0D);
                this.func_98271_a().spawnParticle("flame", d1, d2, d0, 0.0D, 0.0D, 0.0D);

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

                boolean flag = false;

                for (int i = 0; i < this.field_98294_i; ++i)
                {
                    Entity entity = EntityList.createEntityByName(this.func_98276_e(), this.func_98271_a());

                    if (entity == null)
                    {
                        return;
                    }

                    int j = this.func_98271_a().getEntitiesWithinAABB(entity.getClass(), AxisAlignedBB.getAABBPool().getAABB((double)this.func_98275_b(), (double)this.func_98274_c(), (double)this.func_98266_d(), (double)(this.func_98275_b() + 1), (double)(this.func_98274_c() + 1), (double)(this.func_98266_d() + 1)).expand((double)(this.field_98290_m * 2), 4.0D, (double)(this.field_98290_m * 2))).size();

                    if (j >= this.field_98292_k)
                    {
                        this.func_98273_j();
                        return;
                    }

                    d0 = (double)this.func_98275_b() + (this.func_98271_a().rand.nextDouble() - this.func_98271_a().rand.nextDouble()) * (double)this.field_98290_m;
                    double d3 = (double)(this.func_98274_c() + this.func_98271_a().rand.nextInt(3) - 1);
                    double d4 = (double)this.func_98266_d() + (this.func_98271_a().rand.nextDouble() - this.func_98271_a().rand.nextDouble()) * (double)this.field_98290_m;
                    EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
                    entity.setLocationAndAngles(d0, d3, d4, this.func_98271_a().rand.nextFloat() * 360.0F, 0.0F);

                    if (entityliving == null || entityliving.getCanSpawnHere())
                    {
                        this.func_98265_a(entity);
                        this.func_98271_a().playAuxSFX(2004, this.func_98275_b(), this.func_98274_c(), this.func_98266_d(), 0);

                        if (entityliving != null)
                        {
                            entityliving.spawnExplosionParticle();
                        }

                        flag = true;
                    }
                }

                if (flag)
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
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            par1Entity.addEntityID(nbttagcompound);
            Iterator iterator = this.func_98269_i().field_98222_b.getTags().iterator();

            while (iterator.hasNext())
            {
                NBTBase nbtbase = (NBTBase)iterator.next();
                nbttagcompound.setTag(nbtbase.getName(), nbtbase.copy());
            }

            par1Entity.readFromNBT(nbttagcompound);

            if (par1Entity.worldObj != null)
            {
                par1Entity.worldObj.spawnEntityInWorld(par1Entity);
            }

            NBTTagCompound nbttagcompound1;

            for (Entity entity1 = par1Entity; nbttagcompound.hasKey("Riding"); nbttagcompound = nbttagcompound1)
            {
                nbttagcompound1 = nbttagcompound.getCompoundTag("Riding");
                Entity entity2 = EntityList.createEntityByName(nbttagcompound1.getString("id"), this.func_98271_a());

                if (entity2 != null)
                {
                    NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                    entity2.addEntityID(nbttagcompound2);
                    Iterator iterator1 = nbttagcompound1.getTags().iterator();

                    while (iterator1.hasNext())
                    {
                        NBTBase nbtbase1 = (NBTBase)iterator1.next();
                        nbttagcompound2.setTag(nbtbase1.getName(), nbtbase1.copy());
                    }

                    entity2.readFromNBT(nbttagcompound2);
                    entity2.setLocationAndAngles(entity1.posX, entity1.posY, entity1.posZ, entity1.rotationYaw, entity1.rotationPitch);
                    this.func_98271_a().spawnEntityInWorld(entity2);
                    entity1.mountEntity(entity2);
                }

                entity1 = entity2;
            }
        }
        else if (par1Entity instanceof EntityLiving && par1Entity.worldObj != null)
        {
            ((EntityLiving)par1Entity).initCreature();
            this.func_98271_a().spawnEntityInWorld(par1Entity);
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
            int i = this.field_98293_h - this.field_98283_g;
            this.field_98286_b = this.field_98283_g + this.func_98271_a().rand.nextInt(i);
        }

        if (this.field_98285_e != null && this.field_98285_e.size() > 0)
        {
            this.func_98277_a((WeightedRandomMinecart)WeightedRandom.getRandomItem(this.func_98271_a().rand, this.field_98285_e));
        }

        this.func_98267_a(1);
    }

    public void func_98270_a(NBTTagCompound par1NBTTagCompound)
    {
        this.field_98288_a = par1NBTTagCompound.getString("EntityId");
        this.field_98286_b = par1NBTTagCompound.getShort("Delay");

        if (par1NBTTagCompound.hasKey("SpawnPotentials"))
        {
            this.field_98285_e = new ArrayList();
            NBTTagList nbttaglist = par1NBTTagCompound.getTagList("SpawnPotentials");

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                this.field_98285_e.add(new WeightedRandomMinecart(this, (NBTTagCompound)nbttaglist.tagAt(i)));
            }
        }
        else
        {
            this.field_98285_e = null;
        }

        if (par1NBTTagCompound.hasKey("SpawnData"))
        {
            this.func_98277_a(new WeightedRandomMinecart(this, par1NBTTagCompound.getCompoundTag("SpawnData"), this.field_98288_a));
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

        if (this.func_98271_a() != null && this.func_98271_a().isRemote)
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
            NBTTagList nbttaglist = new NBTTagList();

            if (this.field_98285_e != null && this.field_98285_e.size() > 0)
            {
                Iterator iterator = this.field_98285_e.iterator();

                while (iterator.hasNext())
                {
                    WeightedRandomMinecart weightedrandomminecart = (WeightedRandomMinecart)iterator.next();
                    nbttaglist.appendTag(weightedrandomminecart.func_98220_a());
                }
            }
            else
            {
                nbttaglist.appendTag(this.func_98269_i().func_98220_a());
            }

            par1NBTTagCompound.setTag("SpawnPotentials", nbttaglist);
        }
    }

    public boolean func_98268_b(int par1)
    {
        if (par1 == 1 && this.func_98271_a().isRemote)
        {
            this.field_98286_b = this.field_98283_g;
            return true;
        }
        else
        {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    public Entity func_98281_h()
    {
        if (this.field_98291_j == null)
        {
            Entity entity = EntityList.createEntityByName(this.func_98276_e(), (World)null);
            entity = this.func_98265_a(entity);
            this.field_98291_j = entity;
        }

        return this.field_98291_j;
    }

    public WeightedRandomMinecart func_98269_i()
    {
        return this.field_98282_f;
    }

    public void func_98277_a(WeightedRandomMinecart par1WeightedRandomMinecart)
    {
        this.field_98282_f = par1WeightedRandomMinecart;
    }

    public abstract void func_98267_a(int i);

    public abstract World func_98271_a();

    public abstract int func_98275_b();

    public abstract int func_98274_c();

    public abstract int func_98266_d();
}
