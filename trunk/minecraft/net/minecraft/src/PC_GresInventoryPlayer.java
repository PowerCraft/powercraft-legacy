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
	protected PC_GresAlign labelAlign = PC_GresAlign.LEFT;

	/**
	 * @param labelVisible show inventory label.
	 * 
	 */
	public PC_GresInventoryPlayer(boolean labelVisible) {
		showLabel = labelVisible;
		setAlignH(labelAlign);
		setAlignV(PC_GresAlign.TOP);
	
	}
	
	@Override
	public void addedToWidget() {
		if(containerManager == null){
			return;
		}
		
		
		canAddWidget = true;
		PC_GresWidget label = new PC_GresLabel(PC_Lang.tr("container.inventory")).setWidgetMargin(2).setColor(textColorEnabled, 0x404040);
		if(showLabel) add(label);
		
		PC_GresInventory inv1 = new PC_GresInventory(new PC_CoordI(9, 3));
		inv1.slots = getContainerManager().inventoryPlayerUpper;
		add(inv1.setWidgetMargin(4));
		
		PC_GresInventory inv2 = new PC_GresInventory(new PC_CoordI(9, 1));
		inv2.slots = getContainerManager().inventoryPlayerLower;
		add(inv2.setWidgetMargin(4));
		canAddWidget = false;
		super.addedToWidget();
	}
	
	


}
