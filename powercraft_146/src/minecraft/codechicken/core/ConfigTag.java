package codechicken.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;

import cpw.mods.fml.common.FMLCommonHandler;

public class ConfigTag
{	
	public ConfigTag(ConfigTag parent, String name)
	{
		parenttag = parent;
		parentfile = parent.parentfile;
		this.name = name;
		qualifiedname = parent.qualifiedname+"."+name;
		parent.childtags.put(name, this);
	}
	
	public ConfigTag(ConfigFile parent, String name)
	{
		parentfile = parent;
		this.name = name;
		qualifiedname = name;
		parentfile.maintags.put(name, this);
		newline = parent.newlinemode == 2;
	}
	
	public ConfigTag getTag(String tagname, boolean createnew)
	{
		tagname = tagname.replace('_', ' ');
		int dotpos = tagname.indexOf(".");
		String maintagname = dotpos == -1 ? tagname : tagname.substring(0, dotpos);
		ConfigTag basetag = childtags.get(maintagname);
		if(basetag == null)
		{
			if(!createnew)return null;
			
			basetag = getNewTag(maintagname);
			parentfile.saveConfig();
		}
		if(dotpos == -1)
		{
			return basetag;
		}
		else
		{
			return basetag.getTag(tagname.substring(dotpos+1));
		}
	}
	
	public ConfigTag getTag(String tagname)
	{
		return getTag(tagname, true);
	}
	
	public ConfigTag getNewTag(String name)
	{
		return new ConfigTag(this, name);
	}
	
	public boolean tagExists(String tagname)
	{
		return getTag(tagname, false) != null;
	}
	
	/**
	 * @param tagname the Tag to remove
	 * @return true if this tag existed and was removed
	 */
	public boolean deleteTag(String tagname)
	{
		ConfigTag tag = getTag(tagname, false);
		if(tag == null)return false;

		int dotpos = tagname.lastIndexOf(".");
		String lastpart = dotpos == -1 ? tagname : tagname.substring(dotpos+1, tagname.length());
		if(tag.parenttag != null)
		{
			return tag.parenttag.childtags.remove(lastpart) != null;
		}
		else if(tag.parentfile != null)	
		{
			return tag.parentfile.maintags.remove(lastpart) != null;
		}
		else
		{
			return false;
		}
	}
	
	public void setValue(String value)
	{
		this.value = value;
		parentfile.saveConfig();
	}
	
	public void setDefaultValue(String defaultvalue)
	{
		if(value == null)
		{
			value = defaultvalue;
			parentfile.saveConfig();
		}
	}
	
	public void setIntValue(int i)
	{
		setValue(Integer.toString(i));
	}
	
	public void setBooleanValue(boolean b)
	{
		setValue(Boolean.toString(b));
	}
	
	public void setHexValue(int i)
	{
		setValue(Long.toString(((long)i) << 32 >>> 32, 16));
	}
	
	public String getValue()
	{
		return value;
	}
	
	public String getValue(String defaultvalue)
	{
		setDefaultValue(defaultvalue);
		return value;
	}
	
	public int getIntValue()
	{
		return Integer.parseInt(getValue());
	}
	
	public int getIntValue(int defaultvalue)
	{
		if(value == null)
		{
			setIntValue(defaultvalue);
		}
		try
		{
			return getIntValue();
		}
		catch(NumberFormatException nfe)
		{
			setIntValue(defaultvalue);
			return getIntValue();
		}
	}
	
