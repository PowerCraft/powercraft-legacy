package powercraft.machines;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.gres.PC_GresImage;
import powercraft.management.gres.PC_GresInventory;
import powercraft.management.gres.PC_GresInventoryPlayer;
import powercraft.management.gres.PC_GresLayoutH;
import powercraft.management.gres.PC_GresLayoutV;
import powercraft.management.gres.PC_GresWidget;
import powercraft.management.gres.PC_GresWindow;
import powercraft.management.gres.PC_IGresClient;
import powercraft.management.gres.PC_IGresGui;

public class PCma_GuiCraftingFurnace extends PCma_ContainerCraftingFurnace implements PC_IGresClient {

	public PCma_GuiCraftingFurnace(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	@Override
	public void keyChange(String key, Object value) {}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(PCma_App.craftinFurnace.getBlockName()+".name");
		PC_GresWidget hg = new PC_GresLayoutH();
		hg.add(new PC_GresInventory(3, 3));
		PC_GresWidget vg = new PC_GresLayoutV();
		vg.add(new PC_GresInventory(2, 2));
		vg.add(new PC_GresImage(ModuleInfo.getGresImgDir() + "widgets.png", 44, 66, 12, 11));
		vg.add(new PC_GresInventory(1, 1));
		hg.add(vg);
		hg.add(new PC_GresInventory(2, 2));
		w.add(hg);
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
	public void onReturnPressed(PC_IGresGui gui) {}

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
