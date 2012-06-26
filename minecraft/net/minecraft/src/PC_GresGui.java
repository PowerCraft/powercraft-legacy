package net.minecraft.src;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * 
 * GuiScreen class
 * 
 * @authors XOR19, Rapus95, MightyPork
 * @copy (c) 2012
 * 
 */
public class PC_GresGui extends GuiContainer implements PC_IGresGui {

	/** The wrapped GUI */
	public PC_IGresBase gui;
	private PC_GresLayoutV child;
	private PC_GresWidget lastFocus;
	private PC_GresContainerManager containerManager;
	private boolean pauseGame = false;
	private boolean shiftTransfer = false;

	/**
	 * Constructor for creating a gui
	 * 
	 * @param gui the gui
	 */
	public PC_GresGui(PC_IGresBase gui) {
		super(new PC_GresContainerManager(gui.getPlayer()));
		this.gui = gui;
		containerManager = (PC_GresContainerManager) inventorySlots;
		containerManager.setGresGui(this);
	}

	@Override
	public PC_GresWidget add(PC_GresWidget widget) {
		PC_GresWidget c = child.add(widget);
		xSize = child.getSize().x;
		ySize = child.getSize().y;
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		child.setPosition(guiLeft, guiTop);
		return c;
	}

	@Override
	public void setPausesGame(boolean b) {
		pauseGame = b;
	}

	@Override
	public void setCanShiftTransfer(boolean b) {
		shiftTransfer = b;
	}

	@Override
	public boolean canShiftTransfer() {
		return shiftTransfer;
	}

	@Override
	public void close() {
		mc.displayGuiScreen(null);
		mc.setIngameFocus();
	}

	@Override
	public void updateScreen() {
		if (lastFocus != null) {
			lastFocus.updateCursorCounter();
		}
	}
	
	@Override
	public void setFocus(PC_GresWidget widget){
		if (widget != lastFocus) {
			if (lastFocus != null) {
				lastFocus.setFocus(false);
			}
			if (widget != null) {
				widget.setFocus(true);
			}
			lastFocus = widget;
		}
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		child = new PC_GresLayoutV();
		child.setFontRenderer(fontRenderer);
		child.setContainerManager(containerManager);
		child.setSize(0, 0);
		gui.initGui(this);
		super.initGui();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		gui.onGuiClosed(this);
		super.onGuiClosed();
	}

	@Override
	protected void keyTyped(char c, int i) {

		boolean consumed = false;

		if (lastFocus != null) {
			if (lastFocus.keyTyped(c, i)) {
				gui.actionPerformed(lastFocus, this);
				consumed = true;
			}
		}

		if (!consumed) {
			super.keyTyped(c, i);
		}
		if (!consumed && i == Keyboard.KEY_ESCAPE) {
			gui.onEscapePressed(this);
		} else if (!consumed && i == Keyboard.KEY_RETURN) {
			gui.onReturnPressed(this);
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);

		PC_GresWidget newFocus = child.getWidgetUnderMouse(new PC_CoordI(i, j));

		if (newFocus != lastFocus) {
			if (lastFocus != null) {
				lastFocus.setFocus(false);
			}
			if (newFocus != null) {
				newFocus.setFocus(true);
			}
			lastFocus = newFocus;
		}

		if (newFocus != null) {
			PC_CoordI fpos = newFocus.getPositionOnScreen();
			if (newFocus.mouseClick(new PC_CoordI(i - fpos.x, j - fpos.y), k)) {
				gui.actionPerformed(newFocus, this);
			}
		}
	}

	private void mouseMoved(int i, int j) {
		int wheel = Mouse.getDWheel();
		if (wheel < 0) {
			wheel = -1;
		}
		if (wheel > 0) {
			wheel = 1;
		}
		if (lastFocus != null) {
			PC_CoordI fpos = lastFocus.getPositionOnScreen();
			lastFocus.mouseMove(new PC_CoordI(i - fpos.x, j - fpos.y));
			lastFocus.mouseWheel(wheel);
		}
		child.getWidgetUnderMouse(new PC_CoordI(i, j));
	}

	private void mouseUp(int i, int j, int k) {
		if (lastFocus != null) {
			PC_CoordI fpos = lastFocus.getPositionOnScreen();
			if (lastFocus.mouseClick(new PC_CoordI(i - fpos.x, j - fpos.y), -1)) {
				gui.actionPerformed(lastFocus, this);
			}
		}
	}

	@Override
	protected void mouseMovedOrUp(int i, int j, int k) {
		super.mouseMovedOrUp(i, j, k);
		if (k != -1) {
			mouseUp(i, j, k);
		} else {
			mouseMoved(i, j);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return pauseGame;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		drawDefaultBackground();
		child.updateRenderer(new PC_CoordI(0, 0));
	}


	/**
	 * Draws the screen and all the components in it.
	 * 
	 * COPY FROM GuiContainer!<BR>
	 * NEEDED TO OVERRIDE render() AND FOR CUSTOM SLOT RENDERING.<br>
	 * <br>
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3) {

		gui.updateTick(this);

		drawDefaultBackground();
		int i = guiLeft;
		int j = guiTop;
		drawGuiContainerBackgroundLayer(par3, par1, par2);

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
			drawSlotInventory(slot1);

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

	/**
	 * COPY FROM GuiContainer!<BR>
	 * NEEDED TO OVERRIDE render() AND FOR CUSTOM SLOT RENDERING.<br>
	 * <br>
	 * Returns if the passed mouse position is over the specified slot.
	 */
	private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3) {
		int i = guiLeft;
		int j = guiTop;
		par2 -= i;
		par3 -= j;
		return par2 >= par1Slot.xDisplayPosition - 1 && par2 < par1Slot.xDisplayPosition + 16 + 1 && par3 >= par1Slot.yDisplayPosition - 1
				&& par3 < par1Slot.yDisplayPosition + 16 + 1;
	}


	/**
	 * Draws an inventory slot.<br>
	 * Almost copy from GuiContainer, but also does PCco_SlotDirectCrafting's ghostly rendering.
	 * 
	 * @param slot the slot
	 */
	private void drawSlotInventory(Slot slot) {
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

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {
		gui.onCraftMatrixChanged(iinventory);
	}

	@Override
	public Container getContainer() {
		return containerManager;
	}


}
