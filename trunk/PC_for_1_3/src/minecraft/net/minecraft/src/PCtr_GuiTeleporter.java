package net.minecraft.src;


import java.util.List;

import net.minecraft.src.PC_GresRadioButton.PC_GresRadioGroup;
import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * gui for teleporter
 * 
 * @author MightyPork
 */
public class PCtr_GuiTeleporter extends PC_GresBase {

	private PCtr_TileEntityTeleporter teleporter;
	private PCtr_TeleporterData td;

	private PC_GresButton ok;
	private PC_GresTextEdit name;
	
	/**
	 * @param te teleproter TE
	 */
	public PCtr_GuiTeleporter(EntityPlayer palyer, TileEntity te) {
		this.player = palyer;
		teleporter = (PCtr_TileEntityTeleporter)te;
		td = teleporter.td;
	}


	@Override
	public void initGui(PC_IGresGui gui) {

		PC_GresWindow w = (new PC_GresWindow("Teleporter"));
		
		PC_GresWidget hg = new PC_GresLayoutH();
		hg.add(new PC_GresLabel("Name:"));
		hg.add(name = new PC_GresTextEdit(td.name, 10));
		w.add(hg);
		
		PC_GresRadioGroup rg = new PC_GresRadioGroup();
		
		hg = new PC_GresLayoutH();
		hg.add(new PC_GresLabel("Target:"));
		PC_GresWidget sa = new PC_GresLayoutV();
		for(PCtr_TeleporterData tdl:PCtr_TeleporterHelper.teleporterData)
			if(!(tdl.name==null||tdl.name.equals("")))
				sa.add(new PC_GresRadioButton(tdl.name, rg));
		hg.add(new PC_GresScrollArea(0, 100, sa, PC_GresScrollArea.VSCROLL));
		w.add(hg);
		w.add(ok = new PC_GresButton("OK"));
		gui.add(w);

	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if(widget==ok){
			td.name = name.getText();
			gui.close();
		}

	}

}
