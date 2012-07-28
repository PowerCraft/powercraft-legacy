package net.minecraft.src;


import org.lwjgl.input.Keyboard;

import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Digital Workbench
 * 
 * @author MightyPork
 *
 */
public class PClo_GuiWeaselDiskManager implements PC_IGresBase {

	private static class TinyInv implements IInventory, PC_ISpecialAccessInventory {

		ItemStack slot = null;
		EntityPlayer player = null;

		public TinyInv(EntityPlayer player) {
			this.player = player;
		}

		@Override
		public void setInventorySlotContents(int i, ItemStack itemstack) {
			slot = itemstack;
		}

		@Override
		public void openChest() {}

		@Override
		public void onInventoryChanged() {}

		@Override
		public boolean isUseableByPlayer(EntityPlayer entityplayer) {
			return true;
		}

		@Override
		public ItemStack getStackInSlotOnClosing(int i) {
			return null;
		}

		@Override
		public ItemStack getStackInSlot(int i) {
			return slot;
		}

		@Override
		public int getSizeInventory() {
			return 1;
		}

		@Override
		public int getInventoryStackLimit() {
			return 64;
		}

		@Override
		public String getInvName() {
			return "";
		}

		@Override
		public ItemStack decrStackSize(int i, int j) {
			if (slot != null) {
				if (slot.stackSize <= j) {
					ItemStack itemstack = slot;
					slot = null;
					onInventoryChanged();
					return itemstack;
				}

				ItemStack itemstack1 = slot.splitStack(j);

				if (slot.stackSize == 0) {
					slot = null;
				}

				onInventoryChanged();
				return itemstack1;
			} else {
				return null;
			}
		}

		@Override
		public void closeChest() {
			player.dropPlayerItem(slot);
			slot = null;
		}

		@Override
		public boolean insertStackIntoInventory(ItemStack stack) {
			if (slot == null) {
				slot = stack;
				return true;
			}
			return false;
		}

		@Override
		public boolean needsSpecialInserter() {
			return false;
		}

		@Override
		public boolean canPlayerInsertStackTo(int slot, ItemStack stack) {
			return stack.itemID == mod_PClogic.weaselDisk.shiftedIndex;
		}

		@Override
		public boolean canMachineInsertStackTo(int slot, ItemStack stack) {
			return canPlayerInsertStackTo(slot, stack);
		}

		@Override
		public boolean canDispenseStackFrom(int slot) {
			return false;
		}
	}

