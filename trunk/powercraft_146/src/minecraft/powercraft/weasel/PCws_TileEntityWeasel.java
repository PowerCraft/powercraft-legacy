package powercraft.weasel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_TileEntity;

public class PCws_TileEntityWeasel extends PC_TileEntity {

	private int pluginID;

	@Override
	public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		pluginID = PCws_WeaselManager.createPlugin(stack.getItemDamage()).getID();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTag) {
		super.readFromNBT(nbtTag);
		pluginID = nbtTag.getInteger("pluginID");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTag) {
		super.writeToNBT(nbtTag);
		nbtTag.setInteger("pluginID", pluginID);
	}
	
	public PCws_WeaselPlugin getPlugin(){
		return PCws_WeaselManager.getPlugin(pluginID);
	}
	
}
