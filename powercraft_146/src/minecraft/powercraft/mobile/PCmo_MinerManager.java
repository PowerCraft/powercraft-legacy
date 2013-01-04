package powercraft.mobile;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.machines.PCma_App;
import powercraft.management.PC_ClientUtils;
import powercraft.management.PC_I3DRecipeHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;

public class PCmo_MinerManager implements PC_I3DRecipeHandler, PC_IPacketHandler {
	
	@Override
	public boolean foundStructAt(World world, PC_Struct2<PC_VecI, Integer> structStart) {
		for(int x=0; x<2; x++){
			for(int y=0; y<2; y++){
				for(int z=0; z<2; z++){
					ValueWriting.setBID(world, structStart.a.offset(x, y, z), 0, 0);
				}
			}
		}
		PCmo_EntityMiner miner = new PCmo_EntityMiner(world, structStart.a.x+1, structStart.a.y, structStart.a.z+1);
		miner.rotationYaw = (structStart.b+1)*90;
		world.spawnEntityInWorld(miner);
		return true;
	}

	@Override
	public boolean handleIncomingPacket(EntityPlayer player, Object[] o) {
		Entity e = player.worldObj.getEntityByID((Integer)o[0]);
		e.posX = (Double)o[1];
		e.posY = (Double)o[2];
		e.posZ = (Double)o[3];
		e.motionX = (Double)o[4];
		e.motionY = (Double)o[5];
		e.motionZ = (Double)o[6];
		e.rotationYaw = (Float)o[7];
		e.setLocationAndAngles(e.posX, e.posY, e.posZ, e.rotationYaw, 0);
		return false;
	}

}
