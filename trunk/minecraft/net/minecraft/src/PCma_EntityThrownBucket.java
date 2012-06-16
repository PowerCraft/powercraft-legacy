package net.minecraft.src;

/**
 * Thrown bucket entity. Turns into item when hits something, can milk cow and if bowl subtype, also mooshroom.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCma_EntityThrownBucket extends EntityThrowable implements PCma_IThrownItem {

	/** is Bowl? (bucket=false) */
	public boolean isBowl = false;

	/**
	 * New Thrown Bucket
	 * 
	 * @param world the world
	 * @param d x
	 * @param d1 y
	 * @param d2 z
	 * @param bowl is bowl?
	 */
	public PCma_EntityThrownBucket(World world, double d, double d1, double d2, boolean bowl) {
		super(world, d, d1, d2);
		isBowl = bowl;
	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		if (movingobjectposition.entityHit != null) {

			if (movingobjectposition.entityHit instanceof EntityCow) {

				if (movingobjectposition.entityHit instanceof EntityMooshroom) {
					if (isBowl) {
						EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.bowlSoup, 1, 0));
						entityitem.motionX = motionX;
						entityitem.motionY = motionY;
						entityitem.motionZ = motionZ;
						worldObj.spawnEntityInWorld(entityitem);
					} else {
						dropOrigItem();
					}
				} else {
					if (!isBowl) {
						EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(Item.bucketMilk, 1, 0));
						entityitem.motionX = motionX;
						entityitem.motionY = motionY;
						entityitem.motionZ = motionZ;
						worldObj.spawnEntityInWorld(entityitem);
					} else {
						dropOrigItem();
					}
				}

			} else {
				dropOrigItem();
			}

			movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), 0);

		} else {
			dropOrigItem();
		}

		if (!worldObj.isRemote) {
			setDead();
		}
	}

	private void dropOrigItem() {
		EntityItem entityitem = new EntityItem(worldObj, posX, posY, posZ, new ItemStack(isBowl ? Item.bowlEmpty : Item.bucketEmpty, 1, 0));
		entityitem.motionX = motionX;
		entityitem.motionY = motionY;
		entityitem.motionZ = motionZ;
		worldObj.spawnEntityInWorld(entityitem);
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

	@Override
	public byte getIconIndex() {
		return (byte) (isBowl ? Item.bowlEmpty.iconIndex : Item.bucketEmpty.iconIndex);
	}
}
