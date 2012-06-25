package net.minecraft.src.weasel;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.PC_Utils;

public class WeaselVariableList extends WeaselObject {
	
	private LinkedHashMap<String, WeaselObject> variableMap;

	public WeaselVariableList() {
		super(WeaselObjectType.VARIABLE_LIST);
		variableMap = new LinkedHashMap<String, WeaselObject>();
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		
		NBTTagList tags = new NBTTagList();
		for(Entry<String,WeaselObject> entry: variableMap.entrySet()){
			NBTTagCompound tag1 = new NBTTagCompound();
			PC_Utils.writeWrappedToNBT(tag1, entry.getKey(), entry.getValue());
			tag1.setString("VariableName", entry.getKey());
			tags.appendTag(tag1);
		}
		tag.setTag("VariableMap", tags);
		
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		
		NBTTagList tags = tag.getTagList("VariableMap");
		for(int i=0; i<tags.tagCount(); i++){
			NBTTagCompound tag1 = (NBTTagCompound) tags.tagAt(i);
			variableMap.put(tag1.getString("VariableName"), WeaselObject.loadObjectFromNBT(tag1));
		}
		
	}	
	

}
