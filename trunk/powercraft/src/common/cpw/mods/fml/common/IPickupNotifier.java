package cpw.mods.fml.common;

import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;

public interface IPickupNotifier
{
    void notifyPickup(EntityItem item, EntityPlayer player);
}
