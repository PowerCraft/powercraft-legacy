package powercraft.management.hacks;

import java.util.List;

import net.minecraft.src.BaseMod;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ModLoader;
import powercraft.management.PC_ClientUtils;
import powercraft.management.PC_VecI;
import powercraft.management.gres.PC_GresButton;
import powercraft.management.gres.PC_GresFrame;
import powercraft.management.gres.PC_GresGap;
import powercraft.management.gres.PC_GresGui;
import powercraft.management.gres.PC_GresLabel;
import powercraft.management.gres.PC_GresLayoutV;
import powercraft.management.gres.PC_GresScrollArea;
import powercraft.management.gres.PC_GresWidget;
import powercraft.management.gres.PC_GresWidget.PC_GresAlign;
import powercraft.management.gres.PC_IGresClient;
import powercraft.management.gres.PC_IGresGui;
import powercraft.management.reflect.PC_ReflectHelper;

public class PC_GuiMods implements PC_IGresClient {

    private GuiScreen parentScreen;
    private GameSettings options;
    
    private PC_GresButton done;
    
    private PC_GresScrollArea scroll;
    
    private PC_GresLayoutV baseWidget;
    
	public PC_GuiMods(GuiScreen parentScreen, GameSettings options) {
		this.parentScreen = parentScreen;
		this.options = options;
	}

	@Override
	public void keyChange(String key, Object value) {}
	
	public PC_GresWidget makeModWidget(BaseMod mod){
		PC_GresLayoutV lv = new PC_GresLayoutV();
		lv.setAlignH(PC_GresAlign.STRETCH);
		PC_GresLayoutV lv2 = new PC_GresLayoutV();
		lv2.setAlignH(PC_GresAlign.LEFT);
		lv2.add(new PC_GresLabel(mod.getName()));
		lv.add(lv2);
		lv.add(new PC_GresGap(300, 1));
		lv2 = new PC_GresLayoutV();
		lv2.setAlignH(PC_GresAlign.RIGHT);
		lv2.add(new PC_GresLabel(mod.getVersion()));
		lv.add(lv2);
		PC_GresFrame frame = new PC_GresFrame();
		frame.add(lv);
		return frame;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_VecI size = gui.getSize();
		baseWidget = (PC_GresLayoutV)PC_ReflectHelper.getValue(PC_GresGui.class, gui, 1);
		PC_GresLabel label;
		gui.add(label = new PC_GresLabel("pc.gui.mods"));
		label.enable(false);
		label.setColor(PC_GresLabel.textColorDisabled, 0xFFFFFF);
		label.setColor(PC_GresLabel.textColorShadowDisabled, (0xFFFFFF & 16579836) >> 2 | 0xFFFFFF & -16777216);
		PC_GresLayoutV lv = new PC_GresLayoutV();
		lv.setAlignH(PC_GresAlign.STRETCH);
		List mods = ModLoader.getLoadedMods();
		lv.add(makeModWidget(new ModLoaderFake()));
		for(int i=0; i<mods.size(); i++){
			BaseMod mod = (BaseMod)mods.get(i);
			lv.add(makeModWidget(mod));
		}
		//PC_GresFrame frame = new PC_GresFrame();
		scroll = new PC_GresScrollArea(lv, PC_GresScrollArea.VSCROLL);
		scroll.setSize(300, size.y-50);
		scroll.setMinSize(300, size.y-50);
		scroll.setAlignV(PC_GresAlign.STRETCH);
		//frame.add(scroll);
		gui.add(scroll);
		done = new PC_GresButton("gui.done");
		done.setMinSize(200, 20);
		gui.add(done);
		PC_ReflectHelper.setValue(PC_GresWidget.class, baseWidget, 19, false);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==done){
			PC_ClientUtils.mc().displayGuiScreen(parentScreen);
		}
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		PC_ClientUtils.mc().displayGuiScreen(parentScreen);
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		PC_ClientUtils.mc().displayGuiScreen(parentScreen);
	}

	@Override
	public void updateTick(PC_IGresGui gui) {
		PC_VecI size = gui.getSize();
		scroll.setSize(scroll.getSize().x, size.y-50);
		gui.add(null);
	}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,
			float par3) {
		return false;
	}

	private class ModLoaderFake extends BaseMod{

		@Override
		public String getName() {
			return "ModLoader";
		}
		
		@Override
		public String getVersion() {
			return ModLoader.VERSION;
		}

		@Override
		public void load() {}
		
	}
	
}
