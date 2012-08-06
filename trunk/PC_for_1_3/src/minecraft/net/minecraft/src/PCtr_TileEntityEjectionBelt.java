package net.minecraft.src;


import java.util.Random;


/**
 * ItemStack extraction belt tile entity
 * 
 * @author MightyPork
 */
public class PCtr_TileEntityEjectionBelt extends PC_TileEntity {

	/** Random number generator */
	protected Random rand = new Random();


	/**
	 * Type of the action.<br>
	 * 0 = eject number of stacks 1 = eject number of items 2 = eject all stacks
	 * at once
	 */
	public int actionType = 0;

	/** Number of ejected stacks, if actionType == 0 */
	public int numStacksEjected = 1;

	/** Number of individual items stacks, if actionType == 1 */
	public int numItemsEjected = 1;

	/**
	 * ID of an algorithm used to pick the ejected item / stack 0 = first 1 =
	 * last 2 = random
	 */
	public int itemSelectMode = 0;

	/**
	 * 
	 */
	public PCtr_TileEntityEjectionBelt() {}

	@Override
	public final boolean canUpdate() {
		return false;
	}

	@Override
	public final void updateEntity() {}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);

		tag.setInteger("actionType", actionType);
		tag.setInteger("numStacks", numStacksEjected);
		tag.setInteger("numItems", numItemsEjected);
		tag.setInteger("selectMode", itemSelectMode);

	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);

		actionType = tag.getInteger("actionType");
		numStacksEjected = tag.getInteger("numStacks");
		numItemsEjected = tag.getInteger("numItems");
		itemSelectMode = tag.getInteger("selectMode");

	}

}
