package powercraft.api.hooks;

import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiControls;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;

public class PC_GuiControlsFix extends GuiControls {
	
	private GuiScreen parentScreen;
	private final GameSettings options;
	private PC_GuiControlsScrollPanel scrollPane;
	
	public PC_GuiControlsFix(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
		super(par1GuiScreen, par2GameSettings);
		parentScreen = par1GuiScreen;
		options = par2GameSettings;
	}
	
	@Override
	public void initGui() {
		scrollPane = new PC_GuiControlsScrollPanel(this, options, mc);
		StringTranslate var1 = StringTranslate.getInstance();
		int var2 = width / 2 - 155;
		
		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 28, var1.translateKey("gui.done")));
		scrollPane.registerScrollButtons(buttonList, 7, 8);
		this.screenTitle = var1.translateKey("controls.title");
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		if (scrollPane.keyTyped(par1, par2)) {
			super.keyTyped(par1, par2);
		}
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		scrollPane.drawScreen(par1, par2, par3);
		drawCenteredString(fontRenderer, screenTitle, width / 2, 4, 0xffffff);
		for (int var4 = 0; var4 < this.buttonList.size(); ++var4) {
			GuiButton var5 = (GuiButton) this.buttonList.get(var4);
			var5.drawButton(this.mc, par1, par2);
		}
	}
	
}
