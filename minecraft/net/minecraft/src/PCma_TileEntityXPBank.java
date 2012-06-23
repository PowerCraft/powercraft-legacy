package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class PCma_TileEntityXPBank extends PC_TileEntity {
	public PCma_TileEntityXPBank() {}

	private Random rand = new Random();

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

	public boolean canUpdate() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateEntity() {
		List<EntityXPOrb> hitList = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityXPOrb.class, AxisAlignedBB
				.getBoundingBoxFromPool(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(0.5D, 0.5D, 0.5D));

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

	public void printXP(EntityPlayer player) {
		if (xp == 0) {
			ModLoader.getMinecraftInstance().ingameGUI.clearChatMessages();
			ModLoader.getMinecraftInstance().thePlayer.addChatMessage("This storage is empty.");
			return;
		}
		ModLoader.getMinecraftInstance().ingameGUI.clearChatMessages();
		ModLoader.getMinecraftInstance().thePlayer.addChatMessage("This storage contains " + xp + " experience point"
				+ (xp > 1 ? "s." : "."));
	}

	public void withdrawXP(EntityPlayer player) {
		if (xp == 0) {
			ModLoader.getMinecraftInstance().ingameGUI.clearChatMessages();
			ModLoader.getMinecraftInstance().thePlayer.addChatMessage("This storage is empty.");
			return;
		}
		worldObj.playSoundAtEntity(player, "random.orb", 0.3F, 0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));

		int xpsum = 0;
		for (int i = 0; i < 5 && xp > 0; i++) {
			int addedXP = Math.min(xp, player.xpBarCap());
			player.addExperience(addedXP);
			xp -= addedXP;
			xpsum += addedXP;
		}

		if (xp < 0) {
			xp = 0;
		}

		notifyChange();

		ModLoader.getMinecraftInstance().ingameGUI.clearChatMessages();
		ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Gained " + xpsum + " experience point" + (xpsum > 1 ? "s." : "."));
	}

	public void depositXP(EntityPlayer player) {
		if (player.experienceLevel <= 0 && player.experience <= 0) {
			ModLoader.getMinecraftInstance().ingameGUI.clearChatMessages();
			ModLoader.getMinecraftInstance().thePlayer.addChatMessage("You have no experience to deposit.");
			return;
		}

		worldObj.playSoundAtEntity(player, "random.orb", 0.3F, 0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.4F));

		int totalDeposit = 0;
		int remainder = (int) (player.experience * getLevelSize(player.experienceLevel));
		player.experience = 0;

		totalDeposit += remainder;

		for (int j = 5; player.experienceLevel > 0 && j >= 0; j--) {

			player.experienceLevel--;
			int level = player.experienceLevel;
			int size = getLevelSize(level);
			totalDeposit += size;

		}

		xp += totalDeposit;
		player.experienceTotal -= totalDeposit;
		player.score -= totalDeposit;

		notifyChange();

		ModLoader.getMinecraftInstance().ingameGUI.clearChatMessages();
		ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Deposited " + totalDeposit + " experience point"
				+ (totalDeposit > 1 ? "s." : "."));
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

	private int getLevelSize(int level) {
		return 7 + (level * 7 >> 1);
	}
}