	private PC_GresWindow w;
	private PC_GresWidget edName;
	private PC_GresColor colorBulb;
	private PC_GresWidget vg0, vgf, vgt, vgl, vgi;
	private PC_GresColorPicker colorPicker;
	private Slot theSlot, cpySlot1, cpySlot2;
	private PC_GresWidget btnRename;
	private PC_GresWidget btnRecolor;
	private PC_GresInventoryBigSlot slotInv;
	private PC_GresInventory cpyInv1, cpyInv2;

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}

	private int panelShown = -1;
	private PC_GresWidget edSeparator;
	private PC_GresTextEditMultiline edListText;
	private PC_GresTextEditMultiline edTextText;
	private PC_GresColor colorBulbI;
	private PC_GresColorPicker colorPickerI;
	private PC_GresTextEdit edImgX;
	private PC_GresTextEdit edImgY;
	private PC_GresWidget btnImgResize;
	private PC_GresWidget formatButtons;
	private PC_GresColorMap colormap;
	private PC_GresWidget panelwrap;
	private PC_GresWidget btnEdit;

	private void setPanelForStack(boolean edit) {
		if (theSlot.getStack() != null) {
			int type = PClo_ItemWeaselDisk.getType(theSlot.getStack());
			int panel = -1;
			if (type == PClo_ItemWeaselDisk.EMPTY || !edit) {
				panel = 0;
			} else if (type == PClo_ItemWeaselDisk.TEXT) {
				panel = 1;
			} else if (type == PClo_ItemWeaselDisk.IMAGE) {
				panel = 2;
			} else if (type == PClo_ItemWeaselDisk.STRINGLIST) {
				panel = 3;
			} else if (type == PClo_ItemWeaselDisk.NUMBERLIST) {
				panel = 3;
			}
			
			setPanel_do(panel);
			this.panelShown = panel;
		} else {
			setPanel_do(-1);
			this.panelShown = -1;
		}
	}

	private void setPanel_do(int panel) {
		ItemStack stack = theSlot.getStack();
		vgf.setVisible(false);
		vgt.setVisible(false);
		vgl.setVisible(false);
		vgi.setVisible(false);
		vg0.setVisible(false);
		cpyInv1.setSlot(null, 0, 0);
		cpyInv2.setSlot(null, 0, 0);
		cpySlot1.inventory.closeChest();
		cpySlot2.inventory.closeChest();
		
		formatButtons.setVisible(false);
		switch (panel) {
			case -1:
				w.setText(PC_Lang.tr("pc.gui.weasel.diskManager.empty.title"));
				vg0.setVisible(true);
				cpyInv1.setSlot(cpySlot1, 0, 0);
				cpyInv2.setSlot(cpySlot2, 0, 0);
				break;
			case 0:
				w.setText(theSlot.getStack().getItem().getItemDisplayName(theSlot.getStack()));
				vgf.setVisible(true);
				formatButtons.setVisible(true);
				btnEdit.setText(PC_Lang.tr("pc.gui.weasel.diskManager.edit"));
				break;
			case 1:
				w.setText(PC_Lang.tr("pc.weasel.disk.text"));
				edTextText.setText(PClo_ItemWeaselDisk.getText(stack));
				vgt.setVisible(true);
				btnEdit.setText(PC_Lang.tr("pc.gui.back"));

				break;
			case 2:
				w.setText(PC_Lang.tr("pc.weasel.disk.image"));
				colormap.setColorArray(PClo_ItemWeaselDisk.getImageData(stack));
				edImgX.text = PClo_ItemWeaselDisk.getImageSize(stack).x+"";
				edImgY.text = PClo_ItemWeaselDisk.getImageSize(stack).y+"";
				vgi.setVisible(true);
				btnEdit.setText(PC_Lang.tr("pc.gui.back"));

				break;
			case 3:

				w.setText(theSlot.getStack().getItem().getItemDisplayName(theSlot.getStack()));
				edListText.setText(PClo_ItemWeaselDisk.getListText(stack));
				edSeparator.setText(PClo_ItemWeaselDisk.getListDelimiter(stack));
				vgl.setVisible(true);
				btnEdit.setText(PC_Lang.tr("pc.gui.back"));

				break;
		}
		panelwrap.setMinSize(panelwrap.getMinSize());
		w.calcChildPositions();
	}

	@Override
	public void initGui(PC_IGresGui gui) {

		w = new PC_GresWindow(PC_Lang.tr("pc.gui.weasel.diskManager.title"));
		w.setAlignH(PC_GresAlign.CENTER);
		w.setMinWidth(320);

		PC_GresWidget hg, vg, vg1, hg1;


		panelwrap = new PC_GresLayoutV().setMinHeight(100).setAlignV(PC_GresAlign.CENTER).setAlignH(PC_GresAlign.CENTER).setWidgetMargin(0);



		// Formatting screen
		vgf = new PC_GresLayoutV();
		vgf.setAlignH(PC_GresAlign.CENTER);

		hg = new PC_GresLayoutH();
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.diskManager.label")));
		hg.add(edName = new PC_GresTextEdit("", 15,PC_GresInputType.IDENTIFIER).setWidgetMargin(2));
		hg.add(btnRename = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.set")).setMinWidth(0));
		vgf.add(hg);

		hg = new PC_GresLayoutH();

		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.diskManager.color")));
		PC_GresWidget frame = new PC_GresFrame();
		frame.add(colorBulb = new PC_GresColor(new PC_Color(0, 0, 0)));
		frame.add(colorPicker = new PC_GresColorPicker(0x000000, 60, 30));
		hg.add(frame);
		hg.add(btnRecolor = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.set")).setMinWidth(0));
		vgf.add(hg);


		// Text edit screen
		vgt = new PC_GresLayoutV();
		vgt.setAlignH(PC_GresAlign.STRETCH).setMinWidth(180);
		vgt.add(edTextText = new PC_GresTextEditMultiline("", 0, 100));
		

		vg0 = new PC_GresLayoutV();
		vg0.setAlignH(PC_GresAlign.CENTER).setMinWidth(180);
		vg0.add(new PC_GresImage(mod_PCcore.getImgDir()+"graphics.png", 80, 24, 112, 70));
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(cpyInv1 = new PC_GresInventory(1,1));
		cpyInv1.setSlot(cpySlot1 = new PC_SlotSelective(new TinyInv(getPlayer()), 0, 0, 0), 0, 0);
		
		hg.add(new PC_GresButtonImage(mod_PCcore.getImgDir()+"gres/widgets.png",new PC_CoordI(57,12),new PC_CoordI(13, 10)).setButtonPadding(3, 3).setId(-13));
		hg.add(cpyInv2 = new PC_GresInventory(1,1));
		cpyInv2.setSlot(cpySlot2 = new PC_SlotSelective(new TinyInv(getPlayer()), 0, 0, 0), 0, 0);
		vg0.add(hg);


		// List screen
		vgl = new PC_GresLayoutV();
		vgl.setAlignH(PC_GresAlign.STRETCH).setMinWidth(180);
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.diskManager.separator")));
		hg.add(edSeparator = new PC_GresTextEdit("", 15,PC_GresInputType.TEXT).setWidgetMargin(2));
		vgl.add(hg);
		vgl.add(edListText = new PC_GresTextEditMultiline("", 0, 70));


		// Image screen
		vgi = new PC_GresLayoutV();
		vgi.setAlignH(PC_GresAlign.LEFT);
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);

		// colors
		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER);
		vg.setMinWidth(80);
		frame = new PC_GresFrame().setWidgetMargin(1);
		vg1 = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(0);
		vg1.add(colorBulbI = (PC_GresColor) new PC_GresColor(new PC_Color(0, 0, 0)).setWidgetMargin(1));
		vg1.add(colorPickerI = new PC_GresColorPicker(0x000000, 60, 40)).setWidgetMargin(1);
		hg1 = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(0);
		hg1.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.img.clear")).setButtonPadding(3, 3).setMinWidth(35).setWidgetMargin(1).setId(50));
		hg1.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.img.fill")).setButtonPadding(3, 3).setMinWidth(35).setWidgetMargin(1).setId(51));
		vg1.add(hg1);
		hg1 = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(0);
		hg1.add(new PC_GresButton("-").setButtonPadding(3, 3).setWidgetMargin(1).setId(40).setMinWidth(35));
		hg1.add(new PC_GresButton("+").setButtonPadding(3, 3).setWidgetMargin(1).setId(41).setMinWidth(35));
		vg1.add(hg1);
		frame.add(vg1);
		vg.add(frame);
		hg.add(vg);

		// map
		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER);
		vg.setMinWidth(140);
		vg.add(colormap = new PC_GresColorMap(null).setScale(2).useKeyboard(false));
		hg.add(vg);

		// resize
		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER);
		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.diskManager.size")));
		hg1 = new PC_GresLayoutH();
		hg1.add(edImgX = new PC_GresTextEdit("0", 3, PC_GresInputType.UNSIGNED_INT));
		hg1.add(edImgY = new PC_GresTextEdit("0", 3, PC_GresInputType.UNSIGNED_INT));
		vg.add(hg1);
		vg.add(btnImgResize = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.resize")).setMinWidth(0));
		hg.add(vg);

		vgi.add(hg);


		vg0.setVisible(false);
		vgf.setVisible(false);
		vgt.setVisible(false);
		vgi.setVisible(false);
		vgl.setVisible(false);

		panelwrap.add(vg0);
		panelwrap.add(vgf);
		panelwrap.add(vgt);
		panelwrap.add(vgi);
		panelwrap.add(vgl);


		w.add(panelwrap);


		hg = new PC_GresLayoutH();
		vg = new PC_GresLayoutV().setWidgetMargin(5);
		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.diskManager.disk")).setWidgetMargin(2));
		vg.add(slotInv = new PC_GresInventoryBigSlot(theSlot = new PC_SlotSelective(new TinyInv(getPlayer()), 0, 0, 0)));
		vg.add(btnEdit = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.edit")).setButtonPadding(4, 4).setMinWidth(slotInv.size.x+10).setWidgetMargin(1).setId(99));

		hg.add(vg);
		vg = new PC_GresLayoutV();
		hg.add(new PC_GresInventoryPlayer(true).setWidgetMargin(2));
		vg.add(new PC_GresGap(0, 3));
		hg.add(vg);
		vg = new PC_GresLayoutV().setWidgetMargin(0).setAlignH(PC_GresAlign.LEFT);
		formatButtons = vg1 = new PC_GresLayoutV().setWidgetMargin(0).setAlignH(PC_GresAlign.LEFT);
		
		PC_GresWidget b0, b1, b2, b3;
		vg1.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.diskManager.format")).setWidgetMargin(1).setAlignH(PC_GresAlign.LEFT));
		vg1.add(b0 = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.formatText")).setButtonPadding(3, 3).setWidgetMargin(1).setId(100));
		vg1.add(b1 = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.formatImage")).setButtonPadding(3, 3).setWidgetMargin(1).setId(101));
		vg1.add(b2 = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.formatIntegerList")).setButtonPadding(3, 3).setWidgetMargin(1).setId(102));
		vg1.add(b3 = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.formatStringList")).setButtonPadding(3, 3).setWidgetMargin(1).setId(103));

		int width = w.getStringWidth(PC_Lang.tr("pc.gui.weasel.diskManager.format"));
		width = Math.max(width, w.getStringWidth(PC_Lang.tr("pc.gui.weasel.diskManager.formatText")));
		width = Math.max(width, w.getStringWidth(PC_Lang.tr("pc.gui.weasel.diskManager.formatImage")));
		width = Math.max(width, w.getStringWidth(PC_Lang.tr("pc.gui.weasel.diskManager.formatIntegerList")));
		width = Math.max(width, w.getStringWidth(PC_Lang.tr("pc.gui.weasel.diskManager.formatStringList")));
		width += 6;
		b0.minSize.x = b1.minSize.x = b2.minSize.x = b3.minSize.x = width;

		vg.setMinWidth(width);
		
		vg.add(vg1);

		hg.add(vg);
		w.add(hg);

		w.add(new PC_GresGap(0, 2));

		gui.add(w);
		setPanel_do(-1);

		btnEdit.enabled = false;

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		theSlot.inventory.closeChest();
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		
		if(widget.getId() == -13) {
			if(cpySlot1.getStack() != null && cpySlot2.getStack() != null ) {
				cpySlot2.putStack(cpySlot1.getStack().copy());
				PC_Utils.mc().sndManager.playSoundFX("random.wood click", 1.0F, 1.0F);
			}
		}

		if (widget == btnRecolor) {
			if (theSlot.getStack() != null) {
				PClo_ItemWeaselDisk.setColor(theSlot.getStack(), colorPicker.getColor());
			}
			return;
		}
		if (widget == btnRename) {
			String name = edName.getText().trim();
			if (theSlot.getStack() != null) {
				PClo_ItemWeaselDisk.setLabel(theSlot.getStack(), name);
			}
			return;
		}

		if (widget == colorPicker) {
			colorBulb.setColor(colorPicker.getColor());
			return;
		}

		if (widget == slotInv) {
			if (theSlot.getStack() != null) {
				btnEdit.enabled = PClo_ItemWeaselDisk.getType(theSlot.getStack()) != PClo_ItemWeaselDisk.EMPTY;				
				if (this.panelShown == -1) {
					setPanelForStack(false);
				}
				edName.setText(PClo_ItemWeaselDisk.getLabel(theSlot.getStack()));
				colorPicker.setColor(PClo_ItemWeaselDisk.getColor(theSlot.getStack()));
				colorBulb.setColor(colorPicker.getColor());
			} else {
				btnEdit.enabled = false;
				btnEdit.setText(PC_Lang.tr("pc.gui.weasel.diskManager.edit"));
				setPanelForStack(false);
				edName.setText("");
				colorBulb.setColor(0);
				colorPicker.setColor(0x000000);
			}
		}

		if (theSlot.getStack() != null) {
			
			if(widget == edTextText) {
				PClo_ItemWeaselDisk.setText(theSlot.getStack(), edTextText.getText());
			}
			
			if(widget == edListText || widget == edSeparator) {
				PClo_ItemWeaselDisk.setListText(theSlot.getStack(), edListText.getText(), edSeparator.getText());
			}
			
			if (widget == colorPickerI) {
				colorBulbI.setColor(colorPickerI.getColor());
				return;
			}
			
			// clicked in image map
			if (widget == colormap) {
				int color = colorPickerI.getColor();
				String evt = colormap.getLastEvent();
				
				if(colormap.getLastMouseKey() == 1) color = -1;
				
				
				if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)||Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					colorPickerI.setColor(colormap.getColorArray()[colormap.getLastMousePos().x][colormap.getLastMousePos().y]);
					colorBulbI.setColor(colorPickerI.getColor());
					return;
				}
				
				if(evt.equals("down")||evt.equals("move")) {
					try {
						colormap.getColorArray()[colormap.getLastMousePos().x][colormap.getLastMousePos().y] = color;
						PClo_ItemWeaselDisk.setImageColorAt(theSlot.getStack(), colormap.getLastMousePos(), color);
					}catch(Exception e) {}
				}
				
				
				return;
			}
			
			// edit text for image size
			if(widget == edImgX || widget == edImgY) {
				try {
					btnImgResize.enabled = Integer.valueOf(edImgX.getText())>0 && Integer.valueOf(edImgY.getText())>0;
					btnImgResize.enabled &= Integer.valueOf(edImgX.getText())<48 && Integer.valueOf(edImgY.getText())<48;
				}catch(NumberFormatException e) {
					btnImgResize.enabled = false;
				}
			}
			
			// clear, fill
			
			
			if (widget == btnImgResize) {				
				int[][] data = PClo_ItemWeaselDisk.getImageData(theSlot.getStack());
				int[][] newdata = new int[Integer.valueOf(edImgX.getText())][Integer.valueOf(edImgY.getText())];
				for(int x=0; x<newdata.length; x++) {
					for(int y=0; y<newdata[0].length; y++) {
						try {
							newdata[x][y] = data[x][y];
						}catch(ArrayIndexOutOfBoundsException e) {
							newdata[x][y] = -1;
						}
					}
				}
				PClo_ItemWeaselDisk.setImageData(theSlot.getStack(), newdata);
				colormap.setScale(1);
				colormap.setColorArray(newdata);				
				w.calcSize();
						
				return;
			}
			
			
			switch (widget.getId()) {
				case 50:			
				case 51:
					int color = colorBulbI.getColor().getHex();
					if(widget.getId() == 50) color = -1;
					
					int[][] newdata = colormap.getColorArray();
					
					for(int x=0; x<newdata.length; x++) {
						for(int y=0; y<newdata[0].length; y++) {
							newdata[x][y] = color;
						}
					}
					
					PClo_ItemWeaselDisk.setImageData(theSlot.getStack(), newdata);
					break;
					
				case 40:
					if(colormap.getScale()>1) colormap.setScale(colormap.getScale()-1);
					w.calcSize();
					break;
				case 41:
					
					if(colormap.getSizeAfterChange(1).x < 130 && colormap.getSizeAfterChange(1).y < 100) {
						colormap.setScale(colormap.getScale()+1);
					}
					
					w.calcSize();
					break;
					
				case 99:
					setPanelForStack(panelShown <= 0);
					break;
				case 100:
					PClo_ItemWeaselDisk.formatDisk(theSlot.getStack(), PClo_ItemWeaselDisk.TEXT);
					btnEdit.enabled = true;
					break;
				case 101:
					PClo_ItemWeaselDisk.formatDisk(theSlot.getStack(), PClo_ItemWeaselDisk.IMAGE);
					btnEdit.enabled = true;
					break;
				case 102:
					PClo_ItemWeaselDisk.formatDisk(theSlot.getStack(), PClo_ItemWeaselDisk.NUMBERLIST);
					btnEdit.enabled = true;
					break;
				case 103:
					PClo_ItemWeaselDisk.formatDisk(theSlot.getStack(), PClo_ItemWeaselDisk.STRINGLIST);
					btnEdit.enabled = true;
					break;
			}
		}

	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {}

}
