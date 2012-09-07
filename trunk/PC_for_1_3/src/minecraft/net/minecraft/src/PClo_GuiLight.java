package net.minecraft.src;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;

public class PClo_GuiLight extends PC_GresBase {
	private PClo_TileEntityLight tel;
	private PC_Color color;
	private boolean isStable;
	private boolean isHuge;
	
	private PC_GresCheckBox checkHuge, checkStable;
	private PC_GresColor colorWidget;
	private PC_GresColorPicker colorPicker;
	private PC_GresButton accept, cancel;
	
	public PClo_GuiLight(EntityPlayer player, TileEntity tel){
		this.tel = (PClo_TileEntityLight) tel;
		this.player = player;
		color = this.tel.getColor();
		if(color==null) color = new PC_Color(PC_Color.getHexColorForName("white"));
		isStable = this.tel.isStable;
		isHuge = this.tel.isHuge;
		
	}
	
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = (PC_GresWindow) new PC_GresWindow("Light");
		PC_GresLayoutV v = (PC_GresLayoutV)new PC_GresLayoutV().setAlignH(PC_GresAlign.STRETCH);
		
		PC_GresLayoutH h = (PC_GresLayoutH)new PC_GresLayoutH().setAlignH(PC_GresAlign.JUSTIFIED);
		h.add(checkHuge = (PC_GresCheckBox) new PC_GresCheckBox(PC_Lang.tr("tile.PCloLight.isHuge")).check(isHuge));
		h.add(checkStable = (PC_GresCheckBox) new PC_GresCheckBox(PC_Lang.tr("tile.PCloLight.isStable")).check(isStable));
		v.add(h);
		
		h = (PC_GresLayoutH)new PC_GresLayoutH().setAlignH(PC_GresAlign.STRETCH);;
		h.add(colorWidget = new PC_GresColor(color));
		h.add(colorPicker = new PC_GresColorPicker(color.getHex(), 100, 20));
		v.add(h);
		
		h = (PC_GresLayoutH)new PC_GresLayoutH().setAlignH(PC_GresAlign.STRETCH);;
		h.add(accept = new PC_GresButton(PC_Lang.tr("pc.gui.ok")));
		h.add(cancel = new PC_GresButton(PC_Lang.tr("pc.gui.cancel")));
		v.add(h);
		w.add(v);
		gui.add(w);

	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		
		if(widget == colorPicker){
			colorWidget.setColor(((PC_GresColorPicker)widget).getColor());
			color.setTo(colorPicker.getColor());
		}else if(widget == checkHuge){
			isHuge = ((PC_GresCheckBox)widget).isChecked();
		}else if(widget == checkStable){
			isStable = ((PC_GresCheckBox)widget).isChecked();
		}else if(widget == accept){
			PC_Utils.setTileEntity(player, tel, "color", color.r, color.g, color.b, "isHuge", isHuge, "isStable", isStable);
			gui.close();
		}else if (widget == cancel) {
			gui.close();
		}
		
	}


	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}


	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		PC_Utils.setTileEntity(player, tel, "color", color.r, color.g, color.b, "isHuge", isHuge, "isStable", isStable);
		gui.close();
	}	

}
