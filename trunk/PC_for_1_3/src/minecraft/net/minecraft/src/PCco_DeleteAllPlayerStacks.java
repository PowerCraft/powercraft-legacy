package net.minecraft.src;

public class PCco_DeleteAllPlayerStacks extends PC_PacketHandler {

	@Override
	public void handleIncomingPacket(World world, Object[] o) {
		EntityPlayer player = world.getPlayerEntityByName((String)o[0]);
		IInventory inv = player.inventory;
		for (int i = 0; i < inv.getSizeInventory() - 4; i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack != null) {
				if (stack.itemID != mod_PCcore.craftingTool.shiftedIndex) {
					inv.decrStackSize(i, inv.getStackInSlot(i).stackSize);
					//inv.setInventorySlotContents(i, null);
				}
			}
		}
	}

}
