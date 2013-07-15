package powercraft.api.gres;

import powercraft.api.PC_Vec2I;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;

public interface PC_IGresGui {
	
	public Minecraft getMinecraft();

	public PC_Vec2I getSize();

	public void drawMouseItemStack(PC_Vec2I mouse, float timeStamp);
	
}
