package powercraft.launcher.updategui;

import java.util.List;

import powercraft.launcher.updategui.PC_GuiScroll.ScrollElement;

public class PC_GuiScrollElementText extends ScrollElement {

	private String text;
	private List<String>lText;
	
	public PC_GuiScrollElementText(String text){
		this.text = text;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		for(int i=0; i<lText.size(); i++){
			fontRenderer.drawString(lText.get(i), 2, i*10+2, 0xFFFFFFFF);
		}
	}
	
	@Override
	public void setElementWidth(int elementWidth) {
		if(elementWidth-4>10){
			lText = fontRenderer.listFormattedStringToWidth(this.text, elementWidth-4);
		}
		super.setElementWidth(elementWidth);
	}

	@Override
	public boolean showSelection() {
		return false;
	}

	@Override
	public int getHeight() {
		if(lText==null)
			return 0;
		return lText.size()*10+4;
	}
	
}
