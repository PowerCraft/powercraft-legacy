package powercraft.machines;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_GresCheckBox;
import powercraft.management.PC_GresInventory;
import powercraft.management.PC_GresInventoryPlayer;
import powercraft.management.PC_GresLayoutH;
import powercraft.management.PC_GresLayoutV;
import powercraft.management.PC_GresProgressBar;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_GresWidget.PC_GresAlign;
import powercraft.management.PC_GresWindow;
import powercraft.management.PC_IGresClient;
import powercraft.management.PC_IGresGui;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Utils.Lang;

public class PCma_GuiTransmutabox extends PCma_ContainerTransmutabox implements
		PC_IGresClient {
	
	private PC_GresProgressBar progress;
	private PC_GresCheckBox timeCritical;
	
	public PCma_GuiTransmutabox(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWidget w = new PC_GresWindow(Lang.tr(PCma_App.transmutabox.getBlockName()+".name"));
		
		PC_GresWidget hl = new PC_GresLayoutH();
		hl.setAlignH(PC_GresAlign.JUSTIFIED);
		int id=0;
		hl.add(new PC_GresInventory(lSlot.get(id++)));
		PC_GresInventory inv = new PC_GresInventory(8, 1);
		for(int x=0; x<8; x++){
			inv.setSlot(x, 0, lSlot.get(id++));
		}
		hl.add(inv);
		w.add(hl);
		hl = new PC_GresLayoutH();
		hl.setAlignH(PC_GresAlign.JUSTIFIED);
		int inID = id++;
		int outID = id++;
		inv = new PC_GresInventory(3, 4);
		for(int y=0; y<4; y++){
			for(int x=0; x<3; x++){
				inv.setSlot(x, y, lSlot.get(id++));
			}
		}
		hl.add(inv);
		
		PC_GresLayoutV vl = new PC_GresLayoutV();
		PC_GresLayoutH hl1 = new PC_GresLayoutH();
		
		vl.add(timeCritical = new PC_GresCheckBox(Lang.tr("pc.gui.transmutabox.timeCritical")));
		
		vl.add(progress = new PC_GresProgressBar(0xff0000, 100));
		
		hl1.add(new PC_GresInventory(lSlot.get(inID)));
		hl1.add(new PC_GresInventory(lSlot.get(outID)));
		
		vl.add(hl1);
		
		hl.add(vl);
		
		inv = new PC_GresInventory(3, 4);
		for(int y=0; y<4; y++){
			for(int x=0; x<3; x++){
				inv.setSlot(x, y, lSlot.get(id++));
			}
		}
		hl.add(inv);
		w.add(hl);
		w.add(new PC_GresInventoryPlayer(true));
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==timeCritical){
			tileEntity.setTimeCritical(timeCritical.isChecked());
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
		progress.setFraction(tileEntity.getProgress());
	}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,
			float par3) {
		return false;
	}

	@Override
	public void keyChange(String key, Object value) {
		// TODO Auto-generated method stub
		
	}

}
