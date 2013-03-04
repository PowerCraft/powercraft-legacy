package powercraft.management.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import powercraft.launcher.PC_Logger;
import powercraft.launcher.PC_ModuleObject;
import powercraft.launcher.PC_Property;
import powercraft.management.PC_DummyModContainer;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils;
import powercraft.management.item.PC_IItemInfo;
import powercraft.management.reflect.PC_ReflectHelper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import cpw.mods.fml.common.Loader;

public final class PC_ModuleRegistry {

	private static HashMap<String, PC_Struct2<PC_IModule, PC_Property>> modules = new HashMap<String, PC_Struct2<PC_IModule, PC_Property>>();

	public static void registerModule(PC_IModule module) {
		PC_Property config = null;
		List l = PC_ReflectHelper.getValue(Loader.class, Loader.instance(), 10, List.class);
		Map m = PC_ReflectHelper.getValue(Loader.class, Loader.instance(), 11, Map.class);
		List l2 = Loader.instance().getActiveModList();
		l = new ArrayList(l);
		m = new HashMap(m);
		PC_DummyModContainer dummy = new PC_DummyModContainer(module);
		l.add(dummy);
		m.put(dummy.getModId(), dummy);
		l2.add(dummy);
		l = ImmutableList.copyOf(l);
		m = ImmutableMap.copyOf(m);
		PC_ReflectHelper.setValue(Loader.class, Loader.instance(), 10, l, List.class);
		PC_ReflectHelper.setValue(Loader.class, Loader.instance(), 11, m, Map.class);
		File f = new File(PC_Utils.GameInfo.getMCDirectory(),
				"config/PowerCraft-" + module.getName() + ".cfg");
		if (f.exists()) {
			try {
				InputStream is = new FileInputStream(f);
				config = PC_Property.loadFromFile(is);
			} catch (FileNotFoundException e) {
				PC_Logger.severe("Can't find File " + f);
			}
		}

		if (config == null) {
			config = new PC_Property(null);
		} 
		modules.put(module.getName(),
				new PC_Struct2<PC_IModule, PC_Property>(module, config));
	}
	
	public static List<PC_IModule> getModules() {
		List<PC_IModule> list = new ArrayList<PC_IModule>();
		for (PC_Struct2<PC_IModule, PC_Property> s : modules.values()) {
			list.add(s.a);
		}
		return list;
	}
	
	public static PC_IModule getModule(Object o) {
		if (o instanceof PC_IItemInfo) {
			return ((PC_IItemInfo) o).getModule();
		}
		return null;
	}
	
	public static PC_IModule getModule(String name) {
		if (modules.containsKey(name)) {
			return modules.get(name).a;
		}
		return null;
	}
	
	public static PC_Property getConfig(PC_ModuleObject module) {
		if (modules.containsKey(module.getName())) {
			return modules.get(module.getName()).b;
		}
		return null;
	}
	
}
