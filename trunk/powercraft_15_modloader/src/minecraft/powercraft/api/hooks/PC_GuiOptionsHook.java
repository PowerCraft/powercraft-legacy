package powercraft.api.hooks;

import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;

public class PC_GuiOptionsHook extends GuiOptions {
	
	private final GameSettings options;
	
	public PC_GuiOptionsHook(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
		super(par1GuiScreen, par2GameSettings);
		options = par2GameSettings;
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (guiButton.id == 100) {
			mc.gameSettings.saveOptions();
			mc.displayGuiScreen(new PC_GuiControlsFix(this, options));
		} else {
			super.actionPerformed(guiButton);
		}
	}
	
}
