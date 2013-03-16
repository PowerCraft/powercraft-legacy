package powercraft.transport;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.api.gres.PC_GresInventory;
import powercraft.api.gres.PC_GresInventoryPlayer;
import powercraft.api.gres.PC_GresLayoutH;
import powercraft.api.gres.PC_GresWidget;
import powercraft.api.gres.PC_GresWindow;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.gres.PC_IGresGui;
import powercraft.api.tileentity.PC_TileEntity;

public class PCtr_GuiSplitter extends PCtr_ContainerSplitter implements PC_IGresClient {

	public PCtr_GuiSplitter(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	@Override
	public void keyChange(String key, Object value) {}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(PCtr_App.splitter.getUnlocalizedName()+".name");
		int color[] = {0x49C0FF, 0xFF4C7B, 0xFF8849, 0xE8FF42, 0x4CFF7F, 0x5149FF};
		PC_GresLayoutH lh = new PC_GresLayoutH();
		for(int s=0; s<6; s++){
			PC_GresInventory inv = new PC_GresInventory(1, 5);
			for(int i=0; i<5; i++){
				inv.setSlot(0, i, invSlots[i+s*5]);
			}
			inv.setColor(PC_GresWidget.textColorEnabled, color[s]);
			inv.setColor(PC_GresWidget.textColorHover, color[s]);
			inv.setColor(PC_GresWidget.textColorClicked, color[s]);
			lh.add(inv);
		}
		w.add(lh);
		w.add(new PC_GresInventoryPlayer(true));
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		
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
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,
			float par3) {
		return false;
	}

	
	
}
