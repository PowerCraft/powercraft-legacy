package powercraft.weasel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import powercraft.management.PC_Color;
import powercraft.management.PC_GresButton;
import powercraft.management.PC_GresButtonImage;
import powercraft.management.PC_GresCheckBox;
import powercraft.management.PC_GresColor;
import powercraft.management.PC_GresColorMap;
import powercraft.management.PC_GresColorPicker;
import powercraft.management.PC_GresFrame;
import powercraft.management.PC_GresGap;
import powercraft.management.PC_GresImage;
import powercraft.management.PC_GresInventory;
import powercraft.management.PC_GresInventoryBigSlot;
import powercraft.management.PC_GresInventoryPlayer;
import powercraft.management.PC_GresLabel;
import powercraft.management.PC_GresLayoutH;
import powercraft.management.PC_GresLayoutV;
import powercraft.management.PC_GresTextEdit;
import powercraft.management.PC_GresTextEdit.PC_GresInputType;
import powercraft.management.PC_GresTextEditMultiline;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_GresWidget.PC_GresAlign;
import powercraft.management.PC_GresWindow;
import powercraft.management.PC_IGresClient;
import powercraft.management.PC_IGresGui;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_VecI;
import powercraft.weasel.PCws_WeaselBitmapUtils.WeaselBitmapProvider;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;

public class PCws_GuiWeaselDiskManager extends PCws_ContainerWeaselDiskManager implements PC_IGresClient {

	private PC_GresWindow win;
	private PC_GresWidget main;
	private PC_GresWidget main_copy;
	private PC_GresWidget main_format;
	private PC_GresInventoryBigSlot editDisk;
	private PC_GresWidget btnEditDisk;
	private PC_GresWidget editDiskLabel;
	private PC_GresWidget btnEditDiskLabel;
	private PC_GresColor diskColor;
	private PC_GresColorPicker diskColorPicker;
	private PC_GresWidget btnDiskColor;
	private PC_GresCheckBox checkFormat;
	
	private PC_GresWidget btnBack;
	
	private PC_GresWidget text_textEdit;
	
	private PC_GresColor image_colorBulbI;					
	private PC_GresColorPicker image_colorPickerI;
	private PC_GresWidget image_edImgX;
	private PC_GresWidget image_edImgY;
	private PC_GresWidget image_btnImgResize;
	private PC_GresColorMap image_colormap;
	private PC_GresWidget image_clear;
	private PC_GresWidget image_fill;
	private PC_GresWidget image_btnMinus;
	private PC_GresWidget image_btnPlus;
	
	private PC_VecI image_ldp = null;
	private int image_lastColor;
	private PixelMap pixelMap;
	
	private PC_GresWidget list_Separator;
	private PC_GresWidget list_ListText;
	
	private PC_GresWidget data_DataName;
	private PC_GresWidget data_DataValue;
	private PC_GresWidget data_btnDataOk;		
	private PC_GresWidget data_btnBack;
	private PC_GresWidget data_DataText;
	
	private HashMap<PC_GresWidget, Integer> btnFormat = new HashMap<PC_GresWidget, Integer>();
	private HashMap<Integer, PC_GresWidget> editor = new HashMap<Integer, PC_GresWidget>();
	
