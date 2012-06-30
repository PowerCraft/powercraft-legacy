package net.minecraft.src;



/**
 * Fake container for calls requiring container instance.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_ContainerFake extends Container {
	/**
	 * new fake container
	 */
	public PCma_ContainerFake() {}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void onCraftGuiClosed(EntityPlayer entityplayer) {}
}
