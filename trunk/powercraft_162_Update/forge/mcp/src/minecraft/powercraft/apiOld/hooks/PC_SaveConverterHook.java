package powercraft.api.hooks;

import java.io.File;

import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveHandler;

public class PC_SaveConverterHook extends AnvilSaveConverter {
	
	public PC_SaveConverterHook(File par1File) {
		super(par1File);
	}

	@Override
	public ISaveHandler getSaveLoader(String par1Str, boolean par2) {
		return new PC_SaveHandlerHook(savesDirectory, par1Str, par2);
	}
	
}