	public boolean getBooleanValue()
	{
		String value = getValue();
		if(value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes")))
		{
			return true;
		}
		else if(value != null && (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no")))
		{
			return false;
		}
		throw new NumberFormatException(qualifiedname+".value="+value);
	}
	
	public boolean getBooleanValue(boolean defaultvalue)
	{
		if(value == null)
		{
			setBooleanValue(defaultvalue);
		}
		try
		{
			return getBooleanValue();
		}
		catch(NumberFormatException nfe)
		{
			setBooleanValue(defaultvalue);
			return getBooleanValue();
		}
	}

	public int getHexValue()
	{
		return (int) Long.parseLong(getValue(), 16);
	}
	
	public int getHexValue(int defaultvalue)
	{
		if(value == null)
		{
			setHexValue(defaultvalue);
		}
		try
		{
			return getHexValue();
		}
		catch(NumberFormatException nfe)
		{
			setHexValue(defaultvalue);
			return getHexValue();
		}
	}

	public void save(PrintWriter writer, int tabs, String bracequalifier)
	{
		String unqualifiedname = qualifiedname;
		unqualifiedname = unqualifiedname.replace(' ', '_');
		
		if(qualifiedname.contains(".") && !bracequalifier.equals(""))
		{
			unqualifiedname = qualifiedname.substring(bracequalifier.length() + 1).replace(' ', '_');
		}
		if(newline)
		{
			ConfigFile.writeLine(writer, "", tabs);
		}
		if(comment != null && !comment.equals(""))
		{
			String doubledelimcomment = comment.replace("::", "~|~");//that way a double will still work
			String[] comments = doubledelimcomment.split(":");
			for(int i = 0; i < comments.length; i++)
			{
				comments[i] = comments[i].replace("~|~", ":");
				ConfigFile.writeLine(writer, "#"+comments[i], tabs);
			}
		}
		if(value != null)
		{
			ConfigFile.writeLine(writer, unqualifiedname+"="+value, tabs);
		}
		if(brace && childtags.size() > 0)
		{
			ConfigFile.writeLine(writer, unqualifiedname, tabs);
			ConfigFile.writeLine(writer, "{", tabs);
			ConfigFile.saveTagTree(writer, childtags, tabs+1, qualifiedname.replace(' ', '_'), sortMode);
			ConfigFile.writeLine(writer, "}", tabs);
		}
		else if(childtags.size() > 0)
		{
			ConfigFile.saveTagTree(writer, childtags, tabs, bracequalifier, sortMode);
		}
	}	

	public ConfigTag setNewLine(boolean b)
	{
		newline = b;
		parentfile.saveConfig();
		return this;
	}
	
	public ConfigTag useBraces()
	{
		brace = true;
		if(parenttag == null && parentfile.newlinemode == 1)
		{
			newline = true;
		}
		parentfile.saveConfig();
		return this;
	}
	
	public ConfigTag setSortMode(int mode)
	{
		sortMode = mode;
		parentfile.saveConfig();
		return this;
	}
	
	public ConfigTag setComment(String string)
	{
		comment = string;
		parentfile.saveConfig();
		return this;
	}
	
	public ConfigTag setPosition(int pos)
	{
		position = pos;
		parentfile.saveConfig();
		return this;
	}
	
	public void loadChildren(BufferedReader reader)
	{
		String comment = "";
		String bracequalifier="";
		try
		{
			while(true)
			{
				String line = ConfigFile.readLine(reader);
				if(line == null)
					break;
				if(line.startsWith("#"))
				{
					if(comment == null || comment.equals(""))
					{
						comment = line.substring(1);
					}
					else
					{
						comment = comment+":"+line.substring(1);
					}
				}
				else if(line.contains("="))
				{
					String qualifiedname = line.substring(0, line.indexOf("="));
					getTag(qualifiedname)
					.setComment(comment)
					.setValue(line.substring(line.indexOf("=")+1));
					comment = "";
					bracequalifier = qualifiedname;
				}
				else if(line.equals("{"))
				{
					getTag(bracequalifier).setComment(comment).useBraces().loadChildren(reader);
					comment = "";
					bracequalifier = "";
				}
				else if(line.equals("}"))
				{
					return;
				}
				else
				{
					bracequalifier = line;
				}
			}
		}
		catch(IOException e)
		{
            FMLCommonHandler.instance().raiseException(e, "Code Chicken Core", true);
		}
	}
	
	public boolean containsTag(String tagname)
	{
		return getTag(tagname, false) != null;
	}
	
	public int getBlockId(String name, int defaultValue)
	{
		return getTag(name).getIntValue(defaultValue);
	}
	
	public int getBlockId(String name)
	{
		int ret = getBlockId(name, IDBase);
		IDBase = ret+1;
		return ret;
	}
	
	public int getItemId(String name, int defaultValue)
	{
		return getTag(name).getIntValue(defaultValue);
	}
	
	public int getItemId(String name)
	{
		int ret = getItemId(name, IDBase);
		IDBase = ret+1;
		return ret;
	}
	
	public int getAcheivementId(String name, int defaultValue)
	{
		return getTag(name).getIntValue(defaultValue);
	}
	
	public ConfigTag setBaseID(int i)
	{
		IDBase = i;
		return this;
	}
	
	public ConfigFile parentfile;
	public ConfigTag parenttag;
	public TreeMap<String, ConfigTag> childtags = new TreeMap<String, ConfigTag>();
	public String name;
	public String qualifiedname;
	public String comment;
	public String value;
	public boolean brace;
	public boolean newline;
	public int sortMode;
	public int position = Integer.MAX_VALUE;
	private int IDBase;	
}
