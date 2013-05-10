package powercraft.launcher.updategui;

import java.util.ArrayList;
import java.util.List;

public class PC_GuiScrollVersionInfo extends PC_GuiScroll {
	
	private PC_GuiScrollVersions version;
	
	public PC_GuiScrollVersionInfo(int x, int y, int width, int height, PC_GuiScrollVersions version) {
		super(x, y, width, height);
		this.version = version;
	}

	@Override
	public int getElementCount() {
		return 1;
	}

	@Override
	public int getElementHeight(int element) {
		return getInfoText().size()*10+4;
	}

	@Override
	public boolean isElementActive(int element) {
		return false;
	}

	@Override
	public void drawElement(int element, int par1, int par2, float par3) {
		List<String>lText = getInfoText();
		for(int i=0; i<lText.size(); i++){
			gsfontRenderer.drawString(lText.get(i), 2, i*10+2, 0xFFFFFFFF);
		}
	}

	public List<String> getInfoText(){
		String info = version.getActiveVersionInfo();
		if(this.gswidth-12>10){
			return gsfontRenderer.listFormattedStringToWidth(info, this.gswidth-12);
		}else{
			return new ArrayList<String>();
		}
	}
	
	@Override
	public void clickElement(int element, int par1, int par2, int par3) {
		
	}

}
