package powercraft.launcher.updategui;

import powercraft.launcher.updategui.PC_GuiScroll.ScrollElement;

public class PC_GuiScrollElementHorizontalLine extends ScrollElement {
	
	private String displayText=null;
	
	public PC_GuiScrollElementHorizontalLine(){
		
	}
	
	public PC_GuiScrollElementHorizontalLine(String text){
		displayText = text;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		if(displayText==null){
			drawHorizontalLine(0, elementWidth, 0, 0xFFFFFFFF);
		}else{
			int width = fontRenderer.getStringWidth(displayText);
			drawHorizontalLine(0, (elementWidth-width)/2-2, 6, 0xFFFFFFFF);
			drawHorizontalLine((elementWidth+width)/2+2, elementWidth, 6, 0xFFFFFFFF);
			drawCenteredString(fontRenderer, displayText, elementWidth/2, 1, 0xFFFFFFFF);
		}
	}
	
	@Override
	public boolean showSelection() {
		return false;
	}

	@Override
	public int getHeight() {
		if(displayText!=null){
			return 12;
		}
		return 1;
	}
	
}
