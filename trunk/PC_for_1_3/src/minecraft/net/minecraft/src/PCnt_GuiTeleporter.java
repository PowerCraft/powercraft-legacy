package net.minecraft.src;


import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.PC_GresRadioButton.PC_GresRadioGroup;


/**
 * gui for teleporter
 * 
 * @author MightyPork
 */
public class PCnt_GuiTeleporter extends PC_GresBase {

	private PCnt_TeleporterData td;

	private PC_GresButton ok;
	private PC_GresTextEdit name;
	
	private PC_GresRadioGroup rg;
	
	/**
	 * @param te teleproter TE
	 */
	public PCnt_GuiTeleporter(EntityPlayer player, TileEntity te, Object dimension) {
		this.player = player;
		System.out.println("world:"+te.worldObj.worldInfo.getDimension()+":"+(Integer)dimension);
		td = PCnt_TeleporterManager.getTeleporterDataAt((Integer)dimension, te.xCoord, te.yCoord, te.zCoord);
	}


	@Override
	public void initGui(PC_IGresGui gui) {

		PC_GresWindow w = (new PC_GresWindow("Teleporter"));
		
		PC_GresWidget hg = new PC_GresLayoutH();
		hg.add(new PC_GresLabel("Name:"));
		hg.add(name = new PC_GresTextEdit(td.getName(), 10));
		w.add(hg);
		
		rg = new PC_GresRadioGroup();
		
		hg = new PC_GresLayoutH();
		hg.add(new PC_GresLabel("Target:"));
		PC_GresWidget sa = new PC_GresLayoutV();
		for(String name:PCnt_TeleporterManager.getTargetNames()){
			if(!name.equals(td.getName())){
				PC_GresRadioButton rb = new PC_GresRadioButton(name, rg);
				if(name.equals(td.defaultTarget))
					rb.check(true);
				sa.add(rb);
			}
		}
		hg.add(new PC_GresScrollArea(0, 100, sa, PC_GresScrollArea.VSCROLL));
		w.add(hg);
		w.add(ok = new PC_GresButton(PC_Lang.tr("pc.gui.ok")));
		gui.add(w);
		
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if(widget==name){
			ok.enable(PCnt_TeleporterManager.isNameOk(name.getText())||name.getText().equals(td.getName()));
		}else if(widget==ok){
			onReturnPressed(gui);
		}

	}


	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}


	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		if(ok.isEnabled()){
			PC_GresRadioButton rb = rg.getChecked();
			String target="";
			if(rb!=null)
				target = rb.getText();
			PC_Utils.sendToPacketHandler(player, "TeleporterNetHandler", td.pos.x, td.pos.y, td.pos.z, name.getText(), target, td.dimension);
			gui.close();
		}
	}

	
	
}
