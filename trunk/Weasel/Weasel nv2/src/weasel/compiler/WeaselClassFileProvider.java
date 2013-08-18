package weasel.compiler;

import java.util.List;

public interface WeaselClassFileProvider {

	public String getClassSourceFor(String file);
	
	public String getClassSourceVersionFor(String file);
	
	public List<String> allKnowClasses();
	
}
