package cpw.mods.fml.common.network;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

public interface IGuiHandler
{
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z);

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z);
}
