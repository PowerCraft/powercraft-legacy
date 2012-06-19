package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * PowerCraft crafting tool GUI
 * 
 * @author Ondrej Hruska
 * @copy (c) 2012
 */
/*public class PCco_GuiCraftingTool extends GuiContainer {
	private int inventoryRows;
	private GuiButton prev, next;
	private static int page = 0;

	/**
	 * Create Crafting tool GUI for player
	 * 
	 * @param player the player
	 */
	/*public PCco_GuiCraftingTool(EntityPlayer player) {
		super(new PCco_ContainerCraftingTool(player));
		inventoryRows = 0;
		allowUserInput = false;

		char c = '\336';
		int i = c - 108;
		inventoryRows = 7;
		ySize = i + inventoryRows * 18;
		xSize = 248;

		// achievement - open inventory
		player.addStat(AchievementList.openInventory, 1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		prev = new GuiButton(0, width / 2 - 115, height / 2 + 30, "<<<");
		next = new GuiButton(1, width / 2 + 115 - 30, height / 2 + 30, ">>>");
		prev.width = next.width = 30;
		prev.enabled = page > 0;
		next.enabled = page < Math.ceil(((PCco_ContainerCraftingTool) inventorySlots).stacklist.size() / (13 * 7));
		controlList.add(prev);
		controlList.add(next);

		super.initGui();
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) { return; }

		if (guibutton.id == 0) {
			page--;
		}

		if (guibutton.id == 1) {
			page++;
		}

		((PCco_ContainerCraftingTool) inventorySlots).loadStacksForPage(page);

		prev.enabled = page > 0;
		next.enabled = page < Math.ceil(((PCco_ContainerCraftingTool) inventorySlots).stacklist.size() / (13 * 7));

	}





	@Override
	protected void drawGuiContainerForegroundLayer() {
		fontRenderer.drawString(PC_Lang.tr("pc.gui.craftingTool.title"), 8, 6, 0x404040);
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 100, (ySize - 96) + 2, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int k = mc.renderEngine.getTexture("/PowerCraft/core/gui_craftingtool.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(k);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, inventoryRows * 18 + 17);
		drawTexturedModalRect(l, i1 + inventoryRows * 18 + 17, 0, 144, xSize, 96);
	}

	// OVERRIDE
	/**
	 * Draws an inventory slot
	 * 
	 * @param slot the slot
	 */
	/*private void drawSlotInventoryCT(Slot slot) {
		int i = slot.xDisplayPosition;
		int j = slot.yDisplayPosition;
		ItemStack itemstack = slot.getStack();
		boolean isNull = false;
		int k = i;
		int l = j;
		zLevel = 100F;
		itemRenderer.zLevel = 100F;

		if (itemstack == null) {
			int i1 = slot.getBackgroundIconIndex();

			if (i1 >= 0) {
				GL11.glDisable(GL11.GL_LIGHTING);
				mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/items.png"));
				drawTexturedModalRect(k, l, (i1 % 16) * 16, (i1 / 16) * 16, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
				isNull = true;
			}
		}

		if (isNull || itemstack == null) {
			if (slot instanceof PCco_SlotDirectCrafting) {
				PCco_SlotDirectCrafting dirslot = (PCco_SlotDirectCrafting) slot;
				if (dirslot.product != null) {
					itemRenderer.zLevel = 99F;
					zLevel = 99F;
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.2F);
					itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, dirslot.product, k, l);
					// itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, dirslot.product, k, l);


					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					int j1 = slot.xDisplayPosition;
					int k1 = slot.yDisplayPosition;
					drawGradientRect(j1, k1, j1 + 16, k1 + 16, 0xbb999999, 0xbb999999);
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					zLevel = 100F;
					itemRenderer.zLevel = 100F;
				}

			}
		} else {
			itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, k, l);
			itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, k, l);
		}

		itemRenderer.zLevel = 0.0F;
		zLevel = 0.0F;
	}

	/**
	 * Draws the screen and all the components in it.<br>
	 * This should be in all versions exactly the same as in GuiContainer, I copied it here to make it possible to override
	 * <b>drawSlotInventory<b>.
	 */
	/*@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		int i = guiLeft;
		int j = guiTop;
		drawGuiContainerBackgroundLayer(par3, par1, par2);
		
		for (int q = 0; q < controlList.size(); q++) {
			GuiButton guibutton = (GuiButton) controlList.get(q);
			guibutton.drawButton(mc, par1, par2);
		}

		RenderHelper.enableGUIStandardItemLighting();
		GL11.glPushMatrix();
		GL11.glTranslatef(i, j, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		Slot slot = null;
		int k = 240;
		int i1 = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0F, i1 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		for (int l = 0; l < inventorySlots.inventorySlots.size(); l++) {
			Slot slot1 = (Slot) inventorySlots.inventorySlots.get(l);
			drawSlotInventoryCT(slot1);

			if (isMouseOverSlot(slot1, par1, par2)) {
				slot = slot1;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				int j1 = slot1.xDisplayPosition;
				int k1 = slot1.yDisplayPosition;
				drawGradientRect(j1, k1, j1 + 16, k1 + 16, 0x80ffffff, 0x80ffffff);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		drawGuiContainerForegroundLayer();
		InventoryPlayer inventoryplayer = mc.thePlayer.inventory;

		if (inventoryplayer.getItemStack() != null) {
			GL11.glTranslatef(0.0F, 0.0F, 32F);
			zLevel = 200F;
			itemRenderer.zLevel = 200F;
			itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getItemStack(), par1 - i - 8, par2 - j - 8);
			itemRenderer
					.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getItemStack(), par1 - i - 8, par2 - j - 8);
			zLevel = 0.0F;
			itemRenderer.zLevel = 0.0F;
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		if (inventoryplayer.getItemStack() == null && slot != null && slot.getHasStack()) {
			ItemStack itemstack = slot.getStack();
			@SuppressWarnings("rawtypes")
			List list = itemstack.getItemNameandInformation();

			if (list.size() > 0) {
				int l1 = 0;

				for (int i2 = 0; i2 < list.size(); i2++) {
					int k2 = fontRenderer.getStringWidth((String) list.get(i2));

					if (k2 > l1) {
						l1 = k2;
					}
				}

				int j2 = (par1 - i) + 12;
				int l2 = par2 - j - 12;
				int i3 = l1;
				int j3 = 8;

				if (list.size() > 1) {
					j3 += 2 + (list.size() - 1) * 10;
				}

				zLevel = 300F;
				itemRenderer.zLevel = 300F;
				int k3 = 0xf0100010;
				drawGradientRect(j2 - 3, l2 - 4, j2 + i3 + 3, l2 - 3, k3, k3);
				drawGradientRect(j2 - 3, l2 + j3 + 3, j2 + i3 + 3, l2 + j3 + 4, k3, k3);
				drawGradientRect(j2 - 3, l2 - 3, j2 + i3 + 3, l2 + j3 + 3, k3, k3);
				drawGradientRect(j2 - 4, l2 - 3, j2 - 3, l2 + j3 + 3, k3, k3);
				drawGradientRect(j2 + i3 + 3, l2 - 3, j2 + i3 + 4, l2 + j3 + 3, k3, k3);
				int l3 = 0x505000ff;
				int i4 = (l3 & 0xfefefe) >> 1 | l3 & 0xff000000;
				drawGradientRect(j2 - 3, (l2 - 3) + 1, (j2 - 3) + 1, (l2 + j3 + 3) - 1, l3, i4);
				drawGradientRect(j2 + i3 + 2, (l2 - 3) + 1, j2 + i3 + 3, (l2 + j3 + 3) - 1, l3, i4);
				drawGradientRect(j2 - 3, l2 - 3, j2 + i3 + 3, (l2 - 3) + 1, l3, l3);
				drawGradientRect(j2 - 3, l2 + j3 + 2, j2 + i3 + 3, l2 + j3 + 3, i4, i4);

				for (int j4 = 0; j4 < list.size(); j4++) {
					String s = (String) list.get(j4);

					if (j4 == 0) {
						s = (new StringBuilder()).append("\247").append(Integer.toHexString(itemstack.getRarity().nameColor)).append(s)
								.toString();
					} else {
						s = (new StringBuilder()).append("\2477").append(s).toString();
					}

					fontRenderer.drawStringWithShadow(s, j2, l2, -1);

					if (j4 == 0) {
						l2 += 2;
					}

					l2 += 10;
				}

				zLevel = 0.0F;
				itemRenderer.zLevel = 0.0F;
			}
		}

		GL11.glPopMatrix();

		

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	// OVERRIDE
	/**
	 * Returns if the passed mouse position is over the specified slot.
	 * 
	 * @param par1Slot the slot
	 * @param par2 mouse x
	 * @param par3 mouse y
	 * @return is over
	 */
	/*private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3) {
		int i = guiLeft;
		int j = guiTop;
		par2 -= i;
		par3 -= j;
		return par2 >= par1Slot.xDisplayPosition - 1 && par2 < par1Slot.xDisplayPosition + 16 + 1 && par3 >= par1Slot.yDisplayPosition - 1
				&& par3 < par1Slot.yDisplayPosition + 16 + 1;
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		((PCco_ContainerCraftingTool) inventorySlots).mouseClicked();
	}
}
*/
public class PCco_GuiCraftingTool implements PC_IGresBase{

