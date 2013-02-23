package powercraft.management.hacks;

import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;

public class PC_GuiOptionsHack extends GuiOptions {

	private final GameSettings options;
	
	public PC_GuiOptionsHack(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
		super(par1GuiScreen, par2GameSettings);
		options = par2GameSettings;
	}

	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if(guiButton.id==100){
			mc.gameSettings.saveOptions();
            mc.displayGuiScreen(new PC_GuiControlsHack(this, options));
		}else{
			super.actionPerformed(guiButton);
		}
	}
	
}
