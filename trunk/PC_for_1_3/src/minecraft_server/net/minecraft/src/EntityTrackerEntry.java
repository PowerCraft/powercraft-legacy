package net.minecraft.src;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EntityTrackerEntry
{
    /** The entity that this EntityTrackerEntry tracks. */
    public Entity trackedEntity;
    public int trackingDistanceThreshold;
    public int field_73131_c;

    /** The encoded entity X position. */
    public int encodedPosX;

    /** The encoded entity Y position. */
    public int encodedPosY;

    /** The encoded entity Z position. */
    public int encodedPosZ;

    /** The encoded entity yaw rotation. */
    public int encodedRotationYaw;

    /** The encoded entity pitch rotation. */
    public int encodedRotationPitch;
    public int field_73140_i;
    public double lastTrackedEntityMotionX;
    public double lastTrackedEntityMotionY;
    public double lastTrackedEntityMotionZ;
    public int updateCounter = 0;
    private double lastTrackedEntityPosX;
    private double lastTrackedEntityPosY;
    private double lastTrackedEntityPosZ;
    private boolean firstUpdateDone = false;
    private boolean shouldSendMotionUpdates;
    private int field_73142_u = 0;
    private Entity field_73141_v;
    public boolean playerEntitiesUpdated = false;
    public Set trackedPlayers = new HashSet();

    public EntityTrackerEntry(Entity par1Entity, int par2, int par3, boolean par4)
    {
        this.trackedEntity = par1Entity;
        this.trackingDistanceThreshold = par2;
        this.field_73131_c = par3;
        this.shouldSendMotionUpdates = par4;
        this.encodedPosX = MathHelper.floor_double(par1Entity.posX * 32.0D);
        this.encodedPosY = MathHelper.floor_double(par1Entity.posY * 32.0D);
        this.encodedPosZ = MathHelper.floor_double(par1Entity.posZ * 32.0D);
        this.encodedRotationYaw = MathHelper.floor_float(par1Entity.rotationYaw * 256.0F / 360.0F);
        this.encodedRotationPitch = MathHelper.floor_float(par1Entity.rotationPitch * 256.0F / 360.0F);
        this.field_73140_i = MathHelper.floor_float(par1Entity.func_70079_am() * 256.0F / 360.0F);
    }

    public boolean equals(Object par1Obj)
    {
        return par1Obj instanceof EntityTrackerEntry ? ((EntityTrackerEntry)par1Obj).trackedEntity.entityId == this.trackedEntity.entityId : false;
    }

    public int hashCode()
    {
        return this.trackedEntity.entityId;
    }

    public void updatePlayerList(List par1List)
    {
        this.playerEntitiesUpdated = false;

        if (!this.firstUpdateDone || this.trackedEntity.getDistanceSq(this.lastTrackedEntityPosX, this.lastTrackedEntityPosY, this.lastTrackedEntityPosZ) > 16.0D)
        {
            this.lastTrackedEntityPosX = this.trackedEntity.posX;
            this.lastTrackedEntityPosY = this.trackedEntity.posY;
            this.lastTrackedEntityPosZ = this.trackedEntity.posZ;
            this.firstUpdateDone = true;
            this.playerEntitiesUpdated = true;
            this.updatePlayerEntities(par1List);
        }

        if (this.field_73141_v != this.trackedEntity.ridingEntity)
        {
            this.field_73141_v = this.trackedEntity.ridingEntity;
            this.sendPacketToTrackedPlayers(new Packet39AttachEntity(this.trackedEntity, this.trackedEntity.ridingEntity));
        }

        if (this.trackedEntity.ridingEntity == null)
        {
            ++this.field_73142_u;

            if (this.updateCounter++ % this.field_73131_c == 0 || this.trackedEntity.isAirBorne)
            {
                int var2 = this.trackedEntity.field_70168_am.func_75630_a(this.trackedEntity.posX);
                int var3 = MathHelper.floor_double(this.trackedEntity.posY * 32.0D);
                int var4 = this.trackedEntity.field_70168_am.func_75630_a(this.trackedEntity.posZ);
                int var5 = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0F / 360.0F);
                int var6 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0F / 360.0F);
                int var7 = var2 - this.encodedPosX;
                int var8 = var3 - this.encodedPosY;
                int var9 = var4 - this.encodedPosZ;
                Object var10 = null;
                boolean var11 = Math.abs(var7) >= 4 || Math.abs(var8) >= 4 || Math.abs(var9) >= 4;
                boolean var12 = Math.abs(var5 - this.encodedRotationYaw) >= 4 || Math.abs(var6 - this.encodedRotationPitch) >= 4;

                if (var7 >= -128 && var7 < 128 && var8 >= -128 && var8 < 128 && var9 >= -128 && var9 < 128 && this.field_73142_u <= 400)
                {
                    if (var11 && var12)
                    {
                        var10 = new Packet33RelEntityMoveLook(this.trackedEntity.entityId, (byte)var7, (byte)var8, (byte)var9, (byte)var5, (byte)var6);
                    }
                    else if (var11)
                    {
                        var10 = new Packet31RelEntityMove(this.trackedEntity.entityId, (byte)var7, (byte)var8, (byte)var9);
                    }
                    else if (var12)
                    {
                        var10 = new Packet32EntityLook(this.trackedEntity.entityId, (byte)var5, (byte)var6);
                    }
                }
                else
                {
                    this.field_73142_u = 0;
                    var10 = new Packet34EntityTeleport(this.trackedEntity.entityId, var2, var3, var4, (byte)var5, (byte)var6);
                }

                if (this.shouldSendMotionUpdates)
                {
                    double var13 = this.trackedEntity.motionX - this.lastTrackedEntityMotionX;
                    double var15 = this.trackedEntity.motionY - this.lastTrackedEntityMotionY;
                    double var17 = this.trackedEntity.motionZ - this.lastTrackedEntityMotionZ;
                    double var19 = 0.02D;
                    double var21 = var13 * var13 + var15 * var15 + var17 * var17;

                    if (var21 > var19 * var19 || var21 > 0.0D && this.trackedEntity.motionX == 0.0D && this.trackedEntity.motionY == 0.0D && this.trackedEntity.motionZ == 0.0D)
                    {
                        this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
                        this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
                        this.lastTrackedEntityMotionZ = this.trackedEntity.motionZ;
                        this.sendPacketToTrackedPlayers(new Packet28EntityVelocity(this.trackedEntity.entityId, this.lastTrackedEntityMotionX, this.lastTrackedEntityMotionY, this.lastTrackedEntityMotionZ));
                    }
                }

                if (var10 != null)
                {
                    this.sendPacketToTrackedPlayers((Packet)var10);
                }

                DataWatcher var23 = this.trackedEntity.getDataWatcher();

                if (var23.hasObjectChanged())
                {
                    this.sendPacketToTrackedPlayersAndTrackedEntity(new Packet40EntityMetadata(this.trackedEntity.entityId, var23));
                }

                int var14 = MathHelper.floor_float(this.trackedEntity.func_70079_am() * 256.0F / 360.0F);

                if (Math.abs(var14 - this.field_73140_i) >= 4)
                {
                    this.sendPacketToTrackedPlayers(new Packet35EntityHeadRotation(this.trackedEntity.entityId, (byte)var14));
                    this.field_73140_i = var14;
                }

                if (var11)
                {
                    this.encodedPosX = var2;
                    this.encodedPosY = var3;
                    this.encodedPosZ = var4;
                }

                if (var12)
                {
                    this.encodedRotationYaw = var5;
                    this.encodedRotationPitch = var6;
                }
            }

            this.trackedEntity.isAirBorne = false;
        }

        if (this.trackedEntity.velocityChanged)
        {
            this.sendPacketToTrackedPlayersAndTrackedEntity(new Packet28EntityVelocity(this.trackedEntity));
            this.trackedEntity.velocityChanged = false;
        }
    }

    public void sendPacketToTrackedPlayers(Packet par1Packet)
    {
        Iterator var2 = this.trackedPlayers.iterator();

        while (var2.hasNext())
        {
            EntityPlayerMP var3 = (EntityPlayerMP)var2.next();
            var3.playerNetServerHandler.sendPacket(par1Packet);
        }
    }

    public void sendPacketToTrackedPlayersAndTrackedEntity(Packet par1Packet)
    {
        this.sendPacketToTrackedPlayers(par1Packet);

        if (this.trackedEntity instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP)this.trackedEntity).playerNetServerHandler.sendPacket(par1Packet);
        }
    }

    public void sendDestroyEntityPacketToTrackedPlayers()
    {
        Iterator var1 = this.trackedPlayers.iterator();

        while (var1.hasNext())
        {
            EntityPlayerMP var2 = (EntityPlayerMP)var1.next();
            var2.field_71130_g.add(Integer.valueOf(this.trackedEntity.entityId));
        }
    }

    public void removeFromTrackedPlayers(EntityPlayerMP par1EntityPlayerMP)
    {
        if (this.trackedPlayers.contains(par1EntityPlayerMP))
        {
            this.trackedPlayers.remove(par1EntityPlayerMP);
        }
    }

    public void updatePlayerEntity(EntityPlayerMP par1EntityPlayerMP)
    {
        if (par1EntityPlayerMP != this.trackedEntity)
        {
            double var2 = par1EntityPlayerMP.posX - (double)(this.encodedPosX / 32);
            double var4 = par1EntityPlayerMP.posZ - (double)(this.encodedPosZ / 32);

            if (var2 >= (double)(-this.trackingDistanceThreshold) && var2 <= (double)this.trackingDistanceThreshold && var4 >= (double)(-this.trackingDistanceThreshold) && var4 <= (double)this.trackingDistanceThreshold)
            {
                if (!this.trackedPlayers.contains(par1EntityPlayerMP) && this.func_73121_d(par1EntityPlayerMP))
                {
                    this.trackedPlayers.add(par1EntityPlayerMP);
                    Packet var6 = this.getSpawnPacket();
                    par1EntityPlayerMP.playerNetServerHandler.sendPacket(var6);
                    this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
                    this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
                    this.lastTrackedEntityMotionZ = this.trackedEntity.motionZ;

                    if (this.shouldSendMotionUpdates && !(var6 instanceof Packet24MobSpawn))
                    {
                        par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet28EntityVelocity(this.trackedEntity.entityId, this.trackedEntity.motionX, this.trackedEntity.motionY, this.trackedEntity.motionZ));
                    }

                    if (this.trackedEntity.ridingEntity != null)
                    {
                        par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet39AttachEntity(this.trackedEntity, this.trackedEntity.ridingEntity));
                    }

                    ItemStack[] var7 = this.trackedEntity.getInventory();

                    if (var7 != null)
                    {
                        for (int var8 = 0; var8 < var7.length; ++var8)
                        {
                            par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet5PlayerInventory(this.trackedEntity.entityId, var8, var7[var8]));
                        }
                    }

                    if (this.trackedEntity instanceof EntityPlayer)
                    {
                        EntityPlayer var11 = (EntityPlayer)this.trackedEntity;

                        if (var11.isPlayerSleeping())
                        {
                            par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet17Sleep(this.trackedEntity, 0, MathHelper.floor_double(this.trackedEntity.posX), MathHelper.floor_double(this.trackedEntity.posY), MathHelper.floor_double(this.trackedEntity.posZ)));
                        }
                    }

                    if (this.trackedEntity instanceof EntityLiving)
                    {
                        EntityLiving var12 = (EntityLiving)this.trackedEntity;
                        Iterator var9 = var12.getActivePotionEffects().iterator();

                        while (var9.hasNext())
                        {
                            PotionEffect var10 = (PotionEffect)var9.next();
                            par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet41EntityEffect(this.trackedEntity.entityId, var10));
                        }
                    }
                }
            }
            else if (this.trackedPlayers.contains(par1EntityPlayerMP))
            {
                this.trackedPlayers.remove(par1EntityPlayerMP);
                par1EntityPlayerMP.field_71130_g.add(Integer.valueOf(this.trackedEntity.entityId));
            }
        }
    }

    private boolean func_73121_d(EntityPlayerMP par1EntityPlayerMP)
    {
        return par1EntityPlayerMP.func_71121_q().func_73040_p().func_72694_a(par1EntityPlayerMP, this.trackedEntity.chunkCoordX, this.trackedEntity.chunkCoordZ);
    }

    public void updatePlayerEntities(List par1List)
    {
        Iterator var2 = par1List.iterator();

        while (var2.hasNext())
        {
            EntityPlayer var3 = (EntityPlayer)var2.next();
            this.updatePlayerEntity((EntityPlayerMP)var3);
        }
    }

    private Packet getSpawnPacket()
    {
        if (this.trackedEntity.isDead)
        {
            System.out.println("Fetching addPacket for removed entity");
        }

        if (this.trackedEntity instanceof EntityItem)
        {
            EntityItem var8 = (EntityItem)this.trackedEntity;
            Packet21PickupSpawn var9 = new Packet21PickupSpawn(var8);
            var8.posX = (double)var9.xPosition / 32.0D;
            var8.posY = (double)var9.yPosition / 32.0D;
            var8.posZ = (double)var9.zPosition / 32.0D;
            return var9;
        }
        else if (this.trackedEntity instanceof EntityPlayerMP)
        {
            return new Packet20NamedEntitySpawn((EntityPlayer)this.trackedEntity);
        }
        else
        {
            if (this.trackedEntity instanceof EntityMinecart)
            {
                EntityMinecart var1 = (EntityMinecart)this.trackedEntity;

                if (var1.minecartType == 0)
                {
                    return new Packet23VehicleSpawn(this.trackedEntity, 10);
                }

                if (var1.minecartType == 1)
                {
                    return new Packet23VehicleSpawn(this.trackedEntity, 11);
                }

                if (var1.minecartType == 2)
                {
                    return new Packet23VehicleSpawn(this.trackedEntity, 12);
                }
            }

            if (this.trackedEntity instanceof EntityBoat)
            {
                return new Packet23VehicleSpawn(this.trackedEntity, 1);
            }
            else if (!(this.trackedEntity instanceof IAnimals) && !(this.trackedEntity instanceof EntityDragon))
            {
                if (this.trackedEntity instanceof EntityFishHook)
                {
                    EntityPlayer var7 = ((EntityFishHook)this.trackedEntity).angler;
                    return new Packet23VehicleSpawn(this.trackedEntity, 90, var7 != null ? var7.entityId : this.trackedEntity.entityId);
                }
                else if (this.trackedEntity instanceof EntityArrow)
                {
                    Entity var6 = ((EntityArrow)this.trackedEntity).shootingEntity;
                    return new Packet23VehicleSpawn(this.trackedEntity, 60, var6 != null ? var6.entityId : this.trackedEntity.entityId);
                }
                else if (this.trackedEntity instanceof EntitySnowball)
                {
                    return new Packet23VehicleSpawn(this.trackedEntity, 61);
                }
                else if (this.trackedEntity instanceof EntityPotion)
                {
                    return new Packet23VehicleSpawn(this.trackedEntity, 73, ((EntityPotion)this.trackedEntity).getPotionDamage());
                }
                else if (this.trackedEntity instanceof EntityExpBottle)
                {
                    return new Packet23VehicleSpawn(this.trackedEntity, 75);
                }
                else if (this.trackedEntity instanceof EntityEnderPearl)
                {
                    return new Packet23VehicleSpawn(this.trackedEntity, 65);
                }
                else if (this.trackedEntity instanceof EntityEnderEye)
                {
                    return new Packet23VehicleSpawn(this.trackedEntity, 72);
                }
                else
                {
                    Packet23VehicleSpawn var2;

                    if (this.trackedEntity instanceof EntitySmallFireball)
                    {
                        EntitySmallFireball var5 = (EntitySmallFireball)this.trackedEntity;
                        var2 = null;

                        if (var5.shootingEntity != null)
                        {
                            var2 = new Packet23VehicleSpawn(this.trackedEntity, 64, var5.shootingEntity.entityId);
                        }
                        else
                        {
                            var2 = new Packet23VehicleSpawn(this.trackedEntity, 64, 0);
                        }

                        var2.speedX = (int)(var5.accelerationX * 8000.0D);
                        var2.speedY = (int)(var5.accelerationY * 8000.0D);
                        var2.speedZ = (int)(var5.accelerationZ * 8000.0D);
                        return var2;
                    }
                    else if (this.trackedEntity instanceof EntityFireball)
                    {
                        EntityFireball var4 = (EntityFireball)this.trackedEntity;
                        var2 = null;

                        if (var4.shootingEntity != null)
                        {
                            var2 = new Packet23VehicleSpawn(this.trackedEntity, 63, ((EntityFireball)this.trackedEntity).shootingEntity.entityId);
                        }
                        else
                        {
                            var2 = new Packet23VehicleSpawn(this.trackedEntity, 63, 0);
                        }

                        var2.speedX = (int)(var4.accelerationX * 8000.0D);
                        var2.speedY = (int)(var4.accelerationY * 8000.0D);
                        var2.speedZ = (int)(var4.accelerationZ * 8000.0D);
                        return var2;
                    }
                    else if (this.trackedEntity instanceof EntityEgg)
                    {
                        return new Packet23VehicleSpawn(this.trackedEntity, 62);
                    }
                    else if (this.trackedEntity instanceof EntityTNTPrimed)
                    {
                        return new Packet23VehicleSpawn(this.trackedEntity, 50);
                    }
                    else if (this.trackedEntity instanceof EntityEnderCrystal)
                    {
                        return new Packet23VehicleSpawn(this.trackedEntity, 51);
                    }
                    else if (this.trackedEntity instanceof EntityFallingSand)
                    {
                        EntityFallingSand var3 = (EntityFallingSand)this.trackedEntity;
                        return new Packet23VehicleSpawn(this.trackedEntity, 70, var3.blockID | var3.field_70285_b << 16);
                    }
                    else if (this.trackedEntity instanceof EntityPainting)
                    {
                        return new Packet25EntityPainting((EntityPainting)this.trackedEntity);
                    }
                    else if (this.trackedEntity instanceof EntityXPOrb)
                    {
                        return new Packet26EntityExpOrb((EntityXPOrb)this.trackedEntity);
                    }
                    else
                    {
                        throw new IllegalArgumentException("Don\'t know how to add " + this.trackedEntity.getClass() + "!");
                    }
                }
            }
            else
            {
                this.field_73140_i = MathHelper.floor_float(this.trackedEntity.func_70079_am() * 256.0F / 360.0F);
                return new Packet24MobSpawn((EntityLiving)this.trackedEntity);
            }
        }
    }

    /**
     * Remove a tracked player from our list and tell the tracked player to destroy us from their world.
     */
    public void removeTrackedPlayerSymmetric(EntityPlayerMP par1EntityPlayerMP)
    {
        if (this.trackedPlayers.contains(par1EntityPlayerMP))
        {
            this.trackedPlayers.remove(par1EntityPlayerMP);
            par1EntityPlayerMP.field_71130_g.add(Integer.valueOf(this.trackedEntity.entityId));
        }
    }
}
