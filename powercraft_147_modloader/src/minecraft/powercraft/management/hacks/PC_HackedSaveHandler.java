package powercraft.management.hacks;

import java.io.File;

import net.minecraft.src.AnvilSaveHandler;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.WorldInfo;
import powercraft.management.PC_GlobalVariables;
import powercraft.management.PC_IDResolver;
import powercraft.management.registry.PC_DataHandlerRegistry;

public class PC_HackedSaveHandler extends AnvilSaveHandler {

	public PC_HackedSaveHandler(File par1File, String par2Str, boolean par3) {
		super(par1File, par2Str, par3);
	}

	@Override
	public void saveWorldInfoWithPlayer(WorldInfo par1WorldInfo,
			NBTTagCompound par2nbtTagCompound) {
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
