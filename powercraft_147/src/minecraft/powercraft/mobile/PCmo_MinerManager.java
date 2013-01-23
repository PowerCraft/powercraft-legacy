package powercraft.mobile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import powercraft.machines.PCma_App;
import powercraft.management.PC_ClientUtils;
import powercraft.management.PC_I3DRecipeHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;
import powercraft.management.PC_Utils.Communication;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.mobile.PCmo_EntityMiner.MinerStatus;

public class PCmo_MinerManager implements PC_I3DRecipeHandler, PC_IPacketHandler {
	
	public static Class<? extends PCmo_IMinerBrain> mierBrainClass = PCmo_MinerBrain.class;
	
	public static PCmo_IMinerBrain createMinerBrain(PCmo_EntityMiner miner){
		try {
			PCmo_IMinerBrain brain = ValueWriting.createClass(mierBrainClass, new Class[]{PCmo_EntityMiner.class}, new Object[]{miner});
			return brain;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new PCmo_MinerBrain(miner);
	}
	
	@Override
	public boolean foundStructAt(World world, PC_Struct2<PC_VecI, Integer> structStart) {
		
		if(GameInfo.getBID(world, structStart.a)==Block.obsidian.blockID){
			List<PCmo_EntityMiner> miner = world.getEntitiesWithinAABB(PCmo_EntityMiner.class, AxisAlignedBB.getBoundingBox(structStart.a.x+1, structStart.a.y+1, structStart.a.z+1, 
					structStart.a.x+3, structStart.a.y+3, structStart.a.z+3));
			if(miner.size()!=1)
				return false;
			PCmo_EntityMiner m = miner.get(0);
			if(m.st.isExplosionResistent)
				return false;
			m.st.isExplosionResistent = true;
			for(int x=0; x<4; x++){
				for(int y=0; y<4; y++){
					for(int z=0; z<4; z++){
						ValueWriting.setBID(world, structStart.a.offset(x, y, z), 0, 0);
					}
				}
			}
			
			return true;
		}
		
		String eMinerStructure = Lang.tr("pc.miner.build.errInvalidStructure");
		String eMinerCrystals = Lang.tr("pc.miner.build.errMissingCrystals");
		
		PCmo_EntityMiner miner = new PCmo_EntityMiner(world, structStart.a.x+1, structStart.a.y, structStart.a.z+1);
		miner.rotationYaw = (structStart.b+1)*90;
		
		IInventory inv = null;
		
		for (int x = structStart.a.x; x <= structStart.a.x + 1 && inv==null; x++) {
			for (int z = structStart.a.z; z <= structStart.a.z + 1 && inv==null; z++) {
				inv = PC_InvUtils.getCompositeInventoryAt(world, new PC_VecI(x, structStart.a.y + 1, z));
			}
		}
		
		if (inv == null) {
			Communication.chatMsg(eMinerStructure, false);
			return false;
		}

		int cnt = PC_InvUtils.countPowerCrystals(inv);

		if (cnt == 0) {
			Communication.chatMsg(eMinerCrystals, false);
			return false;
		}

		// move contents.
		PC_InvUtils.moveStacks(inv, miner.xtals);
		PC_InvUtils.moveStacksForce(inv, miner.cargo);
		
		for(int x=0; x<2; x++){
			for(int y=0; y<2; y++){
				for(int z=0; z<2; z++){
					ValueWriting.setBID(world, structStart.a.offset(x, y, z), 0, 0);
				}
			}
		}
		
		miner.updateLevel();
		
		world.spawnEntityInWorld(miner);
		return true;
	}

	@Override
	public boolean handleIncomingPacket(EntityPlayer player, Object[] o) {
		Entity e = player.worldObj.getEntityByID((Integer)o[0]);
		if(e instanceof PCmo_EntityMiner){
			PCmo_EntityMiner miner = (PCmo_EntityMiner)e;
			String func = (String)o[1];
			if(func.equals("set")){
				double x = (Double)o[2];
				double y = (Double)o[3];
				double z = (Double)o[4];
				if(Math.abs(e.posX-x)>1)
					e.posX = (Double)o[2];
				if(Math.abs(e.posY-y)>1)
					e.posY = (Double)o[3];
				if(Math.abs(e.posZ-z)>1)
					e.posZ = (Double)o[4];
				e.motionX = (Double)o[5];
				e.motionY = (Double)o[6];
				e.motionZ = (Double)o[7];
				e.rotationYaw = (Float)o[8];
				byte[] b = (byte[])o[9];
				try {
					NBTTagCompound tag = CompressedStreamTools.decompress(b);
					miner.st.readFromNBT(tag);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				b = (byte[])o[10];
				try {
					NBTTagCompound tag = CompressedStreamTools.decompress(b);
					miner.readEntityFromNBT(tag);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				miner.updateLevel();
				e.setLocationAndAngles(e.posX, e.posY, e.posZ, e.rotationYaw, 0);
			}else if(func.equals("command")){
				miner.receiveKeyboardCommand((Integer)o[2]);
			}else if(func.equals("setLevel")){
				miner.st.level = (Integer)o[2];
				miner.updateLevel();
			}else if(func.equals("setInfo")){
				miner.setInfo((String)o[2], o[3]);
			}else if(func.equals("doInfoSet")){
				miner.doInfoSet((String)o[2], (Object[])o[3]);
			}
		}
		return false;
	}

}
