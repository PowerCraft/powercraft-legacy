package powercraft.management;

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

import net.minecraft.crash.CallableMinecraftVersion;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.registry.PC_LangRegistry;
import powercraft.management.registry.PC_ModuleRegistry;

public class PC_UpdateManager {

	public static class Version implements Comparable<Version>{
		public int major;
		public int minor;
		public int patch;
		
		public Version(String s){
			String sl[] = s.split("\\.");
			major = Integer.parseInt(sl[0]);
			minor = Integer.parseInt(sl[1]);
			patch = Integer.parseInt(sl[2]);
		}

		@Override
		public String toString() {
			return major+"."+minor+"."+patch;
		}

		@Override
		public int compareTo(Version ver) {
			if(major>ver.major)
				return 1;
			if(major<ver.major)
				return -1;
			if(minor>ver.minor)
				return 1;
			if(minor<ver.minor)
				return -1;
			if(patch>ver.patch)
				return 1;
			if(patch<ver.patch)
				return -1;
			return 0;
		}	
	}
	
	private static class ThreadCheckUpdates extends Thread{
		private String url;

	    public ThreadCheckUpdates(String url)
	    {
	        this.url = url;
	    }

	    @Override
	    public void run()
	    {
	        try
	        {
	            URL url = new URL(this.url);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
	            String page = "";
	            String line;

	            while ((line = reader.readLine()) != null)
	            {
	                page += line + "\n";
	            }

	            reader.close();
	            onUpdateInfoDownloaded(page);
	        }
	        catch (Exception e)
	        {
	            PC_Logger.warning("Error while downloading update info");
	            e.printStackTrace();
	        }
	    }
	    
	}
	
	private static class ThreadDownloadTranslations extends Thread{
		private String url;
	    private PC_IModule module;
	    private int langVersion;

	    public ThreadDownloadTranslations(String url, PC_IModule module, int langVersion)
	    {
	        this.url = url;
	        this.module = module;
	        this.langVersion = langVersion;
	    }

	    @Override
	    public void run()
	    {
	        try
	        {
	            URL url = new URL(this.url);
	            ZipInputStream zin = new ZipInputStream(url.openStream());
	            PC_Logger.fine("\n\nLanguage pack update downloaded.");
	            PC_Logger.fine("Starting extraction of language files");
	            ZipEntry ze = null;

	            File fileP = new File(GameInfo.getMCDirectory(), GameInfo.getPowerCraftFile() + "/lang");
            	if(!fileP.exists())
            		fileP.mkdirs();
	            
	            while ((ze = zin.getNextEntry()) != null)
	            {
	            	
	                File file = new File(fileP, ze.getName());

	                if (ze.getName().matches("en_US.+"))
	                {
	                    PC_Logger.finer(" - REFRESHING " + ze.getName());

	                    if (file.exists())
	                    {
	                        file.delete();
	                    }

	                    continue;
	                }

	                if (file.exists())
	                {
	                    PC_Logger.finer(" - Updated " + ze.getName());
	                    file.delete();
	                }
	                else
	                {
	                    PC_Logger.finer(" - New file " + ze.getName());
	                }

	                FileOutputStream fout = new FileOutputStream(file);

	                for (int c = zin.read(); c != -1; c = zin.read())
	                {
	                    fout.write(c);
	                }

	                zin.closeEntry();
	                fout.close();
	            }

	            zin.close();
	            PC_Logger.fine("Language pack updated.\n\n");
	            PC_GlobalVariables.config.setInt("modules."+module.getName()+".langVersion", langVersion);
	            PC_GlobalVariables.saveConfig();
	            PC_LangRegistry.loadLanguage(module);
	        }
	        catch (Exception e)
	        {
	            PC_Logger.throwing("PCco_ThreadDownloadTranslations", "run", e);
	            e.printStackTrace();
	        }
	    }
	    
	}
	
