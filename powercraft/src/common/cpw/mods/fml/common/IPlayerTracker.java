package cpw.mods.fml.common;

import net.minecraft.src.EntityPlayer;

public interface IPlayerTracker
{
    void onPlayerLogin(EntityPlayer player);

    void onPlayerLogout(EntityPlayer player);

    void onPlayerChangedDimension(EntityPlayer player);

    void onPlayerRespawn(EntityPlayer player);
}
