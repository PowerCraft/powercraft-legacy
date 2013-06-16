package powercraft.weasel;

import net.minecraft.src.EntityPlayer;

import org.lwjgl.input.Keyboard;

import powercraft.api.gres.PC_GresButton;
import powercraft.api.gres.PC_GresColorMap;
import powercraft.api.gres.PC_GresLayoutV;
import powercraft.api.gres.PC_GresWidget;
import powercraft.api.gres.PC_GresWidget.PC_GresAlign;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.gres.PC_IGresGui;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Struct4;
import powercraft.api.utils.PC_VecI;


public class PCws_GuiWeaselTouchscreen implements PC_IGresClient {

	private PCws_TileEntityWeasel te;
	private PC_GresColorMap colorMap;
	private PC_GresButton close;
	private int screen[][] = new int[PCws_WeaselPluginTouchscreen.WIDTH][PCws_WeaselPluginTouchscreen.HEIGHT];
	private int timer = -1;
	
	public PCws_GuiWeaselTouchscreen(EntityPlayer player, PC_TileEntity te, Object[] o){
		this.te = (PCws_TileEntityWeasel)te;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		updateScreen(gui);
		PC_GresWidget mvg = new PC_GresLayoutV();
		mvg.setAlignH(PC_GresAlign.CENTER);

		colorMap = new PC_GresColorMap(screen).setScale(4);
		mvg.add(colorMap);
		mvg.add(close = new PC_GresButton("pc.gui.close").setButtonPadding(4, 4));

		gui.add(mvg);
		gui.setBackground(0x90101010, 0x90101010);

		gui.setFocus(colorMap);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if (widget == colorMap) {
			if(timer == -1) {
				timer = 0;
				return;
			}
			String event = colorMap.getLastEvent();
			if(event.length()==0) return;
			PC_VecI mouse = colorMap.getLastMousePos();
			int mouseKey = colorMap.getLastMouseKey();
			char key = colorMap.getLastKey();

			te.call("event", new PC_Struct4<String, PC_VecI, Integer, Integer>(event, mouse, mouseKey, (int)key));
			
			colorMap.setLastEvent("");
		}else if(widget == close){
			gui.close();
		}
	}

	@Override
	public void onKeyPressed(PC_IGresGui gui, char c, int i) {
		if(i==Keyboard.KEY_ESCAPE)
			gui.close();
	}

	@Override
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public void updateScreen(PC_IGresGui gui) {
		for(int j=0; j<PCws_WeaselPluginTouchscreen.HEIGHT; j++){
			for(int i=0; i<PCws_WeaselPluginTouchscreen.WIDTH; i++){
				screen[i][j] = (Integer)te.getData("pic["+i+"]["+j+"]");
			}
		}
	}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,
			float par3) {
		return false;
	}

	@Override
	public void keyChange(String key, Object value) {}

}
