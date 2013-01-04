package powercraft.mobile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import powercraft.core.PCco_App;
import powercraft.management.PC_GresGap;
import powercraft.management.PC_GresInventory;
import powercraft.management.PC_GresInventoryBigSlot;
import powercraft.management.PC_GresInventoryPlayer;
import powercraft.management.PC_GresLayoutH;
import powercraft.management.PC_GresLayoutV;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_GresWidget.PC_GresAlign;
import powercraft.management.PC_IGresClient;
import powercraft.management.PC_IGresGui;
import powercraft.management.PC_SlotSelective;

public class PCmo_GuiMiner extends PCmo_ContainerMiner implements PC_IGresClient {

	private PC_GresWidget vgProgram, vgSettings, vgCargo, vgTerm;
	private PC_GresInventory cargoInv;
	private PC_GresInventoryPlayer playerInv;
	private PC_GresInventoryBigSlot[] xtalInv = new PC_GresInventoryBigSlot[8];
	
	public PCmo_GuiMiner(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		
		vgCargo = new PC_GresLayoutH().setWidgetMargin(0).setAlignV(PC_GresAlign.TOP);
		vgCargo.setAlignH(PC_GresAlign.CENTER);
		
		PC_GresWidget vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(6);

		vg.add(xtalInv[0] = new PC_GresInventoryBigSlot(((PC_SlotSelective)lSlot.get(0)).
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(0)))));
		vg.add(xtalInv[1] = new PC_GresInventoryBigSlot(((PC_SlotSelective)lSlot.get(1)).
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(1)))));
		vg.add(xtalInv[2] = new PC_GresInventoryBigSlot(((PC_SlotSelective)lSlot.get(2)).
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(2)))));
		vg.add(xtalInv[3] = new PC_GresInventoryBigSlot(((PC_SlotSelective)lSlot.get(3)).
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(3)))));

		vgCargo.add(vg);
		

		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(1);

		vg.add(cargoInv = new PC_GresInventory(11, 5));
		int n=8;
		for(int j=0; j<5; j++){
			for(int i=0; i<11; i++){
				cargoInv.setSlot(lSlot.get(n), i, j);
				n++;
			}
		}
		vg.add(playerInv = new PC_GresInventoryPlayer(true));

		vgCargo.add(vg);
		
		vgCargo.add(new PC_GresGap(2, 0));
		
		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(6);

		vg.add(xtalInv[7] = new PC_GresInventoryBigSlot(((PC_SlotSelective)lSlot.get(7)).
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(7)))));
		vg.add(xtalInv[6] = new PC_GresInventoryBigSlot(((PC_SlotSelective)lSlot.get(6)).
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(6)))));
		vg.add(xtalInv[5] = new PC_GresInventoryBigSlot(((PC_SlotSelective)lSlot.get(5)).
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(5)))));
		vg.add(xtalInv[4] = new PC_GresInventoryBigSlot(((PC_SlotSelective)lSlot.get(4)).
				setBackgroundStack(new ItemStack(PCco_App.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(4)))));

		vgCargo.add(vg);
		
		gui.add(vgCargo);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTick(PC_IGresGui gui) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateScreen(PC_IGresGui gui) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,
			float par3) {
		// TODO Auto-generated method stub
		return false;
	}

}