	private static List<PC_Struct2<PC_IModule, String>> updateModules = new ArrayList<PC_Struct2<PC_IModule, String>>();
	private static List<PC_Struct3<String, String, String>> newModules = new ArrayList<PC_Struct3<String, String, String>>();
	
	private static void onUpdateInfoDownloaded(String page) {
		PC_Logger.fine("\n\nUpdate information received from server.");

        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new ByteArrayInputStream(page.getBytes("UTF-8")));
            doc.getDocumentElement().normalize();
            NodeList node = doc.getElementsByTagName("Info");

            if (node.getLength() != 1)
            {
                PC_Logger.severe("No Info node found");
                return;
            }

            node = node.item(0).getChildNodes();

            for (int i = 0; i < node.getLength(); i++)
            {
                if (node.item(i).getNodeName().equalsIgnoreCase("update"))
                {
                    Element element = (Element)node.item(i);
                    String sModule = element.getAttribute("module");
                    String sModuleVersion = element.getAttribute("moduleVersion");
                    String sMinecraftVersion = element.getAttribute("minecraftVersion");
                    String sLangVersion = element.getAttribute("langVersion");
                    String sLangLink = element.getAttribute("langLink");
                    String sInfo = element.getTextContent();
                    PC_IModule module = PC_ModuleRegistry.getModule(sModule);

                    if (module != null)
                    {
                        int langVersion = -1;

                        try
                        {
                            langVersion = Integer.parseInt(sLangVersion);
                        }
                        catch (NumberFormatException e) {}

                        if (new CallableMinecraftVersion(null).minecraftVersion().equalsIgnoreCase(sMinecraftVersion))
                        {
                            if (new Version(sModuleVersion).compareTo(new Version(module.getVersion()))>0)
                            {
                            	if(!sModuleVersion.equals(PC_GlobalVariables.config.getString("modules."+module.getName()+".lastIgnoredVersion"))){
                            		updateModules.add(new PC_Struct2<PC_IModule, String>(module, sInfo.trim()));
	                            	PC_GlobalVariables.showUpdateWindow = true;
                            	}
                            }
                        }

                        if (langVersion > PC_GlobalVariables.config.getInt("modules."+module.getName()+".langVersion", 0))
                        {
                            new ThreadDownloadTranslations(sLangLink, module, langVersion).start();
                        }
                    }else if (new CallableMinecraftVersion(null).minecraftVersion().equalsIgnoreCase(sMinecraftVersion)){
                    	if(!sModuleVersion.equals(PC_GlobalVariables.config.getString("modules."+sModule+".lastIgnoredVersion"))){
                    		newModules.add(new PC_Struct3<String, String, String>(sModule, sModuleVersion, sInfo.trim()));
                    		PC_GlobalVariables.showUpdateWindow = true;
                    	}
                    }
                }
            }
        }
        catch (SAXParseException err)
        {
            PC_Logger.severe("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            PC_Logger.severe(" " + err.getMessage());
        }
        catch (SAXException e)
        {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        }
        catch (Throwable t)
        {
            PC_Logger.throwing("mod_PCcore", "onUpdateInfoDownloaded()", t);
            t.printStackTrace();
        }
	}

	public static void ignoreALLUpdateVersion() {
		for(PC_Struct2<PC_IModule, String> e:updateModules){
			PC_GlobalVariables.config.setString("modules."+e.a.getName()+".lastIgnoredVersion", e.b);
		}
		for(PC_Struct3<String, String, String> e:newModules){
			PC_GlobalVariables.config.setString("modules."+e.a+".lastIgnoredVersion", e.b);
		}
		PC_GlobalVariables.saveConfig();
	}

	public static List<PC_Struct2<PC_IModule, String>>  getUpdateModuels() {
		return new ArrayList<PC_Struct2<PC_IModule, String>>(updateModules);
	}
	
	public static List<PC_Struct3<String, String, String>>  getNewModuels() {
		return new ArrayList<PC_Struct3<String, String, String>>(newModules);
	}

	public static void downloadUpdateInfo(String url) {
		new ThreadCheckUpdates(url).start();
	}
	
}
