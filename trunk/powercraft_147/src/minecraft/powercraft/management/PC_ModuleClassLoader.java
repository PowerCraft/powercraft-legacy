package powercraft.management;

import java.util.HashMap;
import java.util.Map;

import powercraft.management.PC_Utils.ValueWriting;
import cpw.mods.fml.relauncher.RelaunchClassLoader;

public class PC_ModuleClassLoader extends ClassLoader {

	private Class<?> c;
	private String className;
	private HashMap<String, Class> loaded = new HashMap<String, Class>();
	private static RelaunchClassLoader cl = (RelaunchClassLoader)PC_ModuleClassLoader.class.getClassLoader();
	
	public PC_ModuleClassLoader(String name, byte[] data) {
		super(cl);
		className = name;
		c = registerClass(name, data);
		Map<String, Class> m = (Map<String, Class>)ValueWriting.getPrivateValue(RelaunchClassLoader.class, cl, 3);
		m.put(className, c);
	}

	private Class<?> registerClass(String name, byte[] data) {
		Class<?> c = null;
		try {
			c = defineClass(name, data, 0, data.length);
			loaded.put(name, c);
			resolveClass(c);
		} catch (ClassFormatError e) {
			e.printStackTrace();
			throw e;
		}
		return c;
	}

	public Class<?> getCreateClass() {
		return c;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return cl.findClass(name);
	}
	
	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		return findClass(name);
	}
	
	
	
}
