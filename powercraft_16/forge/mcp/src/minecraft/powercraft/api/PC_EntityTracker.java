package powercraft.api;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PC_EntityTracker
{

	public static NBTTagCompound trackEntity(Entity entity)
	{
		if (isEntityIgnored(entity))
		{
			return null;
		}
		NBTTagCompound tagCompound = entity.getEntityData();
		NBTTagCompound powerCraftTag;
		if (tagCompound.hasKey("PowerCraft"))
		{
			powerCraftTag = tagCompound.getCompoundTag("PowerCraft");
		}
		else
		{
			powerCraftTag = new NBTTagCompound();
			tagCompound.setCompoundTag("PowerCraft", powerCraftTag);
		}
		return powerCraftTag;
	}

	public static void moveEntityXZ(Entity entity)
	{
		NBTTagCompound powerCraftTag = trackEntity(entity);
		if (powerCraftTag == null) return;
		powerCraftTag.removeTag("motionY");
		powerCraftTag.removeTag("posY");
		int lastUpdateTime = powerCraftTag.getInteger("lastUpdateTime");
		powerCraftTag.setInteger("lastUpdateTime", entity.ticksExisted);
		if (lastUpdateTime + 1 != entity.ticksExisted || lastUpdateTime == 0)
		{
			powerCraftTag.setDouble("motionX", entity.motionX);
			powerCraftTag.setDouble("motionZ", entity.motionZ);
		}
		else
		{
			double savedMotionX = powerCraftTag.getDouble("motionX");
			double savedMotionZ = powerCraftTag.getDouble("motionZ");
			if (entity instanceof EntityLivingBase)
			{
				EntityLivingBase living = (EntityLivingBase)entity;
				float s = PC_MathHelper.sin(living.rotationYaw * (float)Math.PI / 180.0F);
				float c = PC_MathHelper.cos(living.rotationYaw * (float)Math.PI / 180.0F);
				savedMotionX += (living.moveStrafing * c - living.moveForward * s) * 0.01;
				savedMotionZ += (living.moveForward * c + living.moveStrafing * s) * 0.01;
				powerCraftTag.setDouble("motionX", savedMotionX);
				powerCraftTag.setDouble("motionZ", savedMotionZ);
			}
			entity.motionX = savedMotionX;
			entity.motionZ = savedMotionZ;
		}
	}

	public static void moveEntityXYZ(Entity entity)
	{
		NBTTagCompound powerCraftTag = trackEntity(entity);
		if (powerCraftTag == null) return;
		int lastUpdateTime = powerCraftTag.getInteger("lastUpdateTime");
		powerCraftTag.setInteger("lastUpdateTime", entity.ticksExisted);
		if (lastUpdateTime + 1 != entity.ticksExisted || lastUpdateTime == 0)
		{
			powerCraftTag.setDouble("motionX", entity.motionX);
			powerCraftTag.setDouble("motionY", entity.motionY);
			powerCraftTag.setDouble("motionZ", entity.motionZ);
			powerCraftTag.setDouble("posY", entity.posY);
		}
		else
		{
			double savedMotionX = powerCraftTag.getDouble("motionX");
			double savedMotionY;
			double savedMotionZ = powerCraftTag.getDouble("motionZ");
			double savedPosY;
			if (powerCraftTag.hasKey("motionY"))
			{
				savedMotionY = powerCraftTag.getDouble("motionY");
			}
			else
			{
				powerCraftTag.setDouble("motionY", savedMotionY = entity.motionY);
			}
			if (powerCraftTag.hasKey("posY"))
			{
				savedPosY = powerCraftTag.getDouble("posY");
			}
			else
			{
				savedPosY = entity.posY;
			}
			// what does this next block do?!?
			if (entity instanceof EntityLivingBase)
			{
				EntityLivingBase living = (EntityLivingBase)entity;
				float s = PC_MathHelper.sin(living.rotationYaw * (float)Math.PI / 180.0F);
				float c = PC_MathHelper.cos(living.rotationYaw * (float)Math.PI / 180.0F);
				savedMotionX += (living.moveStrafing * c - living.moveForward * s) * 0.01;
				savedMotionZ += (living.moveForward * c + living.moveStrafing * s) * 0.01;
				powerCraftTag.setDouble("motionX", savedMotionX);
				powerCraftTag.setDouble("motionZ", savedMotionZ);
			}
			entity.motionX = savedMotionX;
			entity.motionY = savedMotionY;
			entity.motionZ = savedMotionZ;
			entity.posY = savedPosY + savedMotionY;
			powerCraftTag.setDouble("posY", savedPosY + savedMotionY);
		}
	}

	public static boolean isEntityIgnored(Entity entity)
	{
		if (entity == null)
		{
			return true;
		}

		if (!entity.isEntityAlive())
		{
			return true;
		}

		if (PC_Utils.isClient())
		{
			if (entity instanceof EntityFX)
			{
				return true;
			}
		}

		if (entity.ridingEntity != null)
		{
			return true;
		}

		if (entity instanceof EntityPlayer)
		{
			if (((EntityPlayer)entity).isSneaking())
			{
				return true;
			}
		}
		return false;
	}

}
