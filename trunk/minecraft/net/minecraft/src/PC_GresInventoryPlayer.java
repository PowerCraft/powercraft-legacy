package net.minecraft.src;

/**
 * 
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public class PC_GresInventoryPlayer extends PC_GresLayoutV {
	
	private boolean showLabel = true;

	/**
	 * @param labelVisible show inventory label.
	 * 
	 */
	public PC_GresInventoryPlayer(boolean labelVisible) {
		showLabel = labelVisible;
		setAlignH(PC_GresAlign.CENTER);
		setAlignV(PC_GresAlign.TOP);
	
	}
	
	@Override
	public void addedToWidget() {
		if(containerManager == null){
			System.out.println("Container is null");
			return;
		}
		System.out.println("Container manager not null.");
		
		canAddWidget = true;
		if(showLabel) add(new PC_GresLabel(PC_Lang.tr("container.inventory")));
		
		PC_GresInventory inv1 = new PC_GresInventory(new PC_CoordI(9, 3));
		inv1.slots = getContainerManager().inventoryPlayerUpper;
		add(inv1);
		
		PC_GresInventory inv2 = new PC_GresInventory(new PC_CoordI(9, 1));
		inv2.slots = getContainerManager().inventoryPlayerLower;
		add(inv2);
		canAddWidget = false;
		super.addedToWidget();
	}


}
