package net.minecraft.src;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Crafting Tool GRES GUI
 * 
 * @author MightyPork & XOR19
 * @copy (c) 2012
 *
 */
public class PCco_GuiCraftingTool implements PC_IGresBase{

	private PC_GresButton buttonPrev, buttonNext;
	private EntityPlayer player;
	
	private PC_GresInventory craftingToolInventory;
	private PCco_CraftingToolManager craftingToolManager;
	private static int page = 0;
	
	private static final int invWidth = 13;
	private static final int invHeight = 7;
	
	/**
	 * @param player the player
	 */
	public PCco_GuiCraftingTool(EntityPlayer player){
		this.player = player;
		player.addStat(AchievementList.openInventory, 1);
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return player;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(PC_Lang.tr("pc.gui.craftingTool.title"));
		w.padding.setTo(10,3);
		w.gapUnderTitle = 8;
		PC_GresWidget hg;
		
		craftingToolManager = new PCco_CraftingToolManager();
		craftingToolInventory = new PC_GresInventory(new PC_CoordI(invWidth, invHeight));
		for (int i = 0; i < invWidth; i++){
			for (int j = 0; j < invHeight; j++) {				
				int indexSlot = invWidth * invHeight + j * invWidth + i;				
				craftingToolInventory.setSlot(new PCco_SlotDirectCrafting(player, null, indexSlot, 0, 0), i, j);
			}
		}
		
		w.add(craftingToolInventory);
		
		hg = new PC_GresLayoutH().setAlignV(PC_GresAlign.TOP);
		hg.add(buttonPrev = (PC_GresButton) new PC_GresButton("<<<").setMinWidth(30));
		if(page<=0){
			page=0;
			buttonPrev.enable(false);
		}
		
		hg.add(new PC_GresInventoryPlayer(false));
		
		hg.add(buttonNext = (PC_GresButton) new PC_GresButton(">>>").setMinWidth(30));
		if(page>=getMaxPages()){
			page = getMaxPages();
			buttonNext.enable(false);
		}
		w.add(hg);		
		gui.add(w);
		gui.setCanShiftTransfer(false);
		
		loadSlotsForCorrentPage(gui);
		
		actionPerformed(craftingToolInventory, gui);
	}

	private void loadSlotsForCorrentPage(PC_IGresGui gui){
		for (int i = 0; i < invWidth; i++){
			for (int j = 0; j < invHeight; j++) {

				int indexInList = page * invWidth * invHeight + j * invWidth + i;
				
				((PCco_SlotDirectCrafting) craftingToolInventory.getSlot(i, j)).setProduct(craftingToolManager.getItemForSlotNumber(indexInList));

			}
		}
		
		actionPerformed(craftingToolInventory, gui);
	}
	
	private int getMaxPages(){
		return (craftingToolManager.stacklist.size() / (invWidth*invHeight));
	}
	
	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		player.inventory.closeChest();
	}
	
	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==craftingToolInventory){
			for (int i = 0; i < invWidth; i++){
				for (int j = 0; j < invHeight; j++) {
					Slot slot = craftingToolInventory.getSlot(i, j);
					if(slot!=null)
						((PCco_SlotDirectCrafting)(slot)).updateAvailability();
				}
			}
			
		}else if(widget==buttonPrev){
			page--;
			if(page<=0){
				page=0;
				buttonPrev.enable(false);
			}
			if(page<getMaxPages()){
				buttonNext.enable(true);
			}
			loadSlotsForCorrentPage(gui);
				
		}else if(widget==buttonNext){
			page++;
			if(page>=getMaxPages()){
				page = getMaxPages();
				buttonNext.enable(false);
			}
			if(page>0){
				buttonPrev.enable(true);
			}
			loadSlotsForCorrentPage(gui);
		}
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		gui.close();
	}
	
}


