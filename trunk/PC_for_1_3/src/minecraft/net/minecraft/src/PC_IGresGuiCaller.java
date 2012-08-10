package net.minecraft.src;

public interface PC_IGresGuiCaller {

	int getGuiID();
	
	PC_IGresBase createGui(EntityPlayer player, int var2, int var3, int var4);
	
}
