package powercraft.management.hacks;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.mod_PowerCraft;
import powercraft.management.gres.PC_GresGui;
import powercraft.management.reflect.PC_ReflectHelper;
import powercraft.management.registry.PC_LangRegistry;

public class PC_GuiMainMenuHack extends GuiMainMenu {

	private boolean resetDrawPos = false;
	
	@Override
	public void initGui(){
		super.initGui();
		
		for (int i = 0; i < this.controlList.size(); ++i)
        {
            GuiButton guiButton = (GuiButton)this.controlList.get(i);
            if(guiButton.id==3){
            	PC_ReflectHelper.setValue(GuiButton.class, guiButton, 0, 98);
            	break;
            }
        }
		
		int var4 = this.height / 4 + 48;
		
		controlList.add(new GuiButton(100, this.width / 2 + 2, var4 + 48, 98, 20, PC_LangRegistry.tr("pc.gui.mods")));
		
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton){
		if(guiButton.id==100){
			mc.displayGuiScreen(new PC_GresGui(null, new PC_GuiMods(this, this.mc.gameSettings)));
		}else if(guiButton.id==0){
			mc.displayGuiScreen(new PC_GuiOptionsHack(this, this.mc.gameSettings));
		}else{
			super.actionPerformed(guiButton);
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		resetDrawPos = true;
		super.drawScreen(par1, par2, par3);
		resetDrawPos = false;
		drawString(mc.fontRenderer, "PowerCraft " + mod_PowerCraft.getInstance().getVersion(), 2, height - 10, 0xffffffff);
	}

	@Override
	public void drawString(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5) {
		if(resetDrawPos && par3==2 && par4==height - 10){
			par4 -= par1FontRenderer.FONT_HEIGHT;
		}
		super.drawString(par1FontRenderer, par2Str, par3, par4, par5);
	}
	
	
}
