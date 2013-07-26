package powercraft.core.blocks;


import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import powercraft.api.energy.PC_IEnergyConsumer;
import powercraft.apiOld.PC_Utils;
import powercraft.apiOld.blocks.PC_TileEntity;


public class PC_TileEntityRoaster extends PC_TileEntity implements PC_IEnergyConsumer {

	private static final Random random = new Random();
	private static final int maxEnergyInput = 100;
	private List<EntityLivingBase> entities;
	private List<EntityItem> itemsList;
	private float energy = 0;
	private float request = 0;
	private boolean isActive = false;
	private boolean oldIsActive = false;


	@Override
	public float getEnergyRequest() {

		return request;
	}


	@Override
	public float consumeEnergy(float energy) {

		if (energy <= request) {
			request -= energy;
			this.energy += energy;
			return 0;
		} 
		energy -= request;
		this.energy += request;
		request = 0;
		return energy;
	}


	public boolean isActive() {

		return isActive;
	}


	@Override
	public void updateEntity() {

		super.updateEntity();
		if (!isClient()) {
			isActive = energy > 0.5;
			if (isActive != oldIsActive) {
				sendToClient();
				oldIsActive = isActive;
			}
			if (entities != null) {
				for (EntityLivingBase entity : entities) {
					if (energy > 0.5) {
						if (!entity.isImmuneToFire()) {
							entity.attackEntityFrom(DamageSource.inFire, 3);
						}

						if (!entity.isWet()) {
							entity.setFire(15);
						}
						energy -= 10;
					}
				}
			}
			if (itemsList != null) {
				for (EntityItem item : itemsList) {
					if (energy > 0.5) {
						if (item.getEntityItem().stackSize > 0) {
							EntityItem eitem = new EntityItem(worldObj, item.posX - 0.1F + random.nextFloat() * 0.2F, item.posY, item.posZ - 0.1F
									+ random.nextFloat() * 0.2F, PC_Utils.getSmeltingResult(item.getEntityItem()).splitStack(1));
							eitem.motionX = item.motionX;
							eitem.motionY = item.motionY;
							eitem.motionZ = item.motionZ;
							eitem.delayBeforeCanPickup = 7;

							worldObj.spawnEntityInWorld(eitem);

							if (--item.getEntityItem().stackSize <= 0) {
								item.setDead();
							}
							energy -= 10;
						}
					}
				}
			}
			energy = 0;
			entities = getEntities();
			itemsList = getItems();
			if (PC_Utils.getRedstoneValue(worldObj, xCoord, yCoord, zCoord) > 0) {
				request = entities.size() * 10 + 10;
				request += itemsList.size() * 10;
				if (request > maxEnergyInput) {
					request = maxEnergyInput;
				}
			} else {
				request = 0;
			}
		}
	}


	@SuppressWarnings("unchecked")
	private List<EntityLivingBase> getEntities() {

		List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
				AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
		Iterator<EntityLivingBase> iterator = entities.iterator();
		while (iterator.hasNext()) {
			EntityLivingBase entity = iterator.next();
			if (entity.isDead) {
				iterator.remove();
			}
		}
		return entities;
	}


	@SuppressWarnings("unchecked")
	private List<EntityItem> getItems() {

		List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class,
				AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
		Iterator<EntityItem> iterator = items.iterator();
		while (iterator.hasNext()) {
			EntityItem item = iterator.next();
			ItemStack itemStack = item.getEntityItem();
			if (item.isDead) {
				iterator.remove();
				continue;
			}
			ItemStack result = PC_Utils.getSmeltingResult(itemStack);
			if (result == null) {
				iterator.remove();
			}
		}
		return items;
	}


	@Override
	public void loadFromNBT(NBTTagCompound nbtTagCompound) {

		isActive = nbtTagCompound.getBoolean("isActive");
	}


	@Override
	public void saveToNBT(NBTTagCompound nbtTagCompound) {

		nbtTagCompound.setBoolean("isActive", isActive);
	}


	@Override
	public boolean canConsumerTubeConnectTo(int side) {

		return side != 1;
	}

}
