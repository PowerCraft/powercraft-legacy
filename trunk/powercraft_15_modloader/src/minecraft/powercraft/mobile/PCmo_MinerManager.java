package powercraft.mobile;

import java.io.IOException;
import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.network.PC_IPacketHandler;
import powercraft.api.recipes.PC_I3DRecipeHandler;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_BlockRegistry;
import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.utils.PC_MathHelper;
import powercraft.api.utils.PC_Struct2;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public class PCmo_MinerManager implements PC_I3DRecipeHandler, PC_IPacketHandler {
	
	public static Class<? extends PCmo_IMinerBrain> mierBrainClass = PCmo_MinerBrain.class;
	
	public static PCmo_IMinerBrain createMinerBrain(PCmo_EntityMiner miner, boolean server){
		try {
			PCmo_IMinerBrain brain = PC_ReflectHelper.create(mierBrainClass, miner, server);
			return brain;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new PCmo_MinerBrain(miner);
	}
	
	public static int countPowerCrystals(IInventory inventory) {
		boolean[] foundTable = { false, false, false, false, false, false,
				false, false };

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (inventory.getStackInSlot(i) != null
					&& inventory.getStackInSlot(i).itemID == PC_BlockRegistry
							.getPCBlockByName("PCco_BlockPowerCrystal").blockID) {
				foundTable[PC_MathHelper.clamp_int(inventory
						.getStackInSlot(i).getItemDamage(), 0, 7)] = true;
			}
		}

		int cnt = 0;

		for (int i = 0; i < 8; i++) {
			if (foundTable[i]) {
				cnt++;
			}
		}

		return cnt;
	}
	
	@Override
	public boolean foundStructAt(EntityPlayer entityplayer, World world, PC_Struct2<PC_VecI, Integer> structStart) {
		
		if(PC_Utils.getBID(world, structStart.a)==Block.obsidian.blockID){
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
						PC_Utils.setBID(world, structStart.a.offset(x, y, z), 0, 0);
					}
				}
			}
			
			return true;
		}
		
		String eMinerStructure = PC_LangRegistry.tr("pc.miner.build.errInvalidStructure");
		String eMinerCrystals = PC_LangRegistry.tr("pc.miner.build.errMissingCrystals");
		
		PCmo_EntityMiner miner = new PCmo_EntityMiner(world, structStart.a.x+1, structStart.a.y, structStart.a.z+1);
		miner.rotationYaw = (structStart.b+1)*90;
		
		IInventory inv = null;
		
		for (int x = structStart.a.x; x <= structStart.a.x + 1 && inv==null; x++) {
			for (int z = structStart.a.z; z <= structStart.a.z + 1 && inv==null; z++) {
				inv = PC_InventoryUtils.getInventoryAt(world, x, structStart.a.y + 1, z);
			}
		}
		
		if (inv == null) {
			PC_Utils.chatMsg(eMinerStructure);
			return false;
		}

		int cnt = countPowerCrystals(inv);

		if (cnt == 0) {
			PC_Utils.chatMsg(eMinerCrystals);
			return false;
		}

		// move contents.
		PC_InventoryUtils.moveStacks(inv, miner.xtals);
		PC_InventoryUtils.moveStacks(inv, miner.cargo);
		
		for(int x=0; x<2; x++){
			for(int y=0; y<2; y++){
				for(int z=0; z<2; z++){
					PC_Utils.setBID(world, structStart.a.offset(x, y, z), 0, 0);
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

	@Override
	public boolean canBeCrafted() {
		return true;
	}

}
