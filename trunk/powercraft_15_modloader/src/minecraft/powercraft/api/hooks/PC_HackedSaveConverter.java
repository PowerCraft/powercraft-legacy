package powercraft.api.hooks;

import java.io.File;

import net.minecraft.src.AnvilSaveConverter;
import net.minecraft.src.ISaveHandler;

public class PC_HackedSaveConverter extends AnvilSaveConverter {
	
	public PC_HackedSaveConverter(File par1File) {
		super(par1File);
	}
	
	@Override
	public ISaveHandler getSaveLoader(String par1Str, boolean par2) {
		return new PC_HackedSaveHandler(savesDirectory, par1Str, par2);
	}
	
}