	private int inventoryRows;
	private PC_GresButton prev, next;
	private EntityPlayer player;
	private int page = 0;
	private PCco_ContainerCraftingTool craftingTool;
	private PC_GresInventory craftingToolInventory;
	
	private static final int invWidth = 16;
	private static final int invHeight = 6;
	
	public PCco_GuiCraftingTool(EntityPlayer player){
		this.player = player;
		player.addStat(AchievementList.openInventory, 1);
		craftingTool = new PCco_ContainerCraftingTool(player);
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(PC_Lang.tr("pc.gui.craftingTool.title"));
		PC_GresWidget hg;
		PC_GresWidget vg;
		
		craftingToolInventory= new PC_GresInventory(new PC_CoordI(invWidth, invHeight));
		for (int i = 0; i < invWidth; i++){
			for (int j = 0; j < invHeight; j++) {
				int indexInlist = page * invWidth * invHeight + j * invWidth + i;
				int indexSlot = invWidth * invHeight + j * invWidth + i;
				if(indexInlist<craftingTool.stacklist.size()){
					craftingToolInventory.setSlot(new PCco_SlotDirectCrafting(player, craftingTool.getItemForSlotNumber(indexInlist), indexSlot, 0, 0), i, j);
				}else{
					craftingToolInventory.setSlot(null, i, j);
				}

			}
		}
		w.add(craftingToolInventory);
		
		hg = new PC_GresLayoutH().setAlignV(PC_GresAlign.TOP);
		hg.add(prev = (PC_GresButton) new PC_GresButton("<<<").setMinWidth(30));
		if(page<=0){
			page=0;
			prev.enable(false);
		}
		
		hg.add(new PC_GresInventoryPlayer(false));
		
		hg.add(next = (PC_GresButton) new PC_GresButton(">>>").setMinWidth(30));
		if(page>=getMaxPages()){
			page = getMaxPages();
			next.enable(false);
		}
		w.add(hg);
		actionPerformed(craftingToolInventory, gui);
		gui.add(w);
		gui.setCanShiftTransfer(false);
	}

