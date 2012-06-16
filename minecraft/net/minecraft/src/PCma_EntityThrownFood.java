package net.minecraft.src;

/**
 * Thrown food or wheat item from dispenser.<br>
 * Can start 'love mode' when hits animal.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_EntityThrownFood extends EntityThrowable implements PCma_IThrownItem {

	private Item itemThrown;

	/**
	 * new flying food
	 * 
	 * @param world the world
	 * @param d x
	 * @param d1 y
	 * @param d2 z
	 * @param item the flying item.
	 */
	public PCma_EntityThrownFood(World world, double d, double d1, double d2, Item item) {
		super(world, d, d1, d2);
		itemThrown = item;

	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		if (movingobjectposition.entityHit != null) {
			if (movingobjectposition.entityHit instanceof EntityAnimal) {
				EntityPlayer fplayer = new PC_FakePlayer(worldObj);
				Entity entity = movingobjectposition.entityHit;
				fplayer.inventory.setInventorySlotContents(0, new ItemStack(itemThrown, 1, 0));

				boolean tamed = false;
				if (entity instanceof EntityTameable) {
					tamed = ((EntityTameable) entity).isTamed();
					((EntityTameable) entity).setTamed(true);
				}

				((EntityAnimal) movingobjectposition.entityHit).interact(fplayer);

				if (entity instanceof EntityTameable) {
					((EntityTameable) entity).setTamed(tamed);
				}
				if (fplayer.inventory.getStackInSlot(0) == null) {} else {}

			} else {}

		} else {}

		EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(itemThrown, 1, 0));
		entityitem.motionX = motionX;
		entityitem.motionY = motionY;
		entityitem.motionZ = motionZ;
		worldObj.spawnEntityInWorld(entityitem);

		if (!worldObj.isRemote) {
			setDead();
		}
	}

	@Override
	public void setThrowableHeading(double d, double d1, double d2, float f, float f1) {
		super.setThrowableHeading(d, d1, d2, f, f1);
		float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
		d /= f2;
		d1 /= f2;
		d2 /= f2;
		d += (0.7F + rand.nextGaussian() * 0.3F) * 0.0074999998323619366D * f1;
		d1 += (0.7F + rand.nextGaussian() * 0.3F) * 0.0074999998323619366D * f1;
		d2 += (0.7F + rand.nextGaussian() * 0.3F) * 0.0074999998323619366D * f1;
		d *= f;
		d1 *= f;
		d2 *= f;
		motionX = d;
		motionY = d1;
		motionZ = d2;
		float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
		prevRotationYaw = rotationYaw = (float) ((Math.atan2(d, d2) * 180D) / 3.1415927410125732D);
		prevRotationPitch = rotationPitch = (float) ((Math.atan2(d1, f3) * 180D) / 3.1415927410125732D);
	}

	/**
	 * Icon of the flying food
	 */
	@Override
	public byte getIconIndex() {
		return (byte) itemThrown.iconIndex;
	}
}
