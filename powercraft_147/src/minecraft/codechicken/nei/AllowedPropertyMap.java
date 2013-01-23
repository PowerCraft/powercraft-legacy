package codechicken.nei;

import java.util.HashMap;
import java.util.HashSet;

public class AllowedPropertyMap
{
	public static HashMap<Integer, String> idToNameMap = new HashMap<Integer, String>();
	public static HashMap<Integer, String> idToFeatureClassMap = new HashMap<Integer, String>();
	public static HashMap<String, Integer> nameToIDMap = new HashMap<String, Integer>();
	public static HashSet<String> nameSet = new HashSet<String>();
	private static int counter;
	
	public static void addAction(String actionName, String featureClass)
	{
		idToNameMap.put(counter, actionName);
		idToFeatureClassMap.put(counter, featureClass);
		nameToIDMap.put(actionName, counter);
		nameSet.add(actionName);
		counter++;
	}
	
	static
	{
		addAction("dawn", "time");
		addAction("noon", "time");
		addAction("dusk", "time");
		addAction("midnight", "time");
		addAction("rain", "rain");
	}
}