	private void recalcInventorySlots(PC_IGresGui gui){
		for (int i = 0; i < invWidth; i++){
			for (int j = 0; j < invHeight; j++) {

				int indexInlist = page * invWidth * invHeight + j * invWidth + i;
				int indexSlot = invWidth * invHeight + j * invWidth + i;
				if(indexInlist<craftingTool.stacklist.size())
					craftingToolInventory.setSlot(new PCco_SlotDirectCrafting(player, craftingTool.getItemForSlotNumber(indexInlist), indexSlot, 0, 0), i, j);
				else
					craftingToolInventory.setSlot(null, i, j);

			}
		}
		actionPerformed(craftingToolInventory, gui);
	}
	
	private int getMaxPages(){
		return (craftingTool.stacklist.size() / (invWidth*invHeight));
	}
	
	@Override
	public void onGuiClosed(PC_IGresGui gui) {player.inventory.closeChest();}
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
		}else if(widget==prev){
			page--;
			if(page<=0){
				page=0;
				prev.enable(false);
			}
			if(page<getMaxPages()){
				next.enable(true);
			}
			recalcInventorySlots(gui);
				
		}else if(widget==next){
			page++;
			if(page>=getMaxPages()){
				page = getMaxPages();
				next.enable(false);
			}
			if(page>0){
				prev.enable(true);
			}
			recalcInventorySlots(gui);
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


