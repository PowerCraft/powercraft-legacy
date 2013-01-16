package powercraft.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.DimensionManager;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.SaveHandler;

public class PC_ChunckUpdateForcer implements PC_IDataHandler, PC_IMSG {

	private static PC_ChunckUpdateForcer instance;
	private static HashMap<Integer, List<PC_VecI>> chuncks = new HashMap<Integer, List<PC_VecI>>();
	private static boolean needSave = false;
	
	public static PC_IDataHandler getInstance() {
		if(instance==null)
			instance = new PC_ChunckUpdateForcer();
		return instance;
	}
	
	public static void forceChunckUpdate(World world, PC_VecI pos){
		if(world.isRemote)
			return;
		int dimension = world.getWorldInfo().getDimension();
		List<PC_VecI> posList;
		if(chuncks.containsKey(dimension))
			posList = chuncks.get(dimension);
		else
			chuncks.put(dimension, posList = new ArrayList<PC_VecI>());
		pos = pos.copy();
		if(!posList.contains(pos)){
			posList.add(pos);
			needSave = true;
		}
	}
	
	public static void stopForceChunckUpdate(World world, PC_VecI pos){
		if(world.isRemote)
			return;
		int dimension = world.getWorldInfo().getDimension();
		if(chuncks.containsKey(dimension)){
			List<PC_VecI> posList = chuncks.get(dimension);
			pos = pos.copy();
			if(posList.contains(pos)){
				posList.remove(pos);
				needSave = true;
			}
			if(posList.size()==0){
				chuncks.remove(dimension);
				needSave = true;
			}
		}
	}
	
	private List<PC_VecI> loadList(NBTTagCompound nbtTag, List<PC_VecI> posList){
		int count = nbtTag.getInteger("count");
		for(int i=0; i<count; i++){
			PC_VecI pos = new PC_VecI();
			SaveHandler.loadFromNBT(nbtTag, "value["+i+"]", pos);
			posList.add(pos);
		}
		return posList;
	}
	
	@Override
	public void load(NBTTagCompound nbtTag) {
		reset();
		int count = nbtTag.getInteger("count");
		for(int i=0; i<count; i++){
			int dim = nbtTag.getInteger("key["+i+"]");
			List<PC_VecI> posList = loadList(nbtTag.getCompoundTag("value["+i+"]"), new ArrayList<PC_VecI>());
			chuncks.put(dim, posList);
		}
	}

	private NBTTagCompound saveList(NBTTagCompound nbtTag, List<PC_VecI> posList){
		nbtTag.setInteger("count", posList.size());
		int i=0;
		for(PC_VecI pos:posList){
			SaveHandler.saveToNBT(nbtTag, "value["+i+"]", pos);
			i++;
		}
		return nbtTag;
	}
	
	@Override
	public NBTTagCompound save(NBTTagCompound nbtTag) {
		needSave = false;
		nbtTag.setInteger("count", chuncks.size());
		int i=0;
		for(Entry<Integer, List<PC_VecI>> e:chuncks.entrySet()){
			nbtTag.setInteger("key["+i+"]", e.getKey());
			nbtTag.setCompoundTag("value["+i+"]", saveList(new NBTTagCompound(), e.getValue()));
			i++;
		}
		return nbtTag;
	}

	@Override
	public boolean needSave() {
		return needSave;
	}

	@Override
	public void reset() {
		needSave = false;
		chuncks.clear();
	}

	@Override
	public Object msg(int msg, Object... obj) {
		if(msg==PC_Utils.MSG_TICK_EVENT){
			for(Entry<Integer, List<PC_VecI>> e:chuncks.entrySet()){
				World world = DimensionManager.getWorld(e.getKey());
				if(world!=null){
					IChunkProvider chunckProvider = world.getChunkProvider();
					if(chunckProvider!=null){
						List<PC_VecI> chunckList = new ArrayList<PC_VecI>();
						for(PC_VecI pos:e.getValue()){
							PC_VecI p = pos.copy();
							p.x >>= 16;
							p.y = 0;
							p.z >>= 16;
							if(!chunckList.contains(p)){
								chunckList.add(p);
							}
						}
						for(PC_VecI pos:chunckList){
							chunckProvider.loadChunk(pos.x, pos.z);
						}
					}
				}
			}
		}
		return null;
	}
	
}
