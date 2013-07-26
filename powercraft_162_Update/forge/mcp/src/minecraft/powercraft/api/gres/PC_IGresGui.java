package powercraft.api.gres;


import net.minecraft.client.Minecraft;
import powercraft.api.PC_Vec2I;


public interface PC_IGresGui {

	public Minecraft getMinecraft();


	public PC_Vec2I getSize();


	public void drawMouseItemStack(PC_Vec2I mouse, float timeStamp);

}
