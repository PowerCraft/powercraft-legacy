package powercraft.teleport;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;

public class PCtp_TileEntityTeleporter extends PC_TileEntity {

	public int direction=0;

	@Override
	public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote){
			direction = PCtp_TeleporterManager.getTeleporterData(worldObj.getWorldInfo().getDimension(), getCoord()).direction;
			PC_PacketHandler.setTileEntity(this, "direction", direction);
		}
	}
	
	@Override
	public void setWorldObj(World par1World) {
		super.setWorldObj(par1World);
		if(!worldObj.isRemote){
			PCtp_TeleporterData td = PCtp_TeleporterManager.getTeleporterData(worldObj.getWorldInfo().getDimension(), getCoord());
			if(td!=null){
				direction = td.direction;
				PC_PacketHandler.setTileEntity(this, "direction", td.direction);
			}
		}
	}

	@Override
	public void setData(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("direction")){
				direction = (Integer)o[p++];
			}
		}
	}

	@Override
	public Object[] getData() {
		return new Object[]{
				"direction", direction
		};
	}
	
}
