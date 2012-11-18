package cpw.mods.fml.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.logging.Level;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.asm.ASMTransformer;
import cpw.mods.fml.common.asm.transformers.AccessTransformer;
import cpw.mods.fml.common.modloader.BaseModProxy;
import cpw.mods.fml.relauncher.RelaunchClassLoader;

public class ModClassLoader extends URLClassLoader
{
    private static final List<String> STANDARD_LIBRARIES = ImmutableList.of("jinput.jar", "lwjgl.jar", "lwjgl_util.jar");
    private RelaunchClassLoader mainClassLoader;

    public ModClassLoader(ClassLoader parent)
    {
        super(new URL[0], null);
        this.mainClassLoader = (RelaunchClassLoader)parent;
    }

    public void addFile(File modFile) throws MalformedURLException
    {
        URL url = modFile.toURI().toURL();
        mainClassLoader.addURL(url);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException
    {
        return mainClassLoader.loadClass(name);
    }

    public File[] getParentSources()
    {
        List<URL> urls = mainClassLoader.getSources();
        File[] sources = new File[urls.size()];

        try
        {
            for (int i = 0; i < urls.size(); i++)
            {
                sources[i] = new File(urls.get(i).toURI());
            }

            return sources;
        }
        catch (URISyntaxException e)
        {
            FMLLog.log(Level.SEVERE, "Unable to process our input to locate the minecraft code", e);
            throw new LoaderException(e);
        }
    }

    public List<String> getDefaultLibraries()
    {
        return STANDARD_LIBRARIES;
    }

    public Class <? extends BaseModProxy > loadBaseModClass(String modClazzName) throws Exception
    {
        AccessTransformer transformer = (AccessTransformer)mainClassLoader.getTransformers().get(0);
        transformer.ensurePublicAccessFor(modClazzName);
        return (Class <? extends BaseModProxy >) Class.forName(modClazzName, true, this);
    }
}
