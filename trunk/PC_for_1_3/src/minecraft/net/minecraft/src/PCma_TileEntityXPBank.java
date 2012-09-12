package net.minecraft.src;


import java.util.List;
import java.util.Random;


/**
 * Experience storage tile entity
 * 
 * @author MightyPork
 */
public class PCma_TileEntityXPBank extends PC_TileEntity {

	private Random rand = new Random();

	/** XP points contained in the storage */
	public int xp = 0;

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		xp = nbttagcompound.getInteger("xp");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("xp", xp);
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void updateEntity() {
		List<EntityXPOrb> hitList = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityXPOrb.class,
				AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(0.5D, 0.5D, 0.5D));

		if (hitList.size() > 0) {
			Loop:
			for (EntityXPOrb orb : hitList) {
				if (orb.isDead) {
					continue Loop;
				}

				int oldxp = xp;
				worldObj.playSoundAtEntity(orb, "random.orb", 0.1F, 0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));
				xp += orb.getXpValue();
				orb.setDead();
				if (oldxp == 0 && xp > 0) {
					notifyChange();
				} else {
					notifyResize();
				}
			}
		}
		return;
	}

	/**
	 * withdraw all xp (before block removal)
	 * 
	 * @param player
	 */
	public void withdrawXP(EntityPlayer player) {
		if (xp == 0) {
			return;
		}
		worldObj.playSoundAtEntity(player, "random.orb", 0.3F, 0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));

		while (xp > 0) {
			int addedXP = Math.min(xp, player.xpBarCap());
			player.addExperience(addedXP);
			xp -= addedXP;
		}

		if (xp < 0) {
			xp = 0;
		}

		notifyChange();
	}

	private void notifyChange() {
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType().blockID);
		notifyResize();
	}

	private void notifyResize() {
		worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
		worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void set(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("xp"))
				xp = (Integer)o[p++];
		}
	}

	@Override
	public Object[] get() {
		return new Object[]{
				"xp", xp
		};
	}
}
