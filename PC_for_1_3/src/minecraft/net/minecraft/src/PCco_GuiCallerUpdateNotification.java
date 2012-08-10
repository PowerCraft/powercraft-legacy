package net.minecraft.src;

public class PCco_GuiCallerUpdateNotification implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 4;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		return new PCco_GuiUpdateNotification();
	}

}
