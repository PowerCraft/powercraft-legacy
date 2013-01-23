package codechicken.nei;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemVisibilityHash
{
	public static class IDInfo
	{
		public TreeSet<Short> damages = new TreeSet<Short>();
		public ArrayList<NBTTagCompound> compounds = new ArrayList<NBTTagCompound>();
	}
	
	public ItemVisibilityHash()
	{		
		try
		{
			loadFromCompound(getCurrentSaveCompound());
		}
		catch(Exception e)
		{
			NEIClientUtils.reportException(e);
		}		
	}
	
	public NBTTagCompound getCurrentSaveCompound()
	{
		NBTTagCompound hashSave = NEIClientConfig.saveCompound.getCompoundTag("vis");
    	NEIClientConfig.saveCompound.setTag("vis", hashSave);
    	
		NBTTagCompound currentSave = hashSave.getCompoundTag("current");
		hashSave.setCompoundTag("current", currentSave);
		return currentSave;
	}

	public void hideItem(int item)
	{
		IDInfo info = hiddenitems.get(item);
		if(info == null)
		{
			info = new IDInfo();
			hiddenitems.put(item, info);
		}
		info.damages.add((short) -1);
	}
	
	public void hideItem(int item, int damage)
	{
		IDInfo info = hiddenitems.get(item);
		if(info == null)
		{
			info = new IDInfo();
			hiddenitems.put(item, info);
		}
		info.damages.add((short) damage);
	}
	
	public void hideItem(int item, NBTTagCompound stackTagCompound)
	{
		IDInfo info = hiddenitems.get(item);
		if(info == null)
		{
			info = new IDInfo();
			hiddenitems.put(item, info);
		}
		if(!info.compounds.contains(stackTagCompound))
		{
			info.compounds.add(stackTagCompound);
		}
	}
	
	public void hideItem(ItemHash item)
	{
		if(item.moreinfo != null)
		{
			hideItem(item.item, item.moreinfo);
		}
		else
		{
			hideItem(item.item, item.damage);
		}
	}
	
	public void unhideItem(int item)
	{
		IDInfo info = hiddenitems.get(item);
		if(info == null)
		{
			return;
		}
		hiddenitems.remove(item);
		return;		
	}
	
	public void unhideItem(int item, int damage)
	{
		IDInfo info = hiddenitems.get(item);
		if(info == null)
		{
			return;
		}
		if(damage == -1)
		{
			hiddenitems.remove(item);
			return;
		}
		else
		{
			info.damages.remove((short)damage);
		}
	}
	
	public void unhideItem(int item, NBTTagCompound stackTagCompound)
	{
		IDInfo info = hiddenitems.get(item);
		if(info == null)
		{
			return;
		}
		info.compounds.remove(stackTagCompound);
	}
	
	public void unhideItem(ItemHash item)
	{
		if(item.moreinfo != null)
		{
			unhideItem(item.item, item.moreinfo);
		}
		else
		{
			unhideItem(item.item, item.damage);
		}
	}
	
	public boolean isItemHidden(int itemID, int damage)
	{
		IDInfo info = hiddenitems.get(itemID);
		if(info == null)
		{
			return false;
		}
		return info.damages.contains((short)damage) || info.damages.contains((short)-1);
	}

	public boolean isItemHidden(int item)
	{
		IDInfo info = hiddenitems.get(item);
		if(info == null)
		{
			return false;
		}
		return info.damages.contains((short)-1);
	}
	
	public boolean isItemHidden(int itemID, NBTTagCompound stackTagCompound)
	{
		IDInfo info = hiddenitems.get(itemID);
		if(info == null)
		{
			return false;
		}
		return info.compounds.contains(stackTagCompound);
	}
	
	public boolean isItemHidden(ItemHash item)
	{
		IDInfo info = hiddenitems.get((int)item.item);
		if(info == null)
		{
			return false;
		}
		if(info.damages.contains(item.damage) || info.damages.contains((short)-1))
		{
			return true;
		}
		else if(item.moreinfo != null)
		{
			return info.compounds.contains(item.moreinfo);
		}
		return false;
	}
	
	private void loadFromCompound(NBTTagCompound readTag)
	{
		hiddenitems = new TreeMap<Integer, IDInfo>();
		for(Object obj : readTag.getTags())
		{
			if(obj instanceof NBTTagList)
			{
				NBTTagList compoundlist = (NBTTagList)obj;
				
				int itemID = Integer.parseInt(compoundlist.getName().substring(1));
				IDInfo info = hiddenitems.get(itemID);
				if(info == null)
				{
					info = new IDInfo();
					hiddenitems.put(itemID, info);
				}
				
				for(int i = 0; i < compoundlist.tagCount(); i++)
				{
					info.compounds.add((NBTTagCompound) compoundlist.tagAt(i));
				}
			}
			else if(obj instanceof NBTTagByteArray)
			{
				NBTTagByteArray damagearray = (NBTTagByteArray)obj;
				
				int itemID = Integer.parseInt(damagearray.getName().substring(1));
				IDInfo info = hiddenitems.get(itemID);
				if(info == null)
				{
					info = new IDInfo();
					hiddenitems.put(itemID, info);
				}
				
				for(int i = 0; i < damagearray.byteArray.length / 2; i++)
				{
					info.damages.add((short) ((damagearray.byteArray[i*2]<<8)+damagearray.byteArray[i*2+1]));
				}
			}
		}
	}

	public void save()
	{
		try
		{
			NBTTagCompound hashSave = NEIClientConfig.saveCompound.getCompoundTag("vis");
        	NEIClientConfig.saveCompound.setTag("vis", hashSave);  
        	
        	hashSave.setCompoundTag("current", constructSaveCompound());
			NEIClientConfig.saveConfig();
		}
		catch(Exception e)
		{
			NEIClientUtils.reportException(e);
		}
	}	
	
	private NBTTagCompound constructSaveCompound()
	{
		NBTTagCompound savecompound = new NBTTagCompound();
		for(Entry<Integer, IDInfo> itemEntry : hiddenitems.entrySet())
		{
			int id = itemEntry.getKey();
			IDInfo info = itemEntry.getValue();
			
			if(info.compounds.size() > 0)
			{
				NBTTagList compoundlist = new NBTTagList();
				for(NBTTagCompound compound : info.compounds)
				{
					compoundlist.appendTag(compound);
				}
				savecompound.setTag("c"+id, compoundlist);
			}
			
			if(info.damages.size() > 0)
			{
				byte[] damagearray = new byte[info.damages.size()*2];
				int i = 0;
				for(short damage : info.damages)
				{
					damagearray[i*2]=(byte) (damage>>8);
					damagearray[i*2+1]=(byte)damage;
					i++;					
				}
				savecompound.setByteArray("d"+id, damagearray);
			}
		}
		return savecompound;
	}

	public static void loadStates()
    {
		NBTTagCompound hashSave = NEIClientConfig.saveCompound.getCompoundTag("vis");
    	NEIClientConfig.saveCompound.setTag("vis", hashSave);  
    			
    	for(int i = 0; i < 7; i++)
    	{
    		NBTTagCompound statesave = hashSave.getCompoundTag("save"+i);
    		if(statesave.getTags().size() > 0)
    		{
    			statesSaved[i] = true;
    		}
    	}
    }
	
	public void loadState(int i)
	{
		NBTTagCompound hashSave = NEIClientConfig.saveCompound.getCompoundTag("vis");
    	NEIClientConfig.saveCompound.setTag("vis", hashSave);  
    	
    	loadFromCompound(hashSave.getCompoundTag("save"+i));
    	
		DropDownFile.dropDownInstance.updateState();
		ItemList.updateSearch();
		NEIClientConfig.vishash.save();
	}
	
	public void saveState(int i)
	{
		NBTTagCompound hashSave = NEIClientConfig.saveCompound.getCompoundTag("vis");
    	NEIClientConfig.saveCompound.setTag("vis", hashSave);  
    	
    	NBTTagCompound saveCompound = getCurrentSaveCompound();
    	saveCompound.setBoolean("saved", true);
		hashSave.setCompoundTag("save"+i, saveCompound);
		statesSaved[i] = true;
		NEIClientConfig.saveConfig();
	}
	
	public void clearState(int i)
	{
		NBTTagCompound hashSave = NEIClientConfig.saveCompound.getCompoundTag("vis");
    	NEIClientConfig.saveCompound.setTag("vis", hashSave);  

		hashSave.setCompoundTag("save"+i, new NBTTagCompound("save"+i));
		statesSaved[i] = false;
		NEIClientConfig.saveConfig();
	}
	
	public static boolean isStateSaved(int i)
	{
		return statesSaved[i];
	}	
	
    private static boolean statesSaved[] = new boolean[7];
	
	public TreeMap<Integer, IDInfo> hiddenitems;
}
