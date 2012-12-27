package powercraft.teleport;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;

public class PCtp_TileEntityTeleporter extends PC_TileEntity {

	public int direction=0;
	public List<EntityPlayer> playersForTeleport = new ArrayList<EntityPlayer>();
	public boolean soundEnabled;
	
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
	public void updateEntity() {
        
		List<EntityPlayer> toRemove = new ArrayList<EntityPlayer>();
		
        for(EntityPlayer player:playersForTeleport){

            if(player==null){
            	toRemove.add(player);
            }else{
	            if (player.posX < xCoord - 1F || player.posY < yCoord - 1F || player.posZ < zCoord - 1F || player.posX > xCoord + 2F
	                    || player.posY > yCoord + 3F || player.posZ > zCoord + 2F)
	            {
	            	toRemove.add(player);
	            }
            }
        }
        
        for(EntityPlayer player:toRemove){
        	playersForTeleport.remove(player);
        }
        
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void setData(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("direction")){
				direction = (Integer)o[p++];
			}else if(var.equals("soundEnabled")){
				soundEnabled = (Boolean)o[p++];
			}
		}
		worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
	}

	@Override
	public Object[] getData() {
		return new Object[]{
				"direction", direction,
				"soundEnabled", soundEnabled
		};
	}
	
}
