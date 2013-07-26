package powercraft.apiOld;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.utils.PC_GlobalVariables;
import powercraft.api.utils.PC_Utils;
import powercraft.launcher.PC_Logger;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLTag;

public class PC_ThreadLangUpdates extends Thread {
	
	private static final String url = "https://dl.dropbox.com/s/ba718hnoyf5cvrr/LangInfo.xml?dl=1";
	
	public PC_ThreadLangUpdates(){
		start();
	}
	
	private void onLangInfoDownloaded(String page) {
		PC_Logger.fine("\n\nLang information received from server.");
		XMLLangInfoTag langInfo=null;
		
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new ByteArrayInputStream(page.getBytes("UTF-8")));
			doc.getDocumentElement().normalize();
			NodeList node = doc.getElementsByTagName("Info");
			
			if (node.getLength() != 1) {
				PC_Logger.severe("No Info node found");
				return;
			}
			
			langInfo = new XMLLangInfoTag(node.item(0)).read();
			
		} catch (SAXParseException err) {
			PC_Logger.severe("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
			PC_Logger.severe(" " + err.getMessage());
		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();
		} catch (Throwable t) {
			PC_Logger.throwing("PC_ThreadLangUpdates", "onUpdateInfoDownloaded()", t);
			t.printStackTrace();
		}
		
		if(langInfo != null){
			XMLLangVersionTag lang = langInfo.getLang(PC_LangRegistry.getUsedLang());
			if(lang!=null){
				int nv = lang.getVersion();
				int v = PC_GlobalVariables.config.getInt("lang.versions."+lang.getLang());
				if(v<nv){
					if(downloadLang(lang)){
						PC_GlobalVariables.config.setInt("lang.versions."+lang.getLang(), nv);
						PC_GlobalVariables.saveConfig();
					}
				}
			}
		}
		
	}
	
	private boolean downloadLang(XMLLangVersionTag langVersion){
		try {
			File langFile = new File(PC_Utils.getPowerCraftFile(), "lang");
			if(!langFile.exists())
				langFile.mkdirs();
			
			URL url = new URL(langVersion.getDownload());
			ZipInputStream zis = new ZipInputStream(url.openStream());
			ZipEntry ze = null;

			while ((ze = zis.getNextEntry()) != null){
				File file = new File(langFile, ze.getName());

                if (file.exists()){
                    file.delete();
                }

                FileOutputStream fos = new FileOutputStream(file);

                for (int c = zis.read(); c != -1; c = zis.read()) {
                	fos.write(c);
                }

                zis.closeEntry();
                fos.flush();
                fos.close();

			}
			
			zis.close();
            PC_Logger.fine("Language pack updated.\n\n");
            PC_LangRegistry.reloadLanguage();
			
            return true;
            
		} catch (Exception e) {
			PC_Logger.warning("Error while downloading lang info");
		}
		 return false;
	}
	
	@Override
	public void run(){
		try {
			URL url = new URL(this.url);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String page = "";
			String line;
			
			while ((line = reader.readLine()) != null) {
				page += line + "\n";
			}
			
			reader.close();
			onLangInfoDownloaded(page);
		} catch (Exception e) {
			PC_Logger.warning("Error while downloading lang info");
		}
	}
	
	public static class XMLLangInfoTag extends XMLTag<XMLLangInfoTag>{
		private List<XMLLangVersionTag> langs = new ArrayList<XMLLangVersionTag>();
		
		public XMLLangInfoTag(Node node) {
			super(node);
		}

		public XMLLangVersionTag getLang(String usedLang) {
			for(XMLLangVersionTag lang:langs){
				if(lang.getLang().equalsIgnoreCase(usedLang)){
					return lang;
				}
			}
			return null;
		}

		public List<XMLLangVersionTag> getLangs(){
			return new ArrayList<XMLLangVersionTag>(langs);
		}
		
		@Override
		protected void readAttributes(Element element) {}

		@Override
		protected void readChild(String childName, Node childNode) {
			if (childName.equalsIgnoreCase("lang")) {
				langs.add(new XMLLangVersionTag(this, childNode).read());
			}
		}
		
	}
	
	public static class XMLLangVersionTag extends XMLTag<XMLLangVersionTag>{

		private String lang;
		private int version;
		private String download;
		
		public XMLLangVersionTag(XMLTag parent, Node node) {
			super(parent, node);
		}

		public String getLang(){
			return lang;
		}
		
		public int getVersion(){
			return version;
		}
		
		public String getDownload(){
			return download;
		}
		
		@Override
		protected void readAttributes(Element element) {
			lang = element.getAttribute("lang");
			String sVersion = element.getAttribute("version");
			try{
				version = Integer.parseInt(sVersion);
			}catch(NumberFormatException e){
				PC_Logger.throwing("PC_ThreadLangUpdates.XMLLangVersionTag", "readAttributes", e);
				e.printStackTrace();
			}
			download = element.getTextContent().trim();
		}

		@Override
		protected void readChild(String childName, Node childNode) {}
		
	}
	
}
