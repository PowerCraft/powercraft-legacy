package cpw.mods.fml.common;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.SaveHandler;
import net.minecraft.src.WorldInfo;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

public class FMLDummyContainer extends DummyModContainer implements WorldAccessContainer
{
    public FMLDummyContainer()
    {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "FML";
        meta.name = "Forge Mod Loader";
        meta.version = Loader.instance().getFMLVersionString();
        meta.credits = "Made possible with help from many people";
        meta.authorList = Arrays.asList("cpw, LexManos");
        meta.description = "The Forge Mod Loader provides the ability for systems to load mods " +
                "from the file system. It also provides key capabilities for mods to be able " +
                "to cooperate and provide a good modding environment. " +
                "The mod loading system is compatible with ModLoader, all your ModLoader " +
                "mods should work.";
        meta.url = "https://github.com/cpw/FML/wiki";
        meta.updateUrl = "https://github.com/cpw/FML/wiki";
        meta.screenshots = new String[0];
        meta.logoFile = "";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        return true;
    }

    @Override
    public NBTTagCompound getDataForWriting(SaveHandler handler, WorldInfo info)
    {
        NBTTagCompound fmlData = new NBTTagCompound();
        NBTTagList list = new NBTTagList();

        for (ModContainer mc : Loader.instance().getActiveModList())
        {
            NBTTagCompound mod = new NBTTagCompound();
            mod.setString("ModId", mc.getModId());
            mod.setString("ModVersion", mc.getVersion());
            list.appendTag(mod);
        }

        fmlData.setTag("ModList", list);
        return fmlData;
    }

    @Override
    public void readData(SaveHandler handler, WorldInfo info, Map<String, NBTBase> propertyMap, NBTTagCompound tag)
    {
        if (tag.hasKey("ModList"))
        {
            NBTTagList modList = tag.getTagList("ModList");

            for (int i = 0; i < modList.tagCount(); i++)
            {
                NBTTagCompound mod = (NBTTagCompound) modList.tagAt(i);
                String modId = mod.getString("ModId");
                String modVersion = mod.getString("ModVersion");
                ModContainer container = Loader.instance().getIndexedModList().get(modId);

                if (container == null)
                {
                    FMLLog.severe("This world was saved with mod %s which appears to be missing, things may not work well", modId);
                    continue;
                }

                if (!modVersion.equals(container.getVersion()))
                {
                    FMLLog.info("This world was saved with mod %s version %s and it is now at version %s, things may not work well", modId, modVersion, container.getVersion());
                }
            }
        }
    }
}
