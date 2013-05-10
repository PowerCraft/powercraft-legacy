package powercraft.launcher.updategui;

import powercraft.launcher.update.PC_UpdateManager;
import powercraft.launcher.update.PC_UpdateManager.ModuleUpdateInfo;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLPackTag;

public class PC_GuiScrollModuleAndPackScroll extends PC_GuiScroll{
	
	private int activeElement = -1;
	
	public PC_GuiScrollModuleAndPackScroll(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public int getElementCount() {
		return PC_UpdateManager.updateInfo.getPacks().size() + PC_UpdateManager.moduleList.size() + 2;
	}

	@Override
	public int getElementHeight(int element) {
		if(element==1 || element==PC_UpdateManager.updateInfo.getPacks().size()+2)
			return 12;
		if(element==0)
			return 14;
		if(element<PC_UpdateManager.updateInfo.getPacks().size()+2)
			return 14;
		return 24;
	}

	@Override
	public boolean isElementActive(int element) {
		return activeElement==element;
	}

	@Override
	public void drawElement(int element, int par1, int par2, float par3) {
		String displayText = null;
		if(element==1){
			displayText = "\u2193 Packs \u2193";
		}else if(element==PC_UpdateManager.updateInfo.getPacks().size()+2){
			displayText = "\u2193 Modules \u2193";
		}
		if(displayText!=null){
			int width = gsfontRenderer.getStringWidth(displayText);
			drawHorizontalLine(0, (this.gswidth-8-width)/2-2, 6, 0xFFFFFFFF);
			drawHorizontalLine((this.gswidth-8+width)/2+2, this.gswidth-8, 6, 0xFFFFFFFF);
			drawCenteredString(gsfontRenderer, displayText, (this.gswidth-8)/2, 1, 0xFFFFFFFF);
		}else if(element==0){
			drawString(gsfontRenderer, "API", 2, 2, 0xffffff);
		}else if(element<PC_UpdateManager.updateInfo.getPacks().size()+2){
			XMLPackTag pack = PC_UpdateManager.updateInfo.getPacks().get(element-2);
			drawString(gsfontRenderer, pack.getName(), 2, 2, 0xffffff);
		}else{
			ModuleUpdateInfo module = getModuleInfo(element);
			if(module.oldVersion==null){
				drawString(gsfontRenderer, module.xmlModule.getName(), 2, 2, 0xffffff);
				drawString(gsfontRenderer, "*NEW*", 2, 12, 0xffffff);
			}else{
				drawString(gsfontRenderer, module.module.getModuleName(), 2, 2, 0xffffff);
				drawString(gsfontRenderer, module.oldVersion.toString(), 2, 12, 0xffffff);
			}
		}
	}

	public ModuleUpdateInfo getModuleInfo(int element){
		element -= PC_UpdateManager.updateInfo.getPacks().size()+3;
		int i=0;
		while(element>=0){
			if(PC_UpdateManager.moduleList.get(i).module==null){
				//if(PC_UpdateManager.moduleList.get(i).module.getModuleName().equals("Api")){
					//element++;
				//}
			}else{
				if(PC_UpdateManager.moduleList.get(i).xmlModule.getName().equals("Api")){
					element++;
				}
			}
			i++;
			element--;
		}
		return PC_UpdateManager.moduleList.get(i-1);
	}
	
	@Override
	public void clickElement(int element, int par1, int par2, int par3) {
		if(!(element==1 || element==PC_UpdateManager.updateInfo.getPacks().size()+2)){
			activeElement = element;
		}
		if(element==-1)
			activeElement = -1;
	}
	
	public Object getSelection(){
		if(activeElement==0){
			for(ModuleUpdateInfo module:PC_UpdateManager.moduleList){
				if(module.module!=null){
					if(module.module.getModuleName().equals("Api")){
						return module;
					}
				}else{
					if(module.xmlModule.getName().equals("Api")){
						return module;
					}
				}
			}
			return null;
		}
		if(activeElement>1 && activeElement<PC_UpdateManager.updateInfo.getPacks().size()+2){
			XMLPackTag pack = PC_UpdateManager.updateInfo.getPacks().get(activeElement-2);
			return pack;
		}else if(activeElement>PC_UpdateManager.updateInfo.getPacks().size()+2){
			ModuleUpdateInfo module = getModuleInfo(activeElement);
			return module;
		}
		return null;
	}
	
}
