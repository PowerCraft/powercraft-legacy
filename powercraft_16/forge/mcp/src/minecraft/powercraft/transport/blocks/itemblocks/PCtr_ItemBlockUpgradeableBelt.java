/**
 * 
 */
package powercraft.transport.blocks.itemblocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.api.PC_Utils;
import powercraft.api.blocks.PC_ItemBlock;
import powercraft.transport.blocks.tileentities.PCtr_TEUpgradeableBelt;

/**
 * @author Aaron
 *
 */
public class PCtr_ItemBlockUpgradeableBelt extends PC_ItemBlock {

	/**
	 * @param id
	 */
	public PCtr_ItemBlockUpgradeableBelt(int id) {
		super(id);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata) {
		if(super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY,
				hitZ, metadata)){
			PCtr_TEUpgradeableBelt te = PC_Utils.getTE(world, x, y, z);
			te.setType(stack.getItemDamage());
			return true;
		}
		return false;
	}
	
}
