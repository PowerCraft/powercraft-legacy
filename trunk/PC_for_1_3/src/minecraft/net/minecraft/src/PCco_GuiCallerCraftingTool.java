package net.minecraft.src;

public class PCco_GuiCallerCraftingTool implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 1;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2,
			int var3, int var4) {
		return new PCco_GuiCraftingTool(player);
	}

}
