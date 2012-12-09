package powercraft.management;

import java.io.File;

import net.minecraft.src.AnvilSaveHandler;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.WorldInfo;

public class PC_HackedSaveHandler extends AnvilSaveHandler {

	public PC_HackedSaveHandler(File par1File, String par2Str, boolean par3) {
		super(par1File, par2Str, par3);
	}

	@Override
	public void saveWorldInfoWithPlayer(WorldInfo par1WorldInfo,
			NBTTagCompound par2nbtTagCompound) {
		saveBlockID();
		super.saveWorldInfoWithPlayer(par1WorldInfo, par2nbtTagCompound);
	}

	@Override
	public WorldInfo loadWorldInfo() {
		loadBlockID();
		return super.loadWorldInfo();
	}

	@Override
	public void saveWorldInfo(WorldInfo par1WorldInfo) {
		saveBlockID();
		super.saveWorldInfo(par1WorldInfo);
	}

	public void loadBlockID(){
		if(!PC_Utils.loadPCObjectsIDs(getSaveDirectory())){
			saveBlockID();
		}
	}
	
	public void saveBlockID(){
		PC_Utils.resetPCObjectsIDs();
		PC_Utils.savePCObjectsIDs(getSaveDirectory());
	}
	
}
