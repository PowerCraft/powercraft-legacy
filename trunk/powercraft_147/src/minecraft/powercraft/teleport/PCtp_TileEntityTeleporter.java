package powercraft.teleport;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.api.PC_Entry;
import powercraft.api.PC_PacketHandler;
import powercraft.api.PC_Struct2;
import powercraft.api.PC_VecI;
import powercraft.api.tileentity.PC_TileEntity;

public class PCtp_TileEntityTeleporter extends PC_TileEntity {

	public int direction=0, defaultTargetDirection=0;
	public List<EntityPlayer> playersForTeleport = new ArrayList<EntityPlayer>();
	public boolean soundEnabled, laserDivert = true;
	public PC_VecI defaultTarget;
	
	@Override
	public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote){
			PCtp_TeleporterData tpData = PCtp_TeleporterManager.getTeleporterData(world.getWorldInfo().getDimension(), getCoord());
			direction = tpData.direction;
			if(world.getWorldInfo().getDimension()!=tpData.defaultTargetDimension){
				defaultTarget = null;
			}else{
				defaultTarget = tpData.defaultTarget;
				if(defaultTarget!=null){
					PCtp_TeleporterData otherTPData= PCtp_TeleporterManager.getTeleporterData(tpData.defaultTargetDimension, defaultTarget);
					if(otherTPData!=null){
						defaultTargetDirection = otherTPData.direction;
					}else{
						defaultTarget = null;
					}
				}
			}
			
			
			PC_PacketHandler.setTileEntity(this, new PC_Entry("direction", direction), new PC_Entry("defaultTarget", defaultTarget), new PC_Entry("defaultTargetDirection", defaultTargetDirection));
		}
	}
	
	@Override
	public void setWorldObj(World par1World) {
		super.setWorldObj(par1World);
		if(!worldObj.isRemote){
			PCtp_TeleporterData tpData = PCtp_TeleporterManager.getTeleporterData(worldObj.getWorldInfo().getDimension(), getCoord());
			if(tpData!=null){
				direction = tpData.direction;
				if(worldObj.getWorldInfo().getDimension()!=tpData.defaultTargetDimension){
					defaultTarget = null;
				}else{
					defaultTarget = tpData.defaultTarget;
					if(defaultTarget!=null){
						PCtp_TeleporterData otherTPData= PCtp_TeleporterManager.getTeleporterData(tpData.defaultTargetDimension, defaultTarget);
						if(otherTPData!=null){
							defaultTargetDirection = otherTPData.direction;
						}else{
							defaultTarget = null;
						}
					}
				}
				PC_PacketHandler.setTileEntity(this, new PC_Entry("direction", direction), new PC_Entry("defaultTarget", defaultTarget), new PC_Entry("defaultTargetDirection", defaultTargetDirection));
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
	public void setData(PC_Struct2<String, Object>[] o) {
		for(PC_Struct2<String, Object>s:o){
			String var = s.a;
			if(var.equals("direction")){
				direction = (Integer)s.b;
			}else if(var.equals("soundEnabled")){
				soundEnabled = (Boolean)s.b;
			}else if(var.equals("laserDivert")){
				laserDivert = (Boolean)s.b;
			}else if(var.equals("defaultTarget")){
				defaultTarget = (PC_VecI)s.b;
			}else if(var.equals("defaultTargetDirection")){
				defaultTargetDirection = (Integer)s.b;
			}
		}
		worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
	}

	@Override
	public PC_Struct2<String, Object>[] getData() {
		return new PC_Struct2[]{
				new PC_Entry("direction", direction),
				new PC_Entry("soundEnabled", soundEnabled),
				new PC_Entry("laserDivert", laserDivert),
				new PC_Entry("defaultTarget", defaultTarget),
				new PC_Entry("defaultTargetDirection", defaultTargetDirection)
		};
	}
	
}
