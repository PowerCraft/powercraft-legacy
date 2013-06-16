package powercraft.checkpoints;

import net.minecraft.src.EntityPlayer;

import org.lwjgl.input.Keyboard;

import powercraft.api.gres.PC_GresCheckBox;
import powercraft.api.gres.PC_GresInventory;
import powercraft.api.gres.PC_GresInventoryPlayer;
import powercraft.api.gres.PC_GresWidget;
import powercraft.api.gres.PC_GresWindow;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.gres.PC_IGresGui;
import powercraft.api.tileentity.PC_TileEntity;

public class PCcp_GuiCheckpoint extends PCcp_ContainerCheckpoint implements PC_IGresClient {

	private PC_GresCheckBox cb;
	
	public PCcp_GuiCheckpoint(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow("pc.gui.checkpoint.title");
		w.add(cb = new PC_GresCheckBox("pc.gui.checkpoint.walkingtiggerd"));
		cb.check(tileEntity.isCollideTriggerd());
		PC_GresInventory inv = new PC_GresInventory(9, 3);
		int n=9;
		for(int j=0; j<3; j++){
			for(int i=0; i<9; i++){
				inv.setSlot(i, j, invSlots[n]);
				n++;
			}
		}
		w.add(inv);
		inv = new PC_GresInventory(9, 1);
		for(int i=0; i<9; i++){
			inv.setSlot(i, 0, invSlots[i]);
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
	public void onKeyPressed(PC_IGresGui gui, char c, int i) {
		if(i==Keyboard.KEY_RETURN || i==Keyboard.KEY_ESCAPE || i==Keyboard.KEY_E){
			gui.close();
		}
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
	public void keyChange(String key, Object value) {
		if(key.equals("collideTriggerd")){
			cb.check((Boolean)value);
		}
	}

}
