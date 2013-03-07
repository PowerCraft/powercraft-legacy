package powercraft.api.hacks;

import java.io.File;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.storage.WorldInfo;
import powercraft.api.PC_GlobalVariables;
import powercraft.api.PC_IDResolver;
import powercraft.api.registry.PC_DataHandlerRegistry;

public class PC_HackedSaveHandler extends AnvilSaveHandler {

	public PC_HackedSaveHandler(File par1File, String par2Str, boolean par3) {
		super(par1File, par2Str, par3);
	}

	@Override
	public void saveWorldInfoWithPlayer(WorldInfo par1WorldInfo, NBTTagCompound par2nbtTagCompound) {
		saveBlockID();
		PC_DataHandlerRegistry.savePowerCraftData(par1WorldInfo, getSaveDirectory());
		super.saveWorldInfoWithPlayer(par1WorldInfo, par2nbtTagCompound);
	}

	@Override
	public WorldInfo loadWorldInfo() {
		loadBlockID();
		WorldInfo worldInfo = super.loadWorldInfo();
		PC_DataHandlerRegistry.loadPowerCraftData(worldInfo, getSaveDirectory());
		return worldInfo;
	}

	@Override
	public void saveWorldInfo(WorldInfo par1WorldInfo) {
		PC_IDResolver.resetPCObjectsIDs();
		saveBlockID();
		super.saveWorldInfo(par1WorldInfo);
	}
	
	public void loadBlockID(){
		if(PC_GlobalVariables.oldConsts!=null){
			PC_GlobalVariables.consts = PC_GlobalVariables.oldConsts;
			PC_GlobalVariables.oldConsts = null;
		}
		if(!PC_IDResolver.loadPCObjectsIDs(getSaveDirectory())){
			PC_IDResolver.resetPCObjectsIDs();
			saveBlockID();
		}else{
			PC_IDResolver.savePCObjectsIDs(getSaveDirectory());
		}
	}
	
	public void saveBlockID(){
		PC_IDResolver.savePCObjectsIDs(getSaveDirectory());
	}
	
}
