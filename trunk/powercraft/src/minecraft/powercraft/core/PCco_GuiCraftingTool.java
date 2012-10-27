package powercraft.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import powercraft.core.PC_GresWidget.PC_GresAlign;

public class PCco_GuiCraftingTool extends PCco_ContainerCraftingTool implements PC_IGresClient {

	private static class Page{
		public PC_GresWidget widget;
		public PC_GresWidget tabWidget;
		public PC_GresInventory inv;
		public PC_GresButton buttonRight;
		public PC_GresButton buttonLeft;
		public boolean scroll;
		public int page;
		public List<PCco_SlotDirectCrafting> slots;
	}
	
	private List<Page> pages = new ArrayList<Page>();
	private PC_GresTextEdit search;
	private Page searchPage;
	private PC_GresButton trashAll;
	private PC_GresButton sort;
	
	public PCco_GuiCraftingTool(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	private Page addPage(String name){
		Page page = new Page();
		page.tabWidget = new PC_GresLabel(name);
		PC_GresLayoutV lv = new PC_GresLayoutV();
		lv.setAlignH(PC_GresAlign.STRETCH);
		page.slots = moduleList.get(name);
		page.scroll = page.slots.size()>12*5;
		lv.add(page.inv = new PC_GresInventory(12, page.scroll?4:5));
		if(page.scroll){
			PC_GresLayoutH lh = new PC_GresLayoutH();
			lh.setAlignH(PC_GresAlign.JUSTIFIED);
			lh.add((page.buttonLeft = new PC_GresButton("<<<")).setButtonPadding(4, 4).enable(false));
			lh.add(page.buttonRight = new PC_GresButton(">>>").setButtonPadding(4, 4));
			lv.add(lh);
		}
		page.widget = lv;
		displayPage(page);
		return page;
	}
	
	private void displayPage(Page page){
		int maxSlots = page.inv.gridHeight*page.inv.gridWidth;
		if(page.scroll){
			int maxPage = page.slots.size() / maxSlots;
			if(page.page>=maxPage){
				page.buttonRight.enable(false);
				page.page = maxPage;
			}else{
				page.buttonRight.enable(true);
			}
			if(page.page<=0){
				page.buttonLeft.enable(false);
				page.page = 0;
			}else{
				page.buttonLeft.enable(true);
			}
		}
		int i=page.page*maxSlots;
		for(int y=0; y<page.inv.gridHeight; y++){
			for(int x=0; x<page.inv.gridWidth; x++){
				if(page.slots.size()>i){
					page.inv.setSlot(page.slots.get(i), x, y);
				}else{
					page.inv.setSlot(null, x, y);
				}
				i++;
			}
		}
	}
	
	private void searchItems(){
		searchPage.slots.clear();
		String searchString = search.getText().toLowerCase();
		Collection<List<PCco_SlotDirectCrafting>> cls = moduleList.values();
		for(List<PCco_SlotDirectCrafting> ls: cls){
			for(PCco_SlotDirectCrafting s:ls){
				List<String> info = (List<String>)s.getBackgroundStack().getItemNameandInformation();
				for(String infoString:info){
					if (infoString.toLowerCase().contains(searchString)){
						searchPage.slots.add(s);
						break;
					}
				}
			}
		}
		for(PCco_SlotDirectCrafting s:allMcSlots){
			List<String> info = (List<String>)s.getBackgroundStack().getItemNameandInformation();
			for(String infoString:info){
				if (infoString.toLowerCase().contains(searchString)){
					searchPage.slots.add(s);
					break;
				}
			}
		}
		displayPage(searchPage);
	}
	
	private Page addPageSearch(){
		searchPage = new Page();
		searchPage.slots = new ArrayList<PCco_SlotDirectCrafting>();
		searchPage.tabWidget = new PC_GresLabel(PC_Utils.tr("pc.gui.craftingTool.search"));
		searchPage.scroll = true;
		PC_GresLayoutV lv = new PC_GresLayoutV();
		lv.setAlignH(PC_GresAlign.STRETCH);
		search = new PC_GresTextEdit("", 20);
		lv.add(search);
		lv.add(searchPage.inv = new PC_GresInventory(12, 2));
		PC_GresLayoutH lh = new PC_GresLayoutH();
		lh.setAlignH(PC_GresAlign.JUSTIFIED);
		lh.add((searchPage.buttonLeft = new PC_GresButton("<<<")).setButtonPadding(4, 4).enable(false));
		lh.add(searchPage.buttonRight = new PC_GresButton(">>>").setButtonPadding(4, 4));
		lv.add(lh);
		searchPage.widget = lv;
		searchItems();
		return searchPage;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(PC_Utils.tr("pc.gui.craftingTool.title"));
		w.setAlignH(PC_GresAlign.STRETCH);
		
		PC_GresTab t = new PC_GresTab();
		
		Set<String> keys = moduleList.keySet();
		
		PC_GresWidget td=null;
		
		for(String key:keys){
			Page page = addPage(key);
			pages.add(page);
			if(key.equalsIgnoreCase("core"))
				td = page.widget;
			t.addTab(page.widget, page.tabWidget);
		}
		
		searchPage = addPageSearch();
		t.addTab(searchPage.widget, searchPage.tabWidget);
		
		PC_GresLayoutV lv = new PC_GresLayoutV();
		
		lv.add(new PC_GresInventoryBigSlot(trash));
		lv.add(trashAll = new PC_GresButton(PC_Utils.tr("pc.gui.craftingTool.trashAll")));
		lv.add(sort = new PC_GresButton(PC_Utils.tr("pc.gui.craftingTool.sort")));
		
		t.addTab(lv, new PC_GresLabel(PC_Utils.tr("pc.gui.craftingTool.trash")));
		
		if(td!=null)
			t.makeTabVisible(td);
		
		w.add(t);
		
		PC_GresLayoutH lh = new PC_GresLayoutH();
		lh.setAlignH(PC_GresAlign.CENTER);
		lh.add(new PC_GresInventoryPlayer(true));
		w.add(lh);
		
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	private void updatePage(Page p, PC_GresWidget widget){
		if(p.scroll){
			boolean b=false;
			if(widget==p.buttonLeft){
				b = true;
				p.page--;
			}else if(widget==p.buttonRight){
				b = true;
				p.page++;
			}
			displayPage(p);
		}
	}
	
	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==search){
			searchItems();
			return;
		}
		if(widget==trashAll){
			IInventory inv = thePlayer.inventory;
			for (int i = 0; i < inv.getSizeInventory() - 4; i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack != null) {
					if (stack.itemID != mod_PowerCraftCore.craftingTool.shiftedIndex) {
						inv.decrStackSize(i, inv.getStackInSlot(i).stackSize);
					}
				}
			}
			PC_PacketHandler.sendToPacketHandler(thePlayer, "DeleteAllPlayerStacks", "Delete");
		}
		if(widget==sort){
			InventoryPlayer inv = thePlayer.inventory;
			List<ItemStack> stacks = new ArrayList<ItemStack>();
			for (int i = 0; i < inv.getSizeInventory() - 4; i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack != null) {
					inv.setInventorySlotContents(i, null);
					stacks.add(stack);
				}
			}

