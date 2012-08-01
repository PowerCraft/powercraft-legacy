package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;

import org.lwjgl.input.Keyboard;

import weasel.WeaselEngine;
import weasel.lang.Instruction;
import weasel.lang.InstructionEnd;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;


/**
 * Digital Workbench
 * 
 * @author MightyPork
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
	private PC_GresWidget vg0, vgf, vgt, vgl, vgi, vgd, vgc;
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
	private PC_GresWidget edImgX;
	private PC_GresWidget edImgY;
	private PC_GresWidget btnImgResize;
	private PC_GresWidget formatButtons;
	private PC_GresColorMap colormap;
	private PC_GresWidget panelwrap;
	private PC_GresWidget btnEdit;
	private PC_GresWidget edDataText;
	private PC_GresWidget edDataName;
	private PC_GresWidget edDataValue;
	private PC_GresWidget btnDataOk;
	private PC_GresLayoutH hgLower;
	private PC_GresInventoryPlayer invPlayer;
	private PC_GresWidget edCodeText;
	private PC_GresWidget btCompile;
	private PC_GresWidget txError;
	private PC_GresWidget ckFormat;
	
	private PC_CoordI ldp = null;
	private int lastColor = -1;

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
			
			} else if (type == PClo_ItemWeaselDisk.VARMAP) {
				panel = 4;
			} else if (type == PClo_ItemWeaselDisk.LIBRARY) {
				panel = 5;
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
		panelwrap.setVisible(false);
		vg0.setVisible(false);
		vgd.setVisible(false);
		vgc.setVisible(false);
		hgLower.setVisible(false);
		cpyInv1.setSlot(null, 0, 0);
		cpyInv2.setSlot(null, 0, 0);
		slotInv.setSlot(null);
		cpySlot1.inventory.closeChest();
		cpySlot2.inventory.closeChest();
		invPlayer.hideSlots();
		invPlayer.removeAll();
		panelwrap.setMinHeight(5);
		panelwrap.calcSize();
		w.calcSize();

		formatButtons.setVisible(false);
		formatButtons.enabled = false;
		switch (panel) {
			case -1:
				w.setText(PC_Lang.tr("pc.gui.weasel.diskManager.empty.title"));
				vg0.setVisible(true);
				cpyInv1.setSlot(cpySlot1, 0, 0);
				cpyInv2.setSlot(cpySlot2, 0, 0);
				invPlayer.addedToWidget();
				slotInv.setSlot(theSlot);
				hgLower.setVisible(true);
				break;
			case 0:
				if(theSlot.getStack()!=null) {
				w.setText(theSlot.getStack().getItem().getItemDisplayName(stack));
				}
				vgf.setVisible(true);
				formatButtons.setVisible(true);
				btnEdit.setText(PC_Lang.tr("pc.gui.weasel.diskManager.edit"));
				invPlayer.addedToWidget();
				slotInv.setSlot(theSlot);
				hgLower.setVisible(true);
				break;
			case 1:
				w.setText(PC_Lang.tr("pc.weasel.disk.text"));
				if(theSlot.getStack()!=null) {
				edTextText.setText(PClo_ItemWeaselDisk.getText(stack));
				}
				vgt.setVisible(true);
				panelwrap.setVisible(true);

				break;
			case 2:
				w.setText(PC_Lang.tr("pc.weasel.disk.image"));
				if(theSlot.getStack()!=null) {
					colormap.setColorArray(PClo_ItemWeaselDisk.getImageData(stack));
					edImgX.text = PClo_ItemWeaselDisk.getImageSize(stack).x + "";
					edImgY.text = PClo_ItemWeaselDisk.getImageSize(stack).y + "";
				}
				vgi.setVisible(true);
				panelwrap.setVisible(true);

				break;
			case 3:
				if(theSlot.getStack()!=null) {
				w.setText(theSlot.getStack().getItem().getItemDisplayName(stack));
				edListText.setText(PClo_ItemWeaselDisk.getListText(stack));
				edSeparator.setText(PClo_ItemWeaselDisk.getListDelimiter(stack));
				}
				vgl.setVisible(true);
				panelwrap.setVisible(true);

				break;
				
			case 4:
				if(theSlot.getStack()!=null) {
				w.setText(theSlot.getStack().getItem().getItemDisplayName(stack));
				String str = "";
				for(Entry<String, WeaselObject> entry : PClo_ItemWeaselDisk.getVarMapMap(theSlot.getStack()).get().entrySet()) {
					str += entry.getKey()+" = " + entry.getValue()+";\n";
				}
				edDataText.setText(str);
				}
				vgd.setVisible(true);
				panelwrap.setVisible(true);

				break;
				
			case 5:
				if(theSlot.getStack()!=null) {
					w.setText(theSlot.getStack().getItem().getItemDisplayName(stack));
					String str = PClo_ItemWeaselDisk.getLibrarySource(stack);
					edCodeText.setText(str);
				}
				vgc.setVisible(true);
				panelwrap.setVisible(true);
				txError.setText(PC_Lang.tr("pc.gui.weasel.diskManager.clickCompile"));

				break;
		}
		panelwrap.setMinSize(panelwrap.getMinSize());
		w.calcChildPositions();
	}

	@Override
	public void initGui(PC_IGresGui gui) {

		w = new PC_GresWindow(PC_Lang.tr("pc.gui.weasel.diskManager.title"));
		w.setAlignH(PC_GresAlign.CENTER);
		w.setAlignV(PC_GresAlign.TOP);
		w.setMinWidth(320);

		PC_GresWidget hg, vg, vg1, hg1, hg2, vg2;
		vgf = new PC_GresLayoutV().setMinHeight(100).setAlignV(PC_GresAlign.CENTER).setAlignH(PC_GresAlign.CENTER).setWidgetMargin(0);
		vgf.setAlignH(PC_GresAlign.CENTER);

		hg = new PC_GresLayoutH().setWidgetMargin(2);
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.diskManager.label")));
		hg.add(edName = new PC_GresTextEdit("", 15, PC_GresInputType.IDENTIFIER).setWidgetMargin(2));
		hg.add(btnRename = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.set")).setMinWidth(0));
		vgf.add(hg);
		hg = new PC_GresLayoutH();

		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.diskManager.color")));
		PC_GresWidget frame = new PC_GresFrame().setWidgetMargin(3);
		frame.add(colorBulb = new PC_GresColor(new PC_Color(0, 0, 0)));
		frame.add(colorPicker = new PC_GresColorPicker(0x000000, 60, 30));
		hg.add(frame);
		hg.add(btnRecolor = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.set")).setMinWidth(0));
		vgf.add(hg);
		
		vg = new PC_GresLayoutV().setWidgetMargin(0).setAlignH(PC_GresAlign.LEFT);		
		vg.add(ckFormat = new PC_GresCheckBox(PC_Lang.tr("pc.gui.weasel.diskManager.format")).check(false).setWidgetMargin(1).setAlignH(PC_GresAlign.LEFT));
		formatButtons = vg1 = new PC_GresLayoutH().setWidgetMargin(0).setAlignH(PC_GresAlign.LEFT);
		vg1.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.formatText")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1).setId(100));
		vg1.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.formatImage")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1).setId(101));
		vg1.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.formatIntegerList")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1)
				.setId(102));
		vg1.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.formatStringList")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1).setId(103));
		vg1.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.formatVariableMap")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1).setId(104));
		vg1.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.formatLibrary")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1).setId(105));
		formatButtons.enabled = false;
//		int width = w.getStringWidth(PC_Lang.tr("pc.gui.weasel.diskManager.format"));
//		width = Math.max(width, w.getStringWidth(PC_Lang.tr("pc.gui.weasel.diskManager.formatText")));
//		width = Math.max(width, w.getStringWidth(PC_Lang.tr("pc.gui.weasel.diskManager.formatImage")));
//		width = Math.max(width, w.getStringWidth(PC_Lang.tr("pc.gui.weasel.diskManager.formatIntegerList")));
//		width = Math.max(width, w.getStringWidth(PC_Lang.tr("pc.gui.weasel.diskManager.formatStringList")));
//		width = Math.max(width, w.getStringWidth(PC_Lang.tr("pc.gui.weasel.diskManager.formatVariableMap")));
//		width = Math.max(width, w.getStringWidth(PC_Lang.tr("pc.gui.weasel.diskManager.formatLibrary")));
//		width += 6;
//		b0.minSize.x = b1.minSize.x = b2.minSize.x = b3.minSize.x = b4.minSize.x = b5.minSize.x = width;
//		vg.setMinWidth(width);
		vg.add(vg1);
		vgf.add(vg);
		

		vg0 =  new PC_GresLayoutV().setMinHeight(100).setAlignV(PC_GresAlign.CENTER).setAlignH(PC_GresAlign.CENTER).setWidgetMargin(0);
		vg0.setAlignH(PC_GresAlign.CENTER).setMinWidth(180);
		vg0.add(new PC_GresImage(mod_PCcore.getImgDir() + "graphics.png", 80, 24, 112, 70));

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(3);
		hg.add(cpyInv1 = new PC_GresInventory(1, 1));
		cpyInv1.setSlot(cpySlot1 = new PC_SlotSelective(new TinyInv(getPlayer()), 0, 0, 0), 0, 0);

		hg.add(new PC_GresButtonImage(mod_PCcore.getImgDir() + "gres/widgets.png", new PC_CoordI(57, 12), new PC_CoordI(13, 10)).setButtonPadding(3,
				3).setId(-13));
		hg.add(cpyInv2 = new PC_GresInventory(1, 1));
		cpyInv2.setSlot(cpySlot2 = new PC_SlotSelective(new TinyInv(getPlayer()), 0, 0, 0), 0, 0);
		vg0.add(hg);
		
		
		
		panelwrap = new PC_GresLayoutV().setMinHeight(100).setAlignV(PC_GresAlign.TOP).setAlignH(PC_GresAlign.CENTER).setWidgetMargin(0);

		// Text edit screen
		vgt = new PC_GresLayoutV();
		vgt.setAlignH(PC_GresAlign.STRETCH).setAlignV(PC_GresAlign.TOP);
		hg = new PC_GresLayoutH().setWidgetMargin(2).setAlignH(PC_GresAlign.CENTER).setAlignV(PC_GresAlign.TOP);
		hg.add(new PC_GresButtonImage(mod_PCcore.getImgDir()+"gres/widgets.png",new PC_CoordI(71,11),new PC_CoordI(12, 11)).setButtonPadding(2, 2).setWidgetMargin(3).setId(99));
		hg.add(edTextText = new PC_GresTextEditMultiline("", 220, 160));
		vgt.add(hg);
		
		// pgm
		vgc = new PC_GresLayoutV();
		vgc.setAlignH(PC_GresAlign.STRETCH).setAlignV(PC_GresAlign.TOP);		
		hg = new PC_GresLayoutH().setWidgetMargin(2).setAlignH(PC_GresAlign.CENTER).setAlignV(PC_GresAlign.TOP);
		vg = new PC_GresLayoutV().setAlignV(PC_GresAlign.TOP);
		vg.add(new PC_GresButtonImage(mod_PCcore.getImgDir()+"gres/widgets.png",new PC_CoordI(71,11),new PC_CoordI(12, 11)).setButtonPadding(2, 2).setWidgetMargin(3).setId(99));
		vg.add(btCompile = new PC_GresButtonImage(mod_PCcore.getImgDir()+"gres/widgets.png",new PC_CoordI(84,10),new PC_CoordI(13, 12)).setButtonPadding(2, 2).setWidgetMargin(3));
		hg.add(vg);
		hg.add(edCodeText = new PC_GresTextEditMultiline("", 220, 148, PC_GresHighlightHelper.weasel(null, new WeaselEngine(null)), PC_GresHighlightHelper.autoAdd));
		vgc.add(hg);
		vgc.add(txError = new PC_GresLabelMultiline(PC_Lang.tr("pc.gui.weasel.diskManager.clickCompile"), 220)
		.setMinRows(1).setMaxRows(1).setWidgetMargin(1).setColor(PC_GresWidget.textColorEnabled, 0x000000));
		
		// show datadisk
		vgd = new PC_GresLayoutV();
		vgd.setAlignH(PC_GresAlign.STRETCH).setAlignV(PC_GresAlign.TOP);
		
		hg = new PC_GresLayoutH().setWidgetMargin(2).setAlignH(PC_GresAlign.LEFT).setAlignV(PC_GresAlign.CENTER);
		hg.add(new PC_GresButtonImage(mod_PCcore.getImgDir()+"gres/widgets.png",new PC_CoordI(71,11),new PC_CoordI(12, 11)).setButtonPadding(2, 2).setWidgetMargin(3).setId(99));
		hg.add(edDataName = new PC_GresTextEdit("", 10, PC_GresInputType.IDENTIFIER).setWidgetMargin(2));
		hg.add(new PC_GresLabel("="));
		hg.add(edDataValue = new PC_GresTextEdit("", 16, PC_GresInputType.TEXT).setWidgetMargin(2));
		hg.add(new PC_GresLabel(";"));
		hg.add(btnDataOk = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setButtonPadding(4, 4).setMinWidth(0));		
		hg.add(new PC_GresButtonImage(mod_PCcore.getImgDir()+"gres/widgets.png",new PC_CoordI(71,11),new PC_CoordI(12, 11)).setButtonPadding(2, 2).setWidgetMargin(3).setId(99));
		vgd.add(hg);
		
		hg = new PC_GresLayoutH().setWidgetMargin(2).setAlignH(PC_GresAlign.CENTER).setAlignV(PC_GresAlign.TOP);
		hg.add(edDataText = new PC_GresTextEditMultiline("", 220, 148).enable(false));
		((PC_GresTextEditMultiline) edDataText).setFgColor(0x00ee00);
		((PC_GresTextEditMultiline) edDataText).setBgColor(0x000000);
		vgd.add(hg);

		// List screen
		vgl = new PC_GresLayoutV();
		vgl.setAlignH(PC_GresAlign.STRETCH).setAlignV(PC_GresAlign.TOP);
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresButtonImage(mod_PCcore.getImgDir()+"gres/widgets.png",new PC_CoordI(71,11),new PC_CoordI(12, 11)).setButtonPadding(2, 2).setWidgetMargin(3).setId(99));
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.diskManager.separator")));
		hg.add(edSeparator = new PC_GresTextEdit("", 15, PC_GresInputType.TEXT).setWidgetMargin(2));
		vgl.add(hg);
		vgl.add(edListText = new PC_GresTextEditMultiline("", 220, 130));


		// Image screen
		vgi = new PC_GresLayoutV();
			vgi.setAlignH(PC_GresAlign.CENTER);
			vgi.setAlignV(PC_GresAlign.TOP);
	
			// colors
			//@formatter:off
			vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER);
				vg.setMinWidth(80);
				
				frame = new PC_GresFrame().setWidgetMargin(1).setAlignV(PC_GresAlign.CENTER);
				((PC_GresFrame)frame).framePadding = 3;
					hg1 = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(3);
					hg1.add(new PC_GresButtonImage(mod_PCcore.getImgDir()+"gres/widgets.png",new PC_CoordI(71,11),new PC_CoordI(12, 11)).setButtonPadding(2, 2).setWidgetMargin(1).setId(99));
						hg1.add(colorBulbI = (PC_GresColor) new PC_GresColor(new PC_Color(0, 0, 0)).setWidgetMargin(1));						
						hg1.add(colorPickerI = new PC_GresColorPicker(0x000000, 60, 30)).setWidgetMargin(1);
						colorBulbI.showAsRect = true;
						colorBulbI.setMinSize(8, 30);
						colorBulbI.setSize(8, 30);
						vg2 = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
							hg2 = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT).setWidgetMargin(0);
								hg2.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.img.clear")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1)
										.setId(50));
								hg2.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.img.fill")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1)
										.setId(51));
							vg2.add(hg2);
							hg2 = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT).setWidgetMargin(0);
								hg2.add(new PC_GresButton("-").setButtonPadding(3, 3).setWidgetMargin(1).setId(40).setMinWidth(0));
								hg2.add(new PC_GresButton("+").setButtonPadding(3, 3).setWidgetMargin(1).setId(41).setMinWidth(0));						
							vg2.add(hg2);
						hg1.add(vg2);
					hg1.add(edImgX = new PC_GresTextEdit("0", 4, PC_GresInputType.UNSIGNED_INT).setWidgetMargin(1));
					hg1.add(edImgY = new PC_GresTextEdit("0", 4, PC_GresInputType.UNSIGNED_INT).setWidgetMargin(1));
					hg1.add(btnImgResize = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.resize")).setButtonPadding(4, 4).setWidgetMargin(2).setMinWidth(0));
					frame.add(hg1);
				vg.add(frame);
				
			vgi.add(vg);
			//@formatter:on
	
			// map
			vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER).setMinHeight(135);
			vg.add(colormap = (PC_GresColorMap) new PC_GresColorMap(null).setScale(1).useKeyboard(false).setWidgetMargin(0));
			vgi.add(vg);
		// end if vgi


		vg0.setVisible(false);
		vgf.setVisible(false);
		vgt.setVisible(false);
		vgi.setVisible(false);
		vgl.setVisible(false);
		vgd.setVisible(false);
		vgc.setVisible(false);

		w.add(vg0);
		w.add(vgf);
		panelwrap.add(vgt);
		panelwrap.add(vgi);
		panelwrap.add(vgl);
		panelwrap.add(vgd);
		panelwrap.add(vgc);

		w.add(panelwrap);


		hgLower = new PC_GresLayoutH();
		vg = new PC_GresLayoutV().setWidgetMargin(5);
		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.diskManager.disk")).setWidgetMargin(2));
		vg.add(slotInv = new PC_GresInventoryBigSlot(theSlot = new PC_SlotSelective(new TinyInv(getPlayer()), 0, 0, 0)));
		vg.add(btnEdit = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.diskManager.edit")).setButtonPadding(4, 4).setMinWidth(slotInv.size.x + 10)
				.setWidgetMargin(1).setId(99));

		hgLower.add(vg);
		vg = new PC_GresLayoutV();
		hgLower.add(invPlayer = (PC_GresInventoryPlayer) new PC_GresInventoryPlayer(true).setWidgetMargin(2));
		vg.add(new PC_GresGap(0, 3));
		hgLower.add(vg);
		hgLower.add(new PC_GresGap(32, 2));
		w.add(hgLower);


		gui.add(w);
		//stretch the gui in advance
		setPanel_do(0);
		setPanel_do(1);
		setPanel_do(2);
		setPanel_do(3);
		setPanel_do(4);
		setPanel_do(5);
		setPanel_do(-1);

		btnEdit.enabled = false;

		txError.setFontRenderer(mod_PCcore.fontRendererSmall);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		theSlot.inventory.closeChest();
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		
		formatButtons.enabled = ((PC_GresCheckBox) ckFormat).isChecked();

		if (widget.getId() == -13) {
			if (cpySlot1.getStack() != null && cpySlot2.getStack() != null) {
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

			if (widget == edTextText) {
				PClo_ItemWeaselDisk.setText(theSlot.getStack(), edTextText.getText());
				return;
			}
			
			if (widget == edCodeText) {
				PClo_ItemWeaselDisk.setLibrarySource(theSlot.getStack(), edCodeText.getText());
				return;
			}
			
			if (widget == btCompile) {
				try {
					List<Instruction> list = new ArrayList<Instruction>(20);
					list.add(new InstructionEnd());
					list.addAll(WeaselEngine.compileLibrary(edCodeText.getText()));
					
					System.out.println("\n# Compiled library.");
					for(Instruction i:list) System.out.println(i);
					
					PClo_ItemWeaselDisk.setLibraryInstructions(theSlot.getStack(), list);
					txError.setText(PC_Lang.tr("pc.gui.weasel.diskManager.compiled"));
				}catch(Exception e) {
					txError.setText(e.getMessage());
				}
				return;
			}


			if (widget == edListText || widget == edSeparator) {
				PClo_ItemWeaselDisk.setListText(theSlot.getStack(), edListText.getText(), edSeparator.getText());
				return;
			}
			
			if (widget == edDataName || widget == edDataValue) {
				btnDataOk.enabled = edDataName.text.trim().length()>0;
				return;
			}
			
			if (widget == btnDataOk) {
				String name = edDataName.text.trim();
				String value = edDataValue.text.trim().replace("\"", "");
				Integer i = null;
				
				try {
					i = Integer.valueOf(value);
				}catch(NumberFormatException e) {}
				
				if(value.length() == 0) {
					PClo_ItemWeaselDisk.removeMapVariable(theSlot.getStack(), name);
				}else {
					PClo_ItemWeaselDisk.setMapVariable(theSlot.getStack(), name, i==null?new WeaselString(value):new WeaselInteger(i));
				}
				
				edDataName.text="";
				edDataValue.text="";
				
				this.setPanel_do(4);
				
				return;
			}

			if (widget == colorPickerI) {
				colorBulbI.setColor(colorPickerI.getColor());
				return;
			}

			// clicked in image map
			if (widget == colormap) {
				int color = colorPickerI.getColor();
				String evt = colormap.getLastEvent();
				PC_CoordI pos = colormap.getLastMousePos().copy();

				if (colormap.getLastMouseKey() == 1) {
					color = -1;
				}
				
				


				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					colorPickerI.setColor(colormap.getColorArray()[colormap.getLastMousePos().x][colormap.getLastMousePos().y]);
					colorBulbI.setColor(colorPickerI.getColor());
					return;
				}

				if (evt.equals("down") || evt.equals("move")) {
					lastColor  = color;

					if(Keyboard.isKeyDown(Keyboard.KEY_F)) {
						if(evt.equals("down")) {
							int[][] disk = colormap.getColorArray();
							PC_BitmapUtils.floodFill(disk, pos.x, pos.y, color);
							colormap.setColorArray(disk);						
							ldp = null;
						}
						return;
					}
					if(Keyboard.isKeyDown(Keyboard.KEY_C)) {
						if(evt.equals("down")) {

							ldp = pos.copy();
						}
						return;
					}
					if(Keyboard.isKeyDown(Keyboard.KEY_L)||Keyboard.isKeyDown(Keyboard.KEY_B)||Keyboard.isKeyDown(Keyboard.KEY_R)) {
						if(evt.equals("down")) {
							colormap.getColorArray()[pos.x][pos.y] = color;
							ldp = pos.copy();
						}
						return;
					}
					
					if(ldp != null) {
						if(!ldp.equals(pos)) {
							int[][] disk = colormap.getColorArray();
							PC_BitmapUtils.line(disk, ldp.x, ldp.y, pos.x, pos.y, color);
							colormap.setColorArray(disk);
						}
					}
					ldp = pos.copy();
				}
				
				if (evt.equals("up")) {
					color = lastColor;
					
					int[][] disk = colormap.getColorArray();					
					PClo_ItemWeaselDisk.setImageData(theSlot.getStack(), disk);
					
					if(ldp == null) {
						return;
					}
					
					if(Keyboard.isKeyDown(Keyboard.KEY_C)) {
						PC_BitmapUtils.ellipse(disk, ldp.x, ldp.y, pos.x-ldp.x, pos.y-ldp.y, color);
						PClo_ItemWeaselDisk.setImageData(theSlot.getStack(), disk);
						colormap.setColorArray(disk);
						ldp = null;
						return;
					}
					if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
						PClo_ItemWeaselDisk.setImageData(theSlot.getStack(), disk);
						PC_BitmapUtils.rect(disk, ldp.x, ldp.y, pos.x, pos.y, color);
						colormap.setColorArray(disk);
						ldp = null;
						return;
					}
					if(Keyboard.isKeyDown(Keyboard.KEY_B)) {
						PC_BitmapUtils.frame(disk, ldp.x, ldp.y, pos.x, pos.y, color);
						PClo_ItemWeaselDisk.setImageData(theSlot.getStack(), disk);
						colormap.setColorArray(disk);
						ldp = null;
						return;
					}
					// L
					if(ldp!=null) {
						PC_BitmapUtils.line(disk, ldp.x, ldp.y, pos.x, pos.y, color);						
						PClo_ItemWeaselDisk.setImageData(theSlot.getStack(), disk);
						colormap.setColorArray(disk);
						ldp = null;						
					}
					ldp = null;
				}


				return;
			}

			// edit text for image size
			if (widget == edImgX || widget == edImgY) {
				try {
					btnImgResize.enabled = Integer.valueOf(edImgX.getText()) > 0 && Integer.valueOf(edImgY.getText()) > 0;
					btnImgResize.enabled &= Integer.valueOf(edImgX.getText()) <= 260 && Integer.valueOf(edImgY.getText()) <= 130;
				} catch (NumberFormatException e) {
					btnImgResize.enabled = false;
				}
				return;
			}

			// clear, fill


			if (widget == btnImgResize) {
				int[][] data = PClo_ItemWeaselDisk.getImageData(theSlot.getStack());
				int[][] newdata = new int[Integer.valueOf(edImgX.getText())][Integer.valueOf(edImgY.getText())];
				for (int x = 0; x < newdata.length; x++) {
					for (int y = 0; y < newdata[0].length; y++) {
						try {
							newdata[x][y] = data[x][y];
						} catch (ArrayIndexOutOfBoundsException e) {
							newdata[x][y] = -1;
						}
					}
				}
				PClo_ItemWeaselDisk.setImageData(theSlot.getStack(), newdata);
				colormap.setScale(1);
				colormap.setColorArray(newdata);
				w.calcChildPositions();

				return;
			}


			switch (widget.getId()) {
				case 50:
				case 51:
					int color = colorBulbI.getColor().getHex();
					if (widget.getId() == 50) color = -1;

					int[][] newdata = colormap.getColorArray();

					for (int x = 0; x < newdata.length; x++) {
						for (int y = 0; y < newdata[0].length; y++) {
							newdata[x][y] = color;
						}
					}

					PClo_ItemWeaselDisk.setImageData(theSlot.getStack(), newdata);
					break;

				case 40:
					if (colormap.getScale() > 1) colormap.setScale(colormap.getScale() - 1);
					w.calcSize();
					break;
				case 41:

					if (colormap.getSizeAfterChange(1).x < 260 && colormap.getSizeAfterChange(1).y < 140) {
						colormap.setScale(colormap.getScale() + 1);
					}

					w.calcSize();
					break;

				case 99:
					setPanelForStack(panelShown <= 0);
					break;
				case 100:
					PClo_ItemWeaselDisk.formatDisk(theSlot.getStack(), PClo_ItemWeaselDisk.TEXT);
					btnEdit.enabled = true;
					((PC_GresCheckBox) ckFormat).check(false);
					formatButtons.enabled = false;
					break;
				case 101:
					PClo_ItemWeaselDisk.formatDisk(theSlot.getStack(), PClo_ItemWeaselDisk.IMAGE);
					btnEdit.enabled = true;
					((PC_GresCheckBox) ckFormat).check(false);
					formatButtons.enabled = false;
					break;
				case 102:
					PClo_ItemWeaselDisk.formatDisk(theSlot.getStack(), PClo_ItemWeaselDisk.NUMBERLIST);
					btnEdit.enabled = true;
					((PC_GresCheckBox) ckFormat).check(false);
					formatButtons.enabled = false;
					break;
				case 103:
					PClo_ItemWeaselDisk.formatDisk(theSlot.getStack(), PClo_ItemWeaselDisk.STRINGLIST);
					btnEdit.enabled = true;
					((PC_GresCheckBox) ckFormat).check(false);
					formatButtons.enabled = false;
					break;
				case 104:
					PClo_ItemWeaselDisk.formatDisk(theSlot.getStack(), PClo_ItemWeaselDisk.VARMAP);
					btnEdit.enabled = true;
					((PC_GresCheckBox) ckFormat).check(false);
					formatButtons.enabled = false;
					break;
				case 105:
					PClo_ItemWeaselDisk.formatDisk(theSlot.getStack(), PClo_ItemWeaselDisk.LIBRARY);
					btnEdit.enabled = true;
					((PC_GresCheckBox) ckFormat).check(false);
					formatButtons.enabled = false;
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
