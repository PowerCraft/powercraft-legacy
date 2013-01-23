package codechicken.core.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import cpw.mods.fml.relauncher.RelaunchClassLoader;

@TransformerExclusions(value={"codechicken.core.asm"})
public class CodeChickenCorePlugin implements IFMLLoadingPlugin, IFMLCallHook
{	
	public static RelaunchClassLoader cl;
	public static File minecraftDir;
	
	@Override
	public String[] getLibraryRequestClass()
	{
        return null;
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]{
				"codechicken.core.asm.ClassHeirachyManager",
				"codechicken.core.asm.CodeChickenAccessTransformer",
				"codechicken.core.asm.InterfaceDependancyTransformer",
				"codechicken.core.asm.FeatureHackTransformer", 
				"codechicken.core.asm.DelegatedTransformer"};
	}

	@Override
	public String getModContainerClass()
	{
		return "codechicken.core.asm.CodeChickenCoreModContainer";
	}

	@Override
	public String getSetupClass()
	{
        return "codechicken.core.asm.CodeChickenCorePlugin";
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		cl = (RelaunchClassLoader) data.get("classLoader");
		if(data.containsKey("mcLocation"))
			minecraftDir = (File) data.get("mcLocation");
	}

	@Override
	public Void call() throws Exception
	{
		CodeChickenAccessTransformer.addTransformerMap("codechickencore_at.cfg");
		scanCodeChickenMods();
		if(!ObfuscationManager.obfuscated)
		    ASMDev.print();
		return null;
	}

	private void scanCodeChickenMods()
	{
		File modsDir = new File(minecraftDir, "mods");
		if(!modsDir.exists())
			return;
		for(File file : modsDir.listFiles(new FilenameFilter()
		{			
			@Override
			public boolean accept(File arg0, String arg1)
			{
				return arg1.endsWith(".jar");
			}
		}))
		{
			try
			{
				JarFile jar = new JarFile(file);
				try
				{
					Manifest manifest = jar.getManifest();
					if(manifest == null)
						continue;
					Attributes attr = manifest.getMainAttributes();
					if(attr == null)
						continue;
					
					String mapFile = attr.getValue("AccessTransformer");
					if(mapFile != null && ObfuscationManager.obfuscated)
					{
					    File temp = extractTemp(jar, mapFile);
					    System.out.println("Adding AccessTransformer: "+mapFile);
						CodeChickenAccessTransformer.addTransformerMap(temp.getPath());
						temp.delete();
					}
					String transformer = attr.getValue("CCTransformer");
					if(transformer != null)
					{
                        System.out.println("Adding CCTransformer: "+transformer);
					    DelegatedTransformer.addTransformer(transformer, jar, file);
					}
				}
				finally
				{
					jar.close();
				}
			}
			catch(Exception e)
			{
                e.printStackTrace();
				System.err.println("CodeChickenCore: Failed to read jar file: "+file.getName());
			}
		}
	}

    private File extractTemp(JarFile jar, String mapFile) throws IOException
    {
        File temp = new File("temp.dat");
        if(!temp.exists())
            temp.createNewFile();
        FileOutputStream fout = new FileOutputStream(temp);
        byte[] data = new byte[4096];
        int read = 0;
        InputStream fin = jar.getInputStream(jar.getEntry(mapFile));
        while((read = fin.read(data)) > 0)
        {
            fout.write(data, 0, read);
        }
        fin.close();
        fout.close();
        return temp;
    }
}