			if (stacks.size() == 0) return;

			PC_InvUtils.groupStacks(stacks);

			List<ItemStack> sorted = new ArrayList<ItemStack>();

			while (stacks.size() > 0) {
				ItemStack lowest = null;
				int indexLowest = -1;
				for (int i = 0; i < stacks.size(); i++) {
					ItemStack checked = stacks.get(i);
					if (checked == null) {
						indexLowest = i;
						break;
					}

					if (lowest == null
							|| (checked.itemID == mod_PowerCraftCore.craftingTool.shiftedIndex && lowest.itemID != mod_PowerCraftCore.craftingTool.shiftedIndex)
							|| ((lowest.itemID * 32000 * 64 + lowest.getItemDamage() * 64 + lowest.stackSize) > (checked.itemID * 32000 * 64
									+ checked.getItemDamage() * 64 + checked.stackSize) && lowest.itemID != mod_PowerCraftCore.craftingTool.shiftedIndex)) {
						lowest = checked;
						indexLowest = i;
					}
				}
				if (lowest != null) sorted.add(stacks.remove(indexLowest));
			}

			for (ItemStack stack : sorted) {
				inv.addItemStackToInventory(stack);
			}
			
			PC_PacketHandler.sendToPacketHandler(thePlayer, "DeleteAllPlayerStacks", "Sort");
			
		}
		for(Page p:pages){
			updatePage(p, widget);
		}
		updatePage(searchPage, widget);
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		
	}

	@Override
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2, float par3) {
		return false;
	}

}
