package powercraft.checkpoints;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_GresCheckBox;
import powercraft.management.PC_GresInventory;
import powercraft.management.PC_GresInventoryPlayer;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_GresWindow;
import powercraft.management.PC_IGresClient;
import powercraft.management.PC_IGresGui;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.Lang;

public class PCcp_GuiCheckpoint extends PCcp_ContainerCheckpoint implements PC_IGresClient {

	private PC_GresCheckBox cb;
	
	public PCcp_GuiCheckpoint(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(Lang.tr("pc.gui.checkpoint.title"));
		w.add(cb = new PC_GresCheckBox(Lang.tr("pc.gui.checkpoint.walkingtiggerd")));
		PC_GresInventory inv = new PC_GresInventory(9, 3);
		int n=9;
		for(int j=0; j<3; j++){
			for(int i=0; i<9; i++){
				inv.setSlot(i, j, lSlot.get(n));
				n++;
			}
		}
		w.add(inv);
		inv = new PC_GresInventory(9, 1);
		for(int i=0; i<9; i++){
			inv.setSlot(i, 0, lSlot.get(i));
		}
		w.add(inv);
		w.add(new PC_GresInventoryPlayer(true));
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==cb){
			tileEntity.setCollideTriggerd(cb.isChecked());
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
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,
			float par3) {
		return false;
	}

	@Override
	public void keyChange(String key, Object value) {}

}
