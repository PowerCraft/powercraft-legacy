package powercraft.api.structure;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import powercraft.api.interfaces.PC_IDataHandler;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public class PC_CableNetworkManager implements PC_IDataHandler {

	private static HashMap<Integer, PC_CableNetworkGlobal> globalNetworks = new HashMap<Integer, PC_CableNetworkGlobal>();
	private static HashMap<PC_VecI, PC_CableNetworkChunk> chunkNetworks = new HashMap<PC_VecI, PC_CableNetworkChunk>();
	private static boolean resolved=false;
	
	private static void resolve(){
		if(!resolved){
			resolved = true;
			for(PC_CableNetworkGlobal globalNetwork:globalNetworks.values()){
				globalNetwork.resolve();
			}
		}
	}
	
	public static PC_CableNetworkGlobal getGlobalNetwork(int id){
		resolve();
		return globalNetworks.get(id);
	}
	
	public static PC_CableNetworkChunk getChunkNetwork(World world, PC_VecI coord){
		resolve();
		return chunkNetworks.get(new PC_VecI(coord.x>>4, coord.z>>4, world.getWorldInfo().getDimension()));
	}
	
	public static PC_CableNetwork getCableNetwork(World world, PC_VecI coord, int network){
		PC_CableNetworkChunk cnc = getChunkNetwork(world, coord);
		if(cnc==null)
			return null;
		return cnc.getCableNetwork(network);
	}

	public static int createNewCableNetwork(World world, PC_VecI coord){
		resolve();
		PC_VecI pos = new PC_VecI(coord.x>>4, coord.z>>4, world.getWorldInfo().getDimension());
		PC_CableNetworkChunk cnc = chunkNetworks.get(pos);
		if(cnc==null){
			cnc = new PC_CableNetworkChunk(pos);
			chunkNetworks.put(pos, cnc);
		}
		int id=1;
		boolean again;
		do{
			again=false;
			for(Integer i:globalNetworks.keySet()){
				if(i==id){
					id++;
					again=true;
				}
			}
		}while(again);
		PC_CableNetworkGlobal global = new PC_CableNetworkGlobal();
		globalNetworks.put(id, global);
		return cnc.createNewCableNetwork(global);
	}
	
	public static int getPowerValue(World world, PC_VecI coord, int network){
		PC_CableNetwork cn = getCableNetwork(world, coord, network);
		if(cn!=null)
			return cn.getPowerValue();
		return 0;
	}
	
	public static void setPowerValue(World world, PC_VecI coord, int network, int value){
		PC_CableNetwork cn = getCableNetwork(world, coord, network);
		if(cn!=null)
			cn.setPowerValue(value);
	}
	
	public static void addRef(World world, PC_VecI coord, int network) {
		PC_CableNetwork cn = getCableNetwork(world, coord, network);
		if(cn!=null)
			cn.addRef();
	}
	
	public static void release(World world, PC_VecI coord, int network) {
		PC_CableNetwork cn = getCableNetwork(world, coord, network);
		if(cn!=null)
			cn.release(new PC_VecI((coord.x%16+16)%16, coord.y, (coord.z%16+16)%16));
	}
	
	public static void addIO(World world, PC_VecI coord, int network) {
		PC_CableNetwork cn = getCableNetwork(world, coord, network);
		if(cn!=null)
			cn.addIO(new PC_VecI((coord.x%16+16)%16, coord.y, (coord.z%16+16)%16));
	}
	
	@Override
	public String getName() {
		return "PC_CableNetworkManager";
	}

	@Override
	public void load(NBTTagCompound nbtTag) {
		resolved = false;
		chunkNetworks = (HashMap<PC_VecI, PC_CableNetworkChunk>) PC_Utils.loadFromNBT(nbtTag, "chunkNetworks");
		globalNetworks = (HashMap<Integer, PC_CableNetworkGlobal>) PC_Utils.loadFromNBT(nbtTag, "globalNetworks");
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbtTag) {
		PC_Utils.saveToNBT(nbtTag, "chunkNetworks", chunkNetworks);
		PC_Utils.saveToNBT(nbtTag, "globalNetworks", globalNetworks);
		return nbtTag;
	}

	@Override
	public boolean needSave() {
		return true;
	}

	@Override
	public void reset() {
		resolved = false;
		chunkNetworks.clear();
		globalNetworks.clear();
	}
	
}