	public PCws_GuiWeaselDiskManager(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	private void makeEditor(PC_GresWindow win){
		main = new PC_GresLayoutV();
		main_copy = new PC_GresLayoutV().setMinHeight(100).setAlignV(PC_GresAlign.CENTER).setAlignH(PC_GresAlign.CENTER).setWidgetMargin(0);
		main_copy.setAlignH(PC_GresAlign.CENTER).setMinWidth(180);
		main_copy.add(new PC_GresImage(ModuleInfo.getGresImgDir() + "graphics.png", 80, 24, 112, 70));

		PC_GresWidget hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(3);
		PC_GresInventory cpyInv1, cpyInv2;
		hg.add(cpyInv1 = new PC_GresInventory(1, 1));
		cpyInv1.setSlot(lSlot.get(1), 0, 0);

		hg.add(new PC_GresButtonImage(ModuleInfo.getGresImgDir()  + "widgets.png", new PC_VecI(57, 12), new PC_VecI(13, 10)).setButtonPadding(3,
				3).setId(-13));
		hg.add(cpyInv2 = new PC_GresInventory(1, 1));
		cpyInv2.setSlot(lSlot.get(2), 0, 0);
		main_copy.add(hg);
		main.add(main_copy);
		
		main_format =  new PC_GresLayoutV();
		
		hg = new PC_GresLayoutH().setWidgetMargin(2);
		hg.add(new PC_GresLabel(Lang.tr("pc.gui.weasel.diskManager.label")));
		hg.add(editDiskLabel = new PC_GresTextEdit("", 15, PC_GresInputType.IDENTIFIER).setWidgetMargin(2));
		hg.add(btnEditDiskLabel = new PC_GresButton(Lang.tr("pc.gui.weasel.diskManager.set")).setMinWidth(0));
		main_format.add(hg);
		
		hg = new PC_GresLayoutH();

		hg.add(new PC_GresLabel(Lang.tr("pc.gui.weasel.diskManager.color")));
		PC_GresWidget frame = new PC_GresFrame().setWidgetMargin(3);
		frame.add(diskColor = new PC_GresColor(new PC_Color(0, 0, 0)));
		frame.add(diskColorPicker = new PC_GresColorPicker(0x000000, 60, 30));
		hg.add(frame);
		hg.add(btnDiskColor = new PC_GresButton(Lang.tr("pc.gui.weasel.diskManager.set")).setMinWidth(0));
		main_format.add(hg);
		
		PC_GresWidget vg = new PC_GresLayoutV().setWidgetMargin(0).setAlignH(PC_GresAlign.LEFT);		
		vg.add((checkFormat = new PC_GresCheckBox(Lang.tr("pc.gui.weasel.diskManager.format"))).check(false).setWidgetMargin(1).setAlignH(PC_GresAlign.LEFT));
		hg = new PC_GresLayoutH().setWidgetMargin(0).setAlignH(PC_GresAlign.LEFT);
		PC_GresWidget btn;
		hg.add(btn = new PC_GresButton(Lang.tr("pc.gui.weasel.diskManager.formatText")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1));
		btnFormat.put(btn, PCws_ItemWeaselDisk.TEXT);
		hg.add(btn = new PC_GresButton(Lang.tr("pc.gui.weasel.diskManager.formatImage")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1));
		btnFormat.put(btn, PCws_ItemWeaselDisk.IMAGE);
		hg.add(btn = new PC_GresButton(Lang.tr("pc.gui.weasel.diskManager.formatIntegerList")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1));
		btnFormat.put(btn, PCws_ItemWeaselDisk.NUMBERLIST);
		hg.add(btn = new PC_GresButton(Lang.tr("pc.gui.weasel.diskManager.formatStringList")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1));
		btnFormat.put(btn, PCws_ItemWeaselDisk.STRINGLIST);
		hg.add(btn = new PC_GresButton(Lang.tr("pc.gui.weasel.diskManager.formatVariableMap")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1));
		btnFormat.put(btn, PCws_ItemWeaselDisk.VARMAP);
		hg.add(btn = new PC_GresButton(Lang.tr("pc.gui.weasel.diskManager.formatLibrary")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1));
		btnFormat.put(btn, PCws_ItemWeaselDisk.LIBRARY);
		vg.add(hg);
		main_format.add(vg);
		main.add(main_format);
		
		hg = new PC_GresLayoutH();
		vg = new PC_GresLayoutV().setWidgetMargin(5);
		vg.add(new PC_GresLabel(Lang.tr("pc.gui.weasel.diskManager.disk")).setWidgetMargin(2));
		PC_GresWidget btnEdit;
		vg.add(editDisk = new PC_GresInventoryBigSlot(lSlot.get(0)));
		vg.add(btnEditDisk = new PC_GresButton(Lang.tr("pc.gui.weasel.diskManager.edit")).setButtonPadding(4, 4).setMinWidth(editDisk.getSize().x + 10)
				.setWidgetMargin(1).setId(99));
		btnEditDisk.enable(false);
		hg.add(vg);
		vg = new PC_GresLayoutV();
		PC_GresInventoryPlayer invPlayer;
		hg.add(invPlayer = (PC_GresInventoryPlayer) new PC_GresInventoryPlayer(true).setWidgetMargin(2));
		vg.add(new PC_GresGap(0, 3));
		hg.add(vg);
		hg.add(new PC_GresGap(32, 2));
		main.add(hg);
		
		win.add(main);
	}
	
	private void makeEditor_Text(PC_GresWindow win){
	
		PC_GresWidget vgt = new PC_GresLayoutV();
		vgt.setAlignH(PC_GresAlign.STRETCH).setAlignV(PC_GresAlign.TOP);
		PC_GresWidget hg = new PC_GresLayoutH().setWidgetMargin(2).setAlignH(PC_GresAlign.CENTER).setAlignV(PC_GresAlign.TOP);
		hg.add(btnBack = new PC_GresButtonImage(ModuleInfo.getGresImgDir()+"widgets.png", new PC_VecI(71,11), new PC_VecI(12, 11)).setButtonPadding(2, 2).setWidgetMargin(3));
		hg.add(text_textEdit = new PC_GresTextEditMultiline("", 220, 190, 0xffffff, 0x000000));
		vgt.add(hg);
	
		win.add(vgt);
		
		editor.put(PCws_ItemWeaselDisk.TEXT, vgt);
		
	}
	
	private void makeEditor_Image(PC_GresWindow win){
		
		PC_GresWidget vgi = new PC_GresLayoutV();
		vgi.setAlignH(PC_GresAlign.CENTER);
		vgi.setAlignV(PC_GresAlign.TOP);

		// colors
		//@formatter:off
		PC_GresWidget vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER);
			vg.setMinWidth(80);
			
			PC_GresWidget frame = new PC_GresFrame().setWidgetMargin(1).setAlignV(PC_GresAlign.CENTER);
			((PC_GresFrame)frame).framePadding = 3;
			PC_GresWidget hg1 = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(3);
				hg1.add(btnBack = new PC_GresButtonImage(ModuleInfo.getGresImgDir()+"widgets.png",new PC_VecI(71,11),new PC_VecI(12, 11)).setButtonPadding(2, 2).setWidgetMargin(1));
					hg1.add(image_colorBulbI = (PC_GresColor) new PC_GresColor(new PC_Color(0, 0, 0)).setWidgetMargin(1));						
					hg1.add(image_colorPickerI = new PC_GresColorPicker(0x000000, 60, 30)).setWidgetMargin(1);
					image_colorBulbI.showAsRect = true;
					image_colorBulbI.setMinSize(8, 30);
					image_colorBulbI.setSize(8, 30);
					PC_GresWidget vg2 = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
					PC_GresWidget hg2 = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT).setWidgetMargin(0);
							hg2.add(image_clear = new PC_GresButton(Lang.tr("pc.gui.weasel.diskManager.img.clear")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1));
							hg2.add(image_fill = new PC_GresButton(Lang.tr("pc.gui.weasel.diskManager.img.fill")).setButtonPadding(3, 3).setMinWidth(0).setWidgetMargin(1));
						vg2.add(hg2);
						hg2 = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT).setWidgetMargin(0);
							hg2.add(image_btnMinus = new PC_GresButton("-").setButtonPadding(3, 3).setWidgetMargin(1).setMinWidth(0));
							hg2.add(image_btnPlus = new PC_GresButton("+").setButtonPadding(3, 3).setWidgetMargin(1).setMinWidth(0));						
						vg2.add(hg2);
					hg1.add(vg2);
				hg1.add(image_edImgX = new PC_GresTextEdit("0", 4, PC_GresInputType.UNSIGNED_INT).setWidgetMargin(1));
				hg1.add(image_edImgY = new PC_GresTextEdit("0", 4, PC_GresInputType.UNSIGNED_INT).setWidgetMargin(1));
				hg1.add(image_btnImgResize = new PC_GresButton(Lang.tr("pc.gui.weasel.diskManager.resize")).setButtonPadding(4, 4).setWidgetMargin(2).setMinWidth(0));
				frame.add(hg1);
			vg.add(frame);
			
		vgi.add(vg);
		//@formatter:on

		// map
		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER).setMinHeight(135);
		vg.add(image_colormap = (PC_GresColorMap) new PC_GresColorMap(null).setScale(1).useKeyboard(false).setWidgetMargin(0));
		vgi.add(vg);
		win.add(vgi);
		editor.put(PCws_ItemWeaselDisk.IMAGE, vgi);
		
		pixelMap = new PixelMap(image_colormap);
		
	}
	
	private void makeEditor_List(PC_GresWindow win){
		
		PC_GresLayoutV vgl = new PC_GresLayoutV();
		vgl.setAlignH(PC_GresAlign.STRETCH).setAlignV(PC_GresAlign.TOP);
		PC_GresWidget hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(btnBack = new PC_GresButtonImage(ModuleInfo.getGresImgDir()+"widgets.png",new PC_VecI(71,11),new PC_VecI(12, 11)).setButtonPadding(2, 2).setWidgetMargin(3));
		hg.add(new PC_GresLabel(Lang.tr("pc.gui.weasel.diskManager.separator")));
		hg.add(list_Separator = new PC_GresTextEdit("", 15, PC_GresInputType.TEXT).setWidgetMargin(2));
		vgl.add(hg);
		vgl.add(list_ListText = new PC_GresTextEditMultiline("", 220, 160, 0xffffff, 0x000000));
	
		win.add(vgl);
		
		editor.put(PCws_ItemWeaselDisk.NUMBERLIST, vgl);
		editor.put(PCws_ItemWeaselDisk.STRINGLIST, vgl);
		
	}
	
	private void makeEditor_VarMap(PC_GresWindow win){
		
		PC_GresWidget vgd = new PC_GresLayoutV();
		vgd.setAlignH(PC_GresAlign.STRETCH).setAlignV(PC_GresAlign.TOP);
		
		PC_GresWidget hg = new PC_GresLayoutH().setWidgetMargin(2).setAlignH(PC_GresAlign.LEFT).setAlignV(PC_GresAlign.CENTER);
		hg.add(btnBack = new PC_GresButtonImage(ModuleInfo.getGresImgDir()+"widgets.png",new PC_VecI(71,11),new PC_VecI(12, 11)).setButtonPadding(2, 2).setWidgetMargin(3));
		hg.add(data_DataName = new PC_GresTextEdit("", 10, PC_GresInputType.IDENTIFIER).setWidgetMargin(2));
		hg.add(new PC_GresLabel("="));
		hg.add(data_DataValue = new PC_GresTextEdit("", 16, PC_GresInputType.TEXT).setWidgetMargin(2));
		hg.add(new PC_GresLabel(";"));
		hg.add(data_btnDataOk = new PC_GresButton(Lang.tr("pc.gui.ok")).setButtonPadding(4, 4).setMinWidth(0));		
		hg.add(data_btnBack = new PC_GresButtonImage(ModuleInfo.getGresImgDir()+"widgets.png",new PC_VecI(71,11),new PC_VecI(12, 11)).setButtonPadding(2, 2).setWidgetMargin(3));
		vgd.add(hg);
		
		hg = new PC_GresLayoutH().setWidgetMargin(2).setAlignH(PC_GresAlign.CENTER).setAlignV(PC_GresAlign.TOP);
		hg.add(data_DataText = new PC_GresTextEditMultiline("", 220, 148, 0x00ee00, 0x000000).enable(false));
		vgd.add(hg);
	
		win.add(vgd);
		
		editor.put(PCws_ItemWeaselDisk.VARMAP, vgd);
		
	}
	
	private void openMainPage(boolean copy){
		for(PC_GresWidget e:editor.values()){
			e.setVisible(false);
		}
		main_copy.setVisible(copy);
		main_format.setVisible(!copy);
		main.setSize(main.getSize().x, 20);
		main.setVisible(true);
		win.calcChildPositions();
	}
	
	private void openEditor(int edit){
		main.setVisible(false);
		for(PC_GresWidget e:editor.values()){
			e.setVisible(false);
		}
		PC_GresWidget w = editor.get(edit);
		if(w!=null){
			w.setVisible(true);
			win.calcChildPositions();
		}else{
			openMainPage(false);
		}
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		win = new PC_GresWindow(Lang.tr("pc.gui.weasel.diskManager.title"));
		win.setAlignV(PC_GresAlign.BOTTOM);
		makeEditor(win);
		makeEditor_Text(win);
		makeEditor_Image(win);
		makeEditor_List(win);
		gui.add(win);
		openMainPage(false);
		main.setSize(main.getSize().x, 20);
		win.setSize(win.getSize().x, 20);
		win.calcSize();
		win.calcChildPositions();
		openMainPage(true);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
	}

	private ItemStack getEditItem(){
		return editDisk.getSlot().getStack();
	}
	
	private void openEditor(ItemStack itemstack, int type){
		openEditor(type);
		if(type==PCws_ItemWeaselDisk.TEXT){
			text_textEdit.setText(PCws_ItemWeaselDisk.getText(itemstack));
		}else if(type==PCws_ItemWeaselDisk.IMAGE){
			PC_VecI size = PCws_ItemWeaselDisk.getImageSize(itemstack);
			image_edImgX.setText(""+size.x);
			image_edImgY.setText(""+size.y);
			pixelMap.resize(size.x, size.y);
			int[][] array = PCws_ItemWeaselDisk.getImageData(itemstack);
			for(int i=0; i<array.length; i++){
				for(int j=0; j<array[i].length; j++){
					pixelMap.setBitmapPixel(i, j, array[i][j]);
				}
			}
			image_btnImgResize.enable(size.x>0 && size.x<=260 && size.y>0 && size.y<=130);
		}else if(type==PCws_ItemWeaselDisk.STRINGLIST || type==PCws_ItemWeaselDisk.NUMBERLIST){
			list_Separator.setText(PCws_ItemWeaselDisk.getListDelimiter(itemstack));
			list_ListText.setText(PCws_ItemWeaselDisk.getListText(itemstack));
		}else if(type==PCws_ItemWeaselDisk.VARMAP){
			String str = "";
			for(Entry<String, WeaselObject> entry : PCws_ItemWeaselDisk.getVarMapMap(itemstack).get().entrySet()) {
				str += entry.getKey()+" = " + entry.getValue()+";\n";
			}
			data_DataText.setText(str);
			data_btnDataOk.enable(false);
			data_DataName.setText("");
			data_DataValue.setText("");
		}
	}
	
	private void closeEditor(ItemStack itemstack, int type){
		if(type==PCws_ItemWeaselDisk.TEXT){
			PCws_ItemWeaselDisk.setText(itemstack, text_textEdit.getText());
			PC_PacketHandler.sendToPacketHandler(thePlayer.worldObj, "WeaselDiskDrive", "setText", text_textEdit.getText());
		}else if(type==PCws_ItemWeaselDisk.IMAGE){
			int[][] array = new int[pixelMap.getBitmapSize().x][pixelMap.getBitmapSize().y];
			List<Integer> list = new ArrayList<Integer>();
			list.add(0);
			int n=0;
			int num=0;
			for(int i=0; i<array.length; i++){
				for(int j=0; j<array[i].length; j++){
					array[i][j] = pixelMap.getBitmapPixel(i, j);
					list.add(array[i][j]);
					if(n>500){
						n = 0;
						PC_PacketHandler.sendToPacketHandler(thePlayer.worldObj, "WeaselDiskDrive", "setImageData", list.toArray(new Integer[0]));
						list.clear();
						list.add(num);
					}
					n++;
					num++;
				}
			}
			PCws_ItemWeaselDisk.setImageData(itemstack, array);
			PC_PacketHandler.sendToPacketHandler(thePlayer.worldObj, "WeaselDiskDrive", "setImageData", list.toArray(new Integer[0]));
		}else if(type==PCws_ItemWeaselDisk.STRINGLIST || type==PCws_ItemWeaselDisk.NUMBERLIST){
			PCws_ItemWeaselDisk.setListText(itemstack, list_ListText.getText(), list_Separator.getText());
			PC_PacketHandler.sendToPacketHandler(thePlayer.worldObj, "WeaselDiskDrive", "setListText", list_ListText.getText(), list_Separator.getText());
		}
	}
	
	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		ItemStack itemstack = getEditItem();
		if(widget==editDisk){
			if(itemstack!=null){
				openMainPage(false);
				boolean isEmpty = PCws_ItemWeaselDisk.getType(itemstack)==PCws_ItemWeaselDisk.EMPTY;
				btnEditDisk.enable(!isEmpty);
				editDiskLabel.setText(PCws_ItemWeaselDisk.getLabel(itemstack));
				btnEditDiskLabel.enable(true);
				diskColor.setColor(PCws_ItemWeaselDisk.getColor(itemstack));
				diskColorPicker.setColor(PCws_ItemWeaselDisk.getColor(itemstack));
				checkFormat.check(isEmpty);
				for(PC_GresWidget w:btnFormat.keySet()){
					w.enable(isEmpty);
				}
			}else{
				openMainPage(true);
				btnEditDisk.enable(false);
			}
		}else if(widget==editDiskLabel){
			btnEditDiskLabel.enable(!editDiskLabel.getText().equals(""));
		}else if(widget==btnEditDiskLabel){
			if(itemstack!=null){
				PCws_ItemWeaselDisk.setLabel(itemstack, editDiskLabel.getText());
				PC_PacketHandler.sendToPacketHandler(thePlayer.worldObj, "WeaselDiskDrive", "setLabel", editDiskLabel.getText());
			}
		}else if(widget==diskColorPicker){
			diskColor.setColor(diskColorPicker.getColor());
		}else if(widget==btnDiskColor){
			if(itemstack!=null){
				PCws_ItemWeaselDisk.setColor(itemstack, diskColorPicker.getColor());
				PC_PacketHandler.sendToPacketHandler(thePlayer.worldObj, "WeaselDiskDrive", "setColor", diskColorPicker.getColor());
			}
		}else if(widget==checkFormat){
			boolean enable = checkFormat.isChecked();
			for(PC_GresWidget w:btnFormat.keySet()){
				w.enable(enable);
			}
		}else if(btnFormat.containsKey(widget)){
			if(itemstack!=null){
				PCws_ItemWeaselDisk.setType(itemstack, btnFormat.get(widget));
				PC_PacketHandler.sendToPacketHandler(thePlayer.worldObj, "WeaselDiskDrive", "setType", btnFormat.get(widget));
				checkFormat.check(false);
				for(PC_GresWidget w:btnFormat.keySet()){
					w.enable(false);
				}
				btnEditDisk.enable(true);
			}
		}else if(widget==btnEditDisk){
			if(itemstack!=null){
				openEditor(itemstack, PCws_ItemWeaselDisk.getType(itemstack));
			}
		}else if(widget==btnBack){
			if(itemstack!=null){
				closeEditor(itemstack, PCws_ItemWeaselDisk.getType(itemstack));
			}
			openMainPage(false);
		}else if(widget==image_colorPickerI){
			image_colorBulbI.setColor(image_colorPickerI.getColor());
		}else if(widget==image_edImgX||widget==image_edImgY){
			boolean enable = false;
			try {
				enable = Integer.valueOf(image_edImgX.getText()) > 0 && Integer.valueOf(image_edImgY.getText()) > 0;
				enable &= Integer.valueOf(image_edImgX.getText()) <= 260 && Integer.valueOf(image_edImgY.getText()) <= 130;
			} catch (NumberFormatException e) {}
			image_btnImgResize.enable(enable);
		}else if(widget==image_btnImgResize){
			try {
				PC_VecI size = new PC_VecI(Integer.parseInt(image_edImgX.getText()), Integer.parseInt(image_edImgY.getText()));
				PCws_ItemWeaselDisk.setImageSize(itemstack, size);
				PC_PacketHandler.sendToPacketHandler(thePlayer.worldObj, "WeaselDiskDrive", "setImageSize", size);
				pixelMap.resize(size.x, size.y);
			} catch (NumberFormatException e) {}
			while(image_colormap.getSizeAfterChange(1).x >= 260 && image_colormap.getSizeAfterChange(1).y >= 140 && image_colormap.getScale()>1)
				image_colormap.setScale(image_colormap.getScale() - 1);
			win.calcSize();
		}else if(widget==image_colormap){
			int color = image_colorPickerI.getColor();
			String evt = image_colormap.getLastEvent();
			PC_VecI pos = image_colormap.getLastMousePos().copy();

			if (image_colormap.getLastMouseKey() == 1) {
				color = -1;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				image_colorPickerI.setColor(image_colormap.getColorArray()[image_colormap.getLastMousePos().x][image_colormap.getLastMousePos().y]);
				image_colorBulbI.setColor(image_colorPickerI.getColor());
				return;
			}

			if (evt.equals("down") || evt.equals("move")) {
				image_lastColor  = color;

				if(Keyboard.isKeyDown(Keyboard.KEY_F)) {
					if(evt.equals("down")) {
						PCws_WeaselBitmapUtils.floodFill(pixelMap, pos.x, pos.y, color);				
						image_ldp = null;
					}
					return;
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_C)) {
					if(evt.equals("down")) {

						image_ldp = pos.copy();
					}
					return;
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_L)||Keyboard.isKeyDown(Keyboard.KEY_B)||Keyboard.isKeyDown(Keyboard.KEY_R)) {
					if(evt.equals("down")) {
						image_colormap.getColorArray()[pos.x][pos.y] = color;
						image_ldp = pos.copy();
					}
					return;
				}
				
				if(image_ldp != null) {
					if(!image_ldp.equals(pos)) {
						PCws_WeaselBitmapUtils.line(pixelMap, image_ldp.x, image_ldp.y, pos.x, pos.y, color);
					}
				}
				image_ldp = pos.copy();
			}
			
			if (evt.equals("up")) {
				color = image_lastColor;
				
				if(image_ldp == null) {
					return;
				}
				
				if(Keyboard.isKeyDown(Keyboard.KEY_C)) {
					PCws_WeaselBitmapUtils.ellipse(pixelMap, image_ldp.x, image_ldp.y, pos.x-image_ldp.x, pos.y-image_ldp.y, color);
					image_ldp = null;
					return;
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
					PCws_WeaselBitmapUtils.rect(pixelMap, image_ldp.x, image_ldp.y, pos.x, pos.y, color);
					image_ldp = null;
					return;
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_B)) {
					PCws_WeaselBitmapUtils.frame(pixelMap, image_ldp.x, image_ldp.y, pos.x, pos.y, color);
					image_ldp = null;
					return;
				}
				// L
				if(image_ldp!=null) {
					PCws_WeaselBitmapUtils.line(pixelMap, image_ldp.x, image_ldp.y, pos.x, pos.y, color);						
					image_ldp = null;						
				}
				image_ldp = null;
			}
		}else if(widget==image_clear||widget==image_fill){
			int color = image_colorBulbI.getColor().getHex();
			if (widget==image_clear) color = -1;
			PCws_WeaselBitmapUtils.rect(pixelMap, 0, 0, pixelMap.getBitmapSize().x-1, pixelMap.getBitmapSize().y-1, color);
		}else if(widget==image_btnMinus){
			if (image_colormap.getScale() > 1) 
				image_colormap.setScale(image_colormap.getScale() - 1);
			win.calcSize();
		}else if(widget==image_btnPlus){
			if (image_colormap.getSizeAfterChange(1).x < 260 && image_colormap.getSizeAfterChange(1).y < 140) {
				image_colormap.setScale(image_colormap.getScale() + 1);
			}
			win.calcSize();
		}else if (widget == list_ListText || widget == list_Separator) {
			if(itemstack!=null){
				PCws_ItemWeaselDisk.setListText(itemstack, list_ListText.getText(), list_Separator.getText());
				PC_PacketHandler.sendToPacketHandler(thePlayer.worldObj, "WeaselDiskDrive", "setListText", list_ListText.getText(), list_Separator.getText());
			}
		}else if(widget==data_DataName){
			data_btnDataOk.enable(data_DataName.getText().trim().length()>0);
		}else if(widget==data_btnDataOk){
			String name = data_DataName.getText().trim();
			String value = data_DataValue.getText().trim().replace("\"", "");
			Integer i = null;
			
			try {
				i = Integer.valueOf(value);
			}catch(NumberFormatException e) {}
			
			if(value.length() == 0) {
				PCws_ItemWeaselDisk.removeMapVariable(itemstack, name);
				PC_PacketHandler.sendToPacketHandler(thePlayer.worldObj, "WeaselDiskDrive", "removeMapVariable", name);
			}else {
				PCws_ItemWeaselDisk.setMapVariable(itemstack, name, i==null?new WeaselString(value):new WeaselInteger(i));
				PC_PacketHandler.sendToPacketHandler(thePlayer.worldObj, "WeaselDiskDrive", "setMapVariable", name, i==null?value:i);
			}
			
			data_DataName.setText("");
			data_DataValue.setText("");
			
			String str = "";
			for(Entry<String, WeaselObject> entry : PCws_ItemWeaselDisk.getVarMapMap(itemstack).get().entrySet()) {
				str += entry.getKey()+" = " + entry.getValue()+";\n";
			}
			data_DataText.setText(str);
			
			return;
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

	@Override
	public void updateTick(PC_IGresGui gui) {
	}

	@Override
	public void updateScreen(PC_IGresGui gui) {
	}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2, float par3) {
		return false;
	}

	private static class PixelMap implements WeaselBitmapProvider{

		private int[][] pic;
		private PC_GresColorMap colorMap;
		
		public PixelMap(PC_GresColorMap colorMap){
			this.colorMap = colorMap;
			pic = new int[0][0];
		}
		
		@Override
		public PC_VecI getBitmapSize() {
			return new PC_VecI(pic.length, pic.length==0?0:pic[0].length);
		}

		@Override
		public int getBitmapPixel(int x, int y) {
			return pic[x][y];
		}

		@Override
		public void setBitmapPixel(int x, int y, int color) {
			pic[x][y] = color;
		}

		@Override
		public void resize(int w, int h) {
			int newArray[][] = new int[w][h];
			for(int i=0; i<w; i++){
				for(int j=0; j<h; j++){
					newArray[i][j] = -1;
				}
			}
			for(int i=0; i<pic.length && i<w; i++){
				for(int j=0; j<pic[i].length && j<h; j++){
					newArray[i][j] = pic[i][j];
				}
			}
			pic = newArray;
			if(pic.length==0){
				colorMap.setColorArray(null);
			}else{
				colorMap.setColorArray(pic);
			}
		}

		@Override
		public void notifyChanges() {
			
		}

		@Override
		public WeaselBitmapProvider getImageForName(String name) {
			return null;
		}
		
	}
	
}
