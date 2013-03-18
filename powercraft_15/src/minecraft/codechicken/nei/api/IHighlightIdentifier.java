package codechicken.nei.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public interface IHighlightIdentifier
{
	public ItemStack identifyHighlight(World world, EntityPlayer player, int x, int y, int z, MovingObjectPosition mop);
}
