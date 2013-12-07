package xscript.compiler;

import java.util.List;

public interface XSourceProvider {

	public List<String> getProvidedClasses();
	
	public String getClassSource(String name);

	public String getClassCompiler(String name);

	public void saveClass(String name, byte[] save);
	
}
