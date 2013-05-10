package powercraft.launcher.updategui;

import java.util.List;

import powercraft.launcher.loader.PC_ModuleVersion;
import powercraft.launcher.update.PC_UpdateManager.ModuleUpdateInfo;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLModuleTag;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLPackTag;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLVersionTag;

public class PC_GuiScrollVersions extends PC_GuiScroll {

	private PC_GuiScrollModuleAndPackScroll moduleAndPack;
	private PC_GuiUpdate guiUpdate;
	private int activeElement = -1;
	private List<String>lText;
	
	public PC_GuiScrollVersions(int x, int y, int width, int height, PC_GuiScrollModuleAndPackScroll moduleAndPack, PC_GuiUpdate guiUpdate) {
		super(x, y, width, height);
		this.moduleAndPack = moduleAndPack;
		this.guiUpdate = guiUpdate;
		if(this.gswidth-12>10){
			lText = gsfontRenderer.listFormattedStringToWidth("Select module or pack", this.gswidth-12);
		}
	}

	@Override
	public int getElementCount() {
		Object obj = moduleAndPack.getSelection();
		if(obj instanceof XMLPackTag){
			return ((XMLPackTag)obj).getVersions().size();
		}else if(obj instanceof ModuleUpdateInfo){
			return ((ModuleUpdateInfo)obj).versions.length;
		}
		return 1;
	}

	@Override
	public int getElementHeight(int element) {
		Object obj = moduleAndPack.getSelection();
		if(obj==null){
			return 4+lText.size()*10;
		}
		return 14;
	}

	@Override
	public boolean isElementActive(int element) {
		Object obj = moduleAndPack.getSelection();
		if(obj==null)
			return false;
		return activeElement==element;
	}

	@Override
	public void drawElement(int element, int par1, int par2, float par3) {
		Object obj = moduleAndPack.getSelection();
		String disp = getObjectString(obj, element);
		if(disp!=null){
			drawString(gsfontRenderer, disp, 2, 2, 0xffffff);
		}else{
			for(int i=0; i<lText.size(); i++){
				gsfontRenderer.drawString(lText.get(i), 2, i*10+2, 0xFFFFFFFF);
			}
		}
	}

	public String getObjectString(Object obj, int element){
		String ret=null;
		if(obj instanceof XMLPackTag){
			ret = "v"+ ((XMLPackTag)obj).getVersions().get(element).getVersion();
			if(element==activeElement){
				guiUpdate.download.enabled = true;
				if(((XMLPackTag)obj).getVersions().get(element).getDownloadLink()==null || ((XMLPackTag)obj).getVersions().get(element).getDownloadLink().equals("")){
					guiUpdate.download.enabled = false;
				}
				guiUpdate.download.drawButton = true;
				guiUpdate.activate.drawButton = false;
				guiUpdate.delete.drawButton = false;
			}
		}else if(obj instanceof ModuleUpdateInfo){
			ModuleUpdateInfo ui = (ModuleUpdateInfo)obj;
			ret = "v"+ ui.versions[element];
			if(element==activeElement){
				if(ui.module!=null){
					guiUpdate.download.enabled = ui.module.getVersion(ui.versions[element])==null;
				}else{
					guiUpdate.download.enabled = true;
				}
				if(ui.xmlModule==null){
					guiUpdate.download.enabled = false;
				}else{
					if(ui.xmlModule.getVersion(ui.versions[element])==null 
							|| ui.xmlModule.getVersion(ui.versions[element]).getDownloadLink()==null 
							|| ui.xmlModule.getVersion(ui.versions[element]).getDownloadLink().equals("")){
						guiUpdate.download.enabled = false;
					}
				}
				guiUpdate.download.drawButton = true;
				guiUpdate.activate.drawButton = false;
				guiUpdate.delete.drawButton = false;
			}
			if(ui.oldVersion!=null&&ui.oldVersion.equals(ui.versions[element])){
				ret += " (ACTIVE)";
			}else if(ui.module==null || ui.module.getNewest().getVersion().compareTo(ui.versions[element])<0){
				ret += " *NEW*";
			}
		}else{
			guiUpdate.download.enabled = false;
			guiUpdate.download.drawButton = true;
			guiUpdate.activate.drawButton = false;
			guiUpdate.delete.drawButton = false;
		}
		return ret;
	}
	
	public String getActiveVersionInfo(){
		Object obj = moduleAndPack.getSelection();
		if(activeElement<0)
			return "Select version";
		if(obj instanceof XMLPackTag){
			XMLPackTag pt = (XMLPackTag)obj;
			if(activeElement>=pt.getVersions().size()){
				activeElement=pt.getVersions().size()-1;
			}
			String info = pt.getVersions().get(activeElement).getInfo();
			info += "\n\nContains Modules:";
			for(XMLModuleTag mt:pt.getModules()){
				info += "\n-"+mt.getName();
			}
			return info;
		}else if(obj instanceof ModuleUpdateInfo){
			ModuleUpdateInfo ui = (ModuleUpdateInfo)obj;
			if(activeElement>=ui.versions.length){
				activeElement=ui.versions.length-1;
			}
			if(ui.xmlModule.getVersion(ui.versions[activeElement])==null)
				return "No information found";
			return ui.xmlModule.getVersion(ui.versions[activeElement]).getInfo();
		}
		return "Select version";
	}
	
	@Override
	public void clickElement(int element, int par1, int par2, int par3) {
		activeElement = element;
	}

	public XMLVersionTag getActiveVersionDownload() {
		Object obj = moduleAndPack.getSelection();
		if(activeElement<0)
			return null;
		if(obj instanceof XMLPackTag){
			XMLPackTag pt = (XMLPackTag)obj;
			if(activeElement>=pt.getVersions().size()){
				activeElement=pt.getVersions().size()-1;
			}
			return pt.getVersions().get(activeElement);
		}else if(obj instanceof ModuleUpdateInfo){
			ModuleUpdateInfo ui = (ModuleUpdateInfo)obj;
			if(activeElement>=ui.versions.length){
				activeElement=ui.versions.length-1;
			}
			return ui.xmlModule.getVersion(ui.versions[activeElement]);
		}
		return null;
	}

	public PC_ModuleVersion getActiveModuleVersion() {
		Object obj = moduleAndPack.getSelection();
		if(activeElement<0)
			return null;
		if(obj instanceof ModuleUpdateInfo){
			ModuleUpdateInfo ui = (ModuleUpdateInfo)obj;
			if(activeElement>=ui.versions.length){
				activeElement=ui.versions.length-1;
			}
			if(ui.module==null)
				return null;
			return ui.module.getVersion(ui.versions[activeElement]);
		}
		return null;
	}
	
}
